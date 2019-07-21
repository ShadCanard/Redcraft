package com.shadcanard.redcraft.common.items;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.generator.TileGenerator;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerDebug extends Container {

    public EntityPlayer player;

    public ContainerDebug(EntityPlayer playerIn) {
        super();
        player = playerIn;
        addOwnSlots();
        addPlayerSlots(playerIn.inventory);
    }

    private static final int PLAYER_INV_X = 8;
    private static final int PLAYER_INV_Y = 84;
    public ItemStackHandler inputStack = new ItemStackHandler(1){
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            ItemStack stack = this.getStackInSlot(slot);
            if(stack != ItemStack.EMPTY) {
                player.sendMessage(new TextComponentString("Debug Infos sent on console. Please read your logs."));
                RedCraft.logger.info("Name : " + stack.getDisplayName());
                RedCraft.logger.info("Size : " + stack.getCount());
                RedCraft.logger.info("Is smeltable : " + !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty());
                if(!FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty()){
                    RedCraft.logger.info("Smelting Result : " + FurnaceRecipes.instance().getSmeltingResult(stack).getDisplayName());
                }
                RedCraft.logger.info("Is Fuel : " + (TileEntityFurnace.getItemBurnTime(stack) > 0));
                if((TileEntityFurnace.getItemBurnTime(stack) > 0)) {
                    RedCraft.logger.info("Fuel time : " + TileEntityFurnace.getItemBurnTime(stack));
                }
            }
            super.onContentsChanged(slot);
        }
    };

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    //region Methods
    private void addOwnSlots() {
        int x = 80;
        int y = 35;

        // Add our own slots
        addSlotToContainer(new SlotItemHandler(inputStack, 0, x, y));
    }

    private void addPlayerSlots(IInventory playerInv) {

        //Slots for the Inventory
        int x, y;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                x = (col * References.SLOT_SIZE) + PLAYER_INV_X;
                y = (row * References.SLOT_SIZE) + PLAYER_INV_Y;
                this.addSlotToContainer(new Slot(playerInv,col + row * 9 + 10, x, y));
            }
        }

        //Slots for the Hotbar
        for (int col = 0; col < 9; ++col) {
            x = (col * References.SLOT_SIZE) + PLAYER_INV_X;
            y = 58 + PLAYER_INV_Y;
            this.addSlotToContainer(new Slot(playerInv,col,x,y));
        }
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 1) {
                if (!this.mergeItemStack(itemstack1, 1, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public void onDropItem(EntityPlayer playerIn) {
        if(inputStack.getStackInSlot(0) != ItemStack.EMPTY){
            int res = playerIn.inventory.storeItemStack(inputStack.getStackInSlot(0));
            if(res == -1){
                playerIn.dropItem(inputStack.getStackInSlot(0),true,true);
            }
        }
    }
    //endregion

}
