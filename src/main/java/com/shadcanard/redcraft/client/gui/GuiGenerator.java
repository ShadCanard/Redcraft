package com.shadcanard.redcraft.client.gui;

import com.shadcanard.redcraft.common.blocks.generator.ContainerGenerator;
import com.shadcanard.redcraft.common.blocks.generator.TileGenerator;
import com.shadcanard.redcraft.common.helpers.GuiHelper;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;

public class GuiGenerator extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(References.MOD_ID, "textures/gui/red_generator.png");
    private final TileGenerator te;

    //left,top,right,bottom
    //guiLeft + 51, guiTop + 25,guiLeft + 125, guiTop + 30
    private int energyBarMinX = guiLeft + 51;
    private int energyBarMinY = guiTop + 3;
    private int energyBarMaxX = energyBarMinX + 70;
    private int energyBarMaxY = energyBarMinY + 5;


    //left,top,right,bottom
    //guiLeft + 51, guiTop + 35, guiLeft + 125, guiTop + 40
    private int progressBarMinX = guiLeft + 5;
    private int progressBarMinY = guiTop + 35;
    private int progressBarMaxX = guiLeft + 125;
    private int progressBarMaxY = guiTop + 40;

    public GuiGenerator(TileGenerator tileEntity, ContainerGenerator container, InventoryPlayer playerInv) {
        super(container);
        te = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        energyBarMinX = guiLeft + 10;
        energyBarMinY = guiTop + 5;
        energyBarMaxX = guiLeft + ySize -5;
        energyBarMaxY = energyBarMinY + 5;

        progressBarMinX = guiLeft + 10;
        progressBarMinY = guiTop + 15;
        progressBarMaxX = guiLeft + ySize - 5;
        progressBarMaxY = progressBarMinY + 5;

        drawEnergyBar(te.getClientEnergy(), te.getMaxEnergy());
        drawProgressBar(te.getClientProgress());
    }

    private void drawEnergyBar(int energy, int maxEnergy){
        drawRect(energyBarMinX,energyBarMinY,energyBarMaxX,energyBarMaxY, GuiHelper.BACKGROUND_BAR_COLOR);
        int percentage = energy * 100 / maxEnergy;
        int barSize = GuiHelper.GetBarEndX(progressBarMinX,percentage,progressBarMaxX);
        if(percentage > 0) {
            for (int y = energyBarMinY + 1; y < energyBarMaxY - 1; y++) {
                drawHorizontalLine(energyBarMinX + 1, energyBarMinX + 1 + barSize - 3, y, GuiHelper.ENERGY_BAR_COLOR);
            }
        }
    }

    private void drawProgressBar(int progress){
        drawRect(progressBarMinX, progressBarMinY, progressBarMaxX, progressBarMaxY, GuiHelper.BACKGROUND_BAR_COLOR);
        if(progress > 0) {
            int percentage = progress;
            int barSize = GuiHelper.GetBarEndX(progressBarMinX,percentage,progressBarMaxX);
            if(percentage > 0) {
                for (int y = progressBarMinY + 1; y < progressBarMaxY - 1; y++) {
                    drawHorizontalLine(progressBarMinX + 1, progressBarMinX + barSize - 1, y, GuiHelper.PROGRESS_BAR_COLOR);
                }
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
