package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDragonTracker extends Item{

    private static final HashMap<LivingEntity, Integer> pos = new HashMap<>();

    public ItemDragonTracker() {
        super((new Item.Properties()).group(IceAndFire.TAB_ITEMS).maxStackSize(1));
        setRegistryName("iceandfire", "dragon_tracker");
    }

    @Override
    public void onCreated(ItemStack itemStack, World world, PlayerEntity player) {
        itemStack.setTag(new CompoundNBT());
    }


    @Override
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        return ActionResultType.SUCCESS;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote()) return super.onItemRightClick(worldIn, playerIn, handIn);
        IceAndFire.LOGGER.info("Item USe");
        IceAndFire.LOGGER.info(pos.getOrDefault(playerIn,-1));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    }

    public static int getAngle(LivingEntity living) {
        return pos.getOrDefault(living,0);
    }

    @SubscribeEvent
    public void event(TickEvent.PlayerTickEvent event) {
        IceAndFire.LOGGER.debug("tick");
        ArrayList<Entity> entities = new ArrayList<>(getEnts(event.player.world,(int) event.player.getPosX(), (int) event.player.getPosZ(),128));
        //IceAndFire.LOGGER.info("Entityes: " + entities.size());
        int minDistance = Integer.MAX_VALUE;
        EntityDragonBase minDistanceDragon = null;
        boolean foundDragon = false;
        for(Entity entity : entities) {
            if(entity instanceof EntityDragonBase) {
                //IceAndFire.LOGGER.info("Dragon");
                EntityDragonBase dragonBase = (EntityDragonBase) entity;
                if(dragonBase.isModelDead()) continue;
                //IceAndFire.LOGGER.info("Alive");
                if(dragonBase.isTamed()) continue;
                //IceAndFire.LOGGER.info("NonTamed");
                int distance = (int) Math.sqrt(new BlockPos(dragonBase.getPosX(), event.player.getPosY(), dragonBase.getPosZ()).distanceSq(event.player.getPosition()));
                //IceAndFire.LOGGER.info("MinDist: " + minDistance + " Current: " + distance);
                if(distance < minDistance) {
                    //IceAndFire.LOGGER.info("Distance ok");
                    minDistance = distance;
                    minDistanceDragon = dragonBase;
                    foundDragon = true;
                }
            }
        }
        if(foundDragon) {
            if(minDistance < 20) {
                pos.put(event.player,6);
            } else if (minDistance < 35) {
                pos.put(event.player,5);
            }  else if (minDistance < 50) {
                pos.put(event.player,4);
            }  else if (minDistance < 75) {
                pos.put(event.player,3);
            }  else if (minDistance < 100) {
                pos.put(event.player,2);
            }  else if (minDistance < 128) {
                pos.put(event.player,1);
            }

        } else {
            pos.put(event.player,0);
        }
    }

    public List<Entity> getEnts(World w,int x, int z,int radius) {
        return w.getEntitiesWithinAABB(Entity.class,  new AxisAlignedBB(x-radius, 0, z-radius, x+radius, 255, z+radius));
    }




}
