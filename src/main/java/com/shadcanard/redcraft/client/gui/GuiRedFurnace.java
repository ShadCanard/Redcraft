package com.shadcanard.redcraft.client.gui;

import com.shadcanard.redcraft.common.blocks.furnace.ContainerRedFurnace;
import com.shadcanard.redcraft.common.blocks.furnace.TileRedFurnace;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

public class GuiRedFurnace extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(References.MOD_ID, "textures/gui/red_furnace.png");
    private final TileRedFurnace te;

    //left,top,right,bottom
    //guiLeft + 51, guiTop + 25,guiLeft + 125, guiTop + 30
    private int energyBarMinX = guiLeft + 51;
    private int energyBarMinY = guiTop + 25;
    private int energyBarMaxX = guiLeft + 125;
    private int energyBarMaxY = guiTop + 30;


    //left,top,right,bottom
    //guiLeft + 51, guiTop + 35, guiLeft + 125, guiTop + 40
    private int progressBarMinX = guiLeft + 51;
    private int progressBarMinY = guiTop + 35;
    private int progressBarMaxX = guiLeft + 125;
    private int progressBarMaxY = guiTop + 40;

    public GuiRedFurnace(TileRedFurnace tileEntity, ContainerRedFurnace container, InventoryPlayer playerInv) {
        super(container);
        te = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        energyBarMinX = guiLeft + 51;
        energyBarMinY = guiTop + 25;
        energyBarMaxX = guiLeft + 125;
        energyBarMaxY = guiTop + 30;

        progressBarMinX = guiLeft + 51;
        progressBarMinY = guiTop + 42;
        progressBarMaxX = guiLeft + 125;
        progressBarMaxY = guiTop + 47;

        drawEnergyBar(te.getClientEnergy(), te.getMaxEnergy());
        drawProgressBar(te.getClientProgress(), te.getMaxProgress());
    }

    private void drawEnergyBar(int energy, int maxEnergy){
        drawRect(energyBarMinX,energyBarMinY,energyBarMaxX,energyBarMaxY, 0xff777777);
        int percentage = energy * 100 / maxEnergy;
        for (int i = 0; i < percentage; i++) {
            drawVerticalLine((int) (energyBarMinX + 1 + (i*0.725)), energyBarMinY, (energyBarMaxY - 1),0xffdf0000);
        }
    }

    private void drawProgressBar(int progress, int maxProgress){
        drawRect(progressBarMinX, progressBarMinY, progressBarMaxX, progressBarMaxY, 0xff777777);
        if(te.getClientProgress() > 0) {
            int percentage = te.getClientProgress();
            for (int i = 0; i < (percentage == 100 ? 0 : percentage); i++) {
                drawVerticalLine((int) (progressBarMinX + 1 + (i * 0.725)), progressBarMinY, (progressBarMaxY - 1), 0xff40cf40);
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX,mouseY);

        if(mouseX > energyBarMinX && mouseX < energyBarMaxX && mouseY > energyBarMinY && mouseY < energyBarMaxY){
            drawHoveringText(Collections.singletonList(te.getClientEnergy() + " / " + te.getMaxEnergy() + " FE"), mouseX, mouseY, fontRenderer);
        }
        if(mouseX > progressBarMinX && mouseX < progressBarMaxX && mouseY > progressBarMinY && mouseY < progressBarMaxY){
            int percentage = te.getClientProgress();
            drawHoveringText(Collections.singletonList("Progress : " + (percentage == 100 ? "0" : percentage) + "%"), mouseX, mouseY, fontRenderer);
        }
    }
}
