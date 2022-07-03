package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.dispenser.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemFrameEntity;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
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

public class ItemDragonTracker extends Item {

    private static final HashMap<LivingEntity, Integer> pos = new HashMap<>();
    private static boolean test = false;

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
        if (!worldIn.isRemote()) return super.onItemRightClick(worldIn, playerIn, handIn);
        IceAndFire.LOGGER.info("Item USe");
        IceAndFire.LOGGER.info(pos.getOrDefault(playerIn, -1));

        test = true;

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    }

    public static int getAngle(LivingEntity living) {
        return pos.getOrDefault(living, 0);
    }

    @SubscribeEvent
    public void event(TickEvent.PlayerTickEvent event) {

        ArrayList<Entity> entities = new ArrayList<>(getEnts(event.player.world, (int) event.player.getPosX(), (int) event.player.getPosZ(), 128));
        //IceAndFire.LOGGER.info("Entityes: " + entities.size());
        int minDistance = Integer.MAX_VALUE;
        EntityDragonBase minDistanceDragon = null;
        boolean foundDragon = false;
        for (Entity entity : entities) {
            if (entity instanceof EntityDragonBase) {
                //IceAndFire.LOGGER.info("Dragon");
                EntityDragonBase dragonBase = (EntityDragonBase) entity;
                if (dragonBase.isModelDead()) continue;
                //IceAndFire.LOGGER.info("Alive");
                if (dragonBase.isTamed()) continue;
                //IceAndFire.LOGGER.info("NonTamed");
                int distance = (int) Math.sqrt(new BlockPos(dragonBase.getPosX(), event.player.getPosY(), dragonBase.getPosZ()).distanceSq(event.player.getPosition()));
                //IceAndFire.LOGGER.info("MinDist: " + minDistance + " Current: " + distance);
                if (distance < minDistance) {
                    //IceAndFire.LOGGER.info("Distance ok");
                    minDistance = distance;
                    minDistanceDragon = dragonBase;
                    foundDragon = true;
                }
            }
        }
        if (foundDragon) {
            Vector2f view = new Vector2f((float) Vector3d.fromPitchYaw(event.player.getPitchYaw()).getX(), (float) Vector3d.fromPitchYaw(event.player.getPitchYaw()).getZ());
            Vector2f dragon = new Vector2f(minDistanceDragon.getPosition().getX() - event.player.getPosition().getX(), minDistanceDragon.getPosition().getZ() - event.player.getPosition().getZ());
            //Vector2f dragon = new Vector2f(-104 - event.player.getPosition().getX(), -248 - event.player.getPosition().getZ());
            float top = view.x * dragon.x + view.y * dragon.y;
            float bottom = (float) (Math.sqrt(view.x * view.x + view.y * view.y) * Math.sqrt(dragon.x * dragon.x + dragon.y * dragon.y));
            float cos = top / bottom;

            float result = (float) Math.acos(cos);

            if (!((dragon.y / dragon.x) * view.x > view.y)) result = (float) (2 * Math.PI - result);

            int compassvalue = 0;
            result = (float) Math.toDegrees(result);

            if(dragon.x >= 0) {
                result = 360 - result;
            }
            if(test) {
                IceAndFire.LOGGER.info(result + "" + !((dragon.y / dragon.x) * view.x > view.y) + "");
                test = false;
            }
            if(result >= 0 && result < 6) {
                compassvalue = 16;
            } else if (result >= 6 && result < 17) {
                compassvalue = 15;
            } else if (result >= 17 && result < 29) {
                compassvalue = 14;
            } else if (result >= 29 && result < 40) {
                compassvalue = 13;
            } else if (result >= 40 && result < 51) {
                compassvalue = 12;
            } else if (result >= 51 && result < 62) {
                compassvalue = 11;
            } else if (result >= 62 && result < 73) {
                compassvalue = 10;
            } else if (result >= 73 && result < 107) {
                compassvalue = 8;
            } else if (result >= 107 && result < 119) {
                compassvalue = 6;
            } else if (result >= 119 && result < 130) {
                compassvalue = 5;
            } else if (result >= 130 && result < 141) {
                compassvalue = 4;
            } else if (result >= 141 && result < 152) {
                compassvalue = 3;
            } else if (result >= 152 && result < 164) {
                compassvalue = 2;
            } else if (result >= 164 && result < 174) {
                compassvalue = 1;
            } else if (result >= 174 && result < 186) {
                compassvalue = 0;
            } else if (result >= 186 && result < 197) {
                compassvalue = 31;
            } else if (result >= 197 && result < 208) {
                compassvalue = 30;
            } else if (result >= 208 && result < 220) {
                compassvalue = 29;
            } else if (result >= 220 && result < 231) {
                compassvalue = 28;
            } else if (result >= 231 && result < 242) {
                compassvalue = 27;
            } else if (result >= 242 && result < 254) {
                compassvalue = 26;
            } else if (result >= 254 && result < 287) {
                compassvalue = 24;
            } else if (result >= 287 && result < 298) {
                compassvalue = 22;
            } else if (result >= 298 && result < 310) {
                compassvalue = 21;
            } else if (result >= 310 && result < 321) {
                compassvalue = 20;
            } else if (result >= 321 && result < 332) {
                compassvalue = 19;
            } else if (result >= 332 && result < 343) {
                compassvalue = 18;
            } else if (result >= 343 && result < 354) {
                compassvalue = 17;
            } else if (result >= 354 && result < 360) {
                compassvalue = 16;
            }
            pos.put(event.player,compassvalue);


        } else {
            pos.put(event.player, -1);
        }


    }

    public List<Entity> getEnts(World w, int x, int z, int radius) {
        return w.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x - radius, 0, z - radius, x + radius, 255, z + radius));
    }

    private double getAngle(LivingEntity entity, BlockPos pos) {
        return Math.atan2((double) pos.getZ() - entity.getPositionVec().z, (double) pos.getX() - entity.getPositionVec().x);
    }


}
