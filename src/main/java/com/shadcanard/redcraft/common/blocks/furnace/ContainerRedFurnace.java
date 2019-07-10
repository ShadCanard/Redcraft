package com.shadcanard.redcraft.common.blocks.furnace;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ContainerRedFurnace extends Container {

    public ContainerRedFurnace(IInventory playerInv, TileRedFurnace te){
        this.te = te;

        addOwnSlots();
        addPlayerSlots(playerInv);
    }

    //region Variables
    private static final int SLOT_SIZE = 18;
    private static final int PLAYER_INV_X = 8;
    private static final int PLAYER_INV_Y = 84;
    private static final int PROGRESS_ID = 0;

    private final TileRedFurnace te;

    //endregion

    //region Methods
    private void addOwnSlots() {
        IItemHandler itemHandler = this.te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        int x = 53;
        int y = 7;

        // Add our own slots
        for (int i = 0; i < te.getInputStackSize() ; i++) {
            addSlotToContainer(new SlotItemHandler(itemHandler, i, x, y));
            x += SLOT_SIZE;
        }
        y = 49;
        x = 53;
        for (int i = te.getInputStackSize(); i < te.getInputStackSize() + te.getOutputStackSize(); i++){
            addSlotToContainer(new SlotItemHandler(itemHandler, i, x, y));
            x += SLOT_SIZE;
        }
    }

    private void addPlayerSlots(IInventory playerInv) {

        //Slots for the Inventory
        int x, y;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                x = (col * SLOT_SIZE) + PLAYER_INV_X;
                y = (row * SLOT_SIZE) + PLAYER_INV_Y;
                this.addSlotToContainer(new Slot(playerInv,col + row * 9 + 10, x, y));
            }
        }

        //Slots for the Hotbar
        for (int col = 0; col < 9; ++col) {
            x = (col * SLOT_SIZE) + PLAYER_INV_X;
            y = 58 + PLAYER_INV_Y;
            this.addSlotToContainer(new Slot(playerInv,col,x,y));
        }
    }

    @Override
    public boolean canInteractWith(@Nullable EntityPlayer playerIn) {
        return te.canInteractWith(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < TileRedFurnace.SLOT_SIZE) {
                if (!this.mergeItemStack(itemstack1, TileRedFurnace.SLOT_SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileRedFurnace.SLOT_SIZE, false)) {
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

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        if(te.getProgress() != te.getClientProgress()){
            te.setClientProgress(te.getProgress());
            for(IContainerListener listener: listeners){
                listener.sendWindowProperty(this,PROGRESS_ID,te.getProgress());
            }
        }
    }

    @Override
    public void updateProgressBar(int id, int data) {
        super.updateProgressBar(id, data);
        if(id == PROGRESS_ID){
            te.setProgress(data);
        }
    }

    //endregion
}
