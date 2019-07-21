package com.shadcanard.redcraft.client.gui;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.items.ContainerDebug;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.io.IOException;

public class GuiDebugTool extends GuiContainer {

    private static final ResourceLocation background = new ResourceLocation(References.MOD_ID, "textures/gui/debugtool.png");
    private ContainerDebug container;
    private EntityPlayer player;
    public GuiDebugTool(ContainerDebug container, InventoryPlayer playerInv) {
        super(container);
        player = playerInv.player;
        this.container = container;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        renderHoveredToolTip(mouseX,mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0){
            ItemStack stack = container.inputStack.getStackInSlot(0);
            player.sendMessage(new TextComponentString("Debug Infos sent on console. Please read your logs."));
            RedCraft.logger.info("Name : " + stack.getDisplayName());
            RedCraft.logger.info("Size : " + stack.getCount());
            RedCraft.logger.info("Is burnable : " + !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty());
            RedCraft.logger.info("Is Fuel : " + (net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack) >= 0));
            RedCraft.logger.info("Fuel time : " + net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack));
        }
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        addButton(new GuiButton(0,guiLeft + 50, guiTop + 50, 100,20, "Debug"));
        super.initGui();
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        container.onDropItem(player);
    }
}
