package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonForge;
import com.github.alexthe666.iceandfire.inventory.ContainerLectern;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GuiDragonForge extends ContainerScreen<ContainerDragonForge> {
    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
    private final PlayerInventory playerInventory;
    private ContainerDragonForge tileFurnace;
    private boolean ice;

    public GuiDragonForge(ContainerDragonForge container, PlayerInventory inv, ITextComponent name) {
        super(container, inv, name);
        this.playerInventory = inv;
        this.tileFurnace = container;
        if (tileFurnace instanceof ContainerDragonForge) {
            this.ice = !tileFurnace.isFire;
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (tileFurnace != null) {
            String s = I18n.format(ice ? "tile.iceandfire.dragonforge_ice_core.name" : "tile.iceandfire.dragonforge_fire_core.name");
            this.font.drawString(s, this.xSize / 2 - this.font.getStringWidth(s) / 2, 6, 4210752);
        }
        this.font.drawString(this.playerInventory.getDisplayName().getFormattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (ice) {
            this.minecraft.getTextureManager().bindTexture(TEXTURE_ICE);
        } else {
            this.minecraft.getTextureManager().bindTexture(TEXTURE_FIRE);
        }
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.blit(k, l, 0, 0, this.xSize, this.ySize);
        int i1 = this.func_175381_h(126);
        this.blit(k + 12, l + 23, 0, 166, i1, 38);
    }

    private int func_175381_h(int p_175381_1_) {
        TileEntity te = IceAndFire.PROXY.getRefrencedTE();
        int j = 0;
        int maxCookTime = 200;
        if(te instanceof TileEntityDragonforge){
            j = ((TileEntityDragonforge)te).cookTime;
            maxCookTime = ((TileEntityDragonforge)te).getMaxCookTime();
        }
        return j != 0 ? j * p_175381_1_ / maxCookTime : 0;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}