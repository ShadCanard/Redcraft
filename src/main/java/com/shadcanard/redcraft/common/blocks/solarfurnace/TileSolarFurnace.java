package com.shadcanard.redcraft.common.blocks.solarfurnace;

import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@SuppressWarnings("SameReturnValue")
public class TileSolarFurnace extends TileEntity implements ITickable {

    //region Variables
    private static final int INPUT_SLOT_SIZE = 4;
    private static final int OUTPUT_SLOT_SIZE = 4;
    public static final int SLOT_SIZE = INPUT_SLOT_SIZE + OUTPUT_SLOT_SIZE;
    private static final int MAX_PROGRESS = 800;
    public static final ResourceLocation resourceLocation = new ResourceLocation(References.MOD_ID, "tile_" + Names.Blocks.BLOCK_SOLAR_FURNACE);

    private int progress = 0;
    private int clientProgress = -1;
    private final ItemStackHandler inputStack = new ItemStackHandler(INPUT_SLOT_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            TileSolarFurnace.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
        }
    };
    private final ItemStackHandler outputStack = new ItemStackHandler(OUTPUT_SLOT_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            TileSolarFurnace.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };
    private final CombinedInvWrapper combinedStack = new CombinedInvWrapper(inputStack, outputStack);

    //endregion

    //region Getters and Setters
    public static int getMaxProgress() {
        return MAX_PROGRESS;
    }

    public int getProgress() {
        return progress;
    }
    public void setProgress(int progress) {
        this.progress = progress;
    }


    public int getInputStackSize(){
        return INPUT_SLOT_SIZE;
    }
    public int getOutputStackSize(){
        return INPUT_SLOT_SIZE;
    }

    public int getClientProgress() {
        return clientProgress;
    }
    public void setClientProgress(int clientProgress) {
        this.clientProgress = clientProgress;
    }
    //endregion

    //region Methods

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("itemsIn")) inputStack.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
        if(compound.hasKey("itemsOut")) outputStack.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
        progress = compound.getInteger("progress");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("itemsIn", inputStack.serializeNBT());
        compound.setTag("itemsOut", outputStack.serializeNBT());
        compound.setInteger("progress",progress);
        return compound;
    }

    public boolean canInteractWith(EntityPlayer playerIn){
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D,0.5D,0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(@Nullable Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nullable Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY){
            if(facing == null) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(combinedStack);
            else if(facing == EnumFacing.DOWN) return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(outputStack);
            else return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStack);
        }
        return super.getCapability(capability, facing);
    }


    private boolean insertOutput(ItemStack output, boolean simulate){
        for (int i = 0; i < OUTPUT_SLOT_SIZE; i++) {
            ItemStack remaining = outputStack.insertItem(i, output,simulate);
            if(remaining.isEmpty()) return true;
        }
        return false;
    }

    private void startSmelt(){
        for (int i = 0; i < INPUT_SLOT_SIZE; i++) {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(inputStack.getStackInSlot(i));
            if(!result.isEmpty()){
                if(insertOutput(result.copy(),true)){
                    progress = MAX_PROGRESS;
                    markDirty();
                }
                break;
            }
        }
    }

    private void attemptSmelt(){
        for (int i = 0; i < INPUT_SLOT_SIZE; i++) {
            ItemStack result = FurnaceRecipes.instance().getSmeltingResult(inputStack.getStackInSlot(i));
            if(!result.isEmpty()) {
                if (insertOutput(result.copy(), false)) {
                    inputStack.extractItem(i,1,false);
                }
            }
        }
    }

    @Override
    public void update() {
        if(!world.isRemote) {
            if (world.canBlockSeeSky(pos.up()) && world.isDaytime()) {
                if (progress > 0) {
                    progress--;
                    if (progress <= 0) {
                        attemptSmelt();
                    }
                    markDirty();
                } else {
                    startSmelt();
                }
            }
        }
    }

    //endregion



}
