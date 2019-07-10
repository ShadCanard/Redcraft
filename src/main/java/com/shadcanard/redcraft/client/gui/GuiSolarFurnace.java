package com.shadcanard.redcraft.client.gui;

import com.shadcanard.redcraft.common.blocks.solarfurnace.ContainerSolarFurnace;
import com.shadcanard.redcraft.common.blocks.solarfurnace.TileSolarFurnace;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiSolarFurnace extends GuiContainer {

    public static final ResourceLocation background = new ResourceLocation(References.MOD_ID, "textures/gui/red_furnace.png");
    public TileSolarFurnace te;
    public GuiSolarFurnace(TileSolarFurnace tileEntity, ContainerSolarFurnace container, InventoryPlayer playerInv) {
        super(container);
        te = tileEntity;
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        if(te.getProgress() > 0){
            int percentage = 100 - te.getClientProgress() * 100 / te.getMaxProgress();
            drawString(mc.fontRenderer, "Progress : " + percentage + "%", guiLeft + 50, guiTop+30, 0xffffff);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX,mouseY);
    }
}