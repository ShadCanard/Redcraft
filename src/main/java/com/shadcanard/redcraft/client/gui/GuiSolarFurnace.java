package com.shadcanard.redcraft.client.gui;

import com.shadcanard.redcraft.common.blocks.furnace.solarfurnace.ContainerSolarFurnace;
import com.shadcanard.redcraft.common.blocks.furnace.solarfurnace.TileSolarFurnace;
import com.shadcanard.redcraft.common.config.SolarMachinesConfig;
import com.shadcanard.redcraft.common.helpers.GuiHelper;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.Collections;

public class GuiSolarFurnace extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(References.MOD_ID, "textures/gui/red_furnace.png");
    private final TileSolarFurnace te;

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

    public GuiSolarFurnace(TileSolarFurnace tileEntity, ContainerSolarFurnace container, InventoryPlayer playerInv) {
        super(container);
        te = tileEntity;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        drawCanWork();
        energyBarMinX = guiLeft + 51;
        energyBarMinY = guiTop + 25;
        energyBarMaxX = guiLeft + 125;
        energyBarMaxY = guiTop + 30;

        progressBarMinX = guiLeft + 51;
        progressBarMinY = guiTop + 42;
        progressBarMaxX = guiLeft + 125;
        progressBarMaxY = guiTop + 47;

        if(SolarMachinesConfig.doesSolarHaveRfStorage) drawEnergyBar(te.getClientEnergy(), te.getMaxEnergy());
        drawProgressBar(te.getClientProgress());
    }

    private void drawEnergyBar(int energy, int maxEnergy){
        drawRect(energyBarMinX,energyBarMinY,energyBarMaxX,energyBarMaxY, GuiHelper.BACKGROUND_BAR_COLOR);
        int percentage = energy * 100 / maxEnergy;
        int barSize = GuiHelper.GetBarEndX(progressBarMinX,percentage,progressBarMaxX);
        if(percentage > 0) {
            for (int y = energyBarMinY + 1; y < energyBarMaxY - 1; y++) {
                drawHorizontalLine(energyBarMinX + 1, energyBarMinX + 1 + barSize - (barSize > energyBarMinX + 1 ? 0 : 3), y, GuiHelper.ENERGY_BAR_COLOR);
            }
        }
    }

    private void drawProgressBar(int progress){
        drawRect(progressBarMinX, progressBarMinY, progressBarMaxX, progressBarMaxY, GuiHelper.BACKGROUND_BAR_COLOR);
        if(te.getClientProgress() > 0) {
            int percentage = progress;
            int barSize = GuiHelper.GetBarEndX(progressBarMinX,percentage,progressBarMaxX);
            if(percentage > 0 && percentage != 100) {
                for (int y = progressBarMinY + 1; y < progressBarMaxY - 1; y++) {
                    drawHorizontalLine(progressBarMinX + 1, progressBarMinX + barSize - 1, y, GuiHelper.PROGRESS_BAR_COLOR);
                }
            }
        }
    }

    private void drawCanWork(){
        drawIsDay();
        drawCanSeeSky();
    }

    private void drawCanSeeSky(){
        World world = te.getWorld();
        boolean canSeeSky = world.canBlockSeeSky(te.getPos().up());
        drawRect(guiLeft + 5, guiTop + 5, guiLeft + 5 + 16, guiTop + 5 + 16, canSeeSky ? 0xFF87CEEB : 0xff707070);
    }

    private void drawIsDay(){
        World world = te.getWorld();
        boolean isDaytime = world.isDaytime();
        drawTexturedModalRect( guiLeft + 5, guiTop + 5 + 16, xSize, (isDaytime ? 0:16), 16,16 );
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
        if(mouseX > guiLeft + 5 && mouseX < guiLeft + 5 + 16 && mouseY > guiTop + 5 && mouseY < guiTop + 5 + 16){
            drawHoveringText(Collections.singletonList("Can see sky ? " + (te.getWorld().canBlockSeeSky(te.getPos().up()) ? "Yes" : "No")), mouseX,mouseY,fontRenderer);
        }
        if(mouseX > guiLeft + 5 && mouseX < guiLeft + 5 + 16 && mouseY > guiTop + 5 + 16 && mouseY < guiTop + 5 + 32){
            drawHoveringText(Collections.singletonList("Is Daytime ? " + (te.getWorld().isDaytime() ? "Yes" : "No")), mouseX,mouseY,fontRenderer);
        }
    }
}