package com.shadcanard.redcraft.common.blocks.furnace.poweredfurnace;

import com.shadcanard.redcraft.common.config.BasicMachinesConfig;
import com.shadcanard.redcraft.common.blocks.machine.MachineState;
import com.shadcanard.redcraft.common.blocks.machine.TileMachineBase;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.tools.RedcraftEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileRedFurnace extends TileMachineBase implements ITickable {

    //region variables
    public static final ResourceLocation resourceLocation = new ResourceLocation(References.MOD_ID, "tile_" + Names.Blocks.BLOCK_RED_FURNACE);
    private static final int INPUT_SLOT_SIZE = 4;
    private static final int OUTPUT_SLOT_SIZE = 4;
    public static final int SLOT_SIZE = INPUT_SLOT_SIZE + OUTPUT_SLOT_SIZE;

    private MachineState STATE = MachineState.OFF;
    private int progress = 0;
    private int clientProgress = -1;

    private int clientEnergy = -1;

    //region Handlers
    private final RedcraftEnergyStorage energyStorage = new RedcraftEnergyStorage(BasicMachinesConfig.basicMachineMaxPower, BasicMachinesConfig.basicMachineMaxReceive);
    private final ItemStackHandler inputStack = new ItemStackHandler(INPUT_SLOT_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            TileRedFurnace.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty();
        }
    };
    private final ItemStackHandler outputStack = new ItemStackHandler(OUTPUT_SLOT_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            TileRedFurnace.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return false;
        }
    };
    public final CombinedInvWrapper combinedStack = new CombinedInvWrapper(inputStack, outputStack);
    //endregion

    //endregion

    //region Getters and Setters

    public CombinedInvWrapper getCombinedStack(){
        return combinedStack;
    }

    public int getClientProgress() {
        return clientProgress;
    }

    void setClientProgress(int clientProgress) {
        this.clientProgress = clientProgress;
    }

    public int getProgress() {
        return progress;
    }

    void setProgress(int progress) {
        this.progress = progress;
    }

    int getInputStackSize(){
        return INPUT_SLOT_SIZE;
    }
    int getOutputStackSize(){
        return INPUT_SLOT_SIZE;
    }


    private static int getMaxProgress() {
        return BasicMachinesConfig.basicMachineMaxProgress;
    }

    public int getClientEnergy() {
        return clientEnergy;
    }

    void setClientEnergy(int clientEnergy) {
        this.clientEnergy = clientEnergy;
    }

    public int getEnergy(){
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergy() {
        return energyStorage.getMaxEnergyStored();
    }

    public MachineState getState() {
        return STATE;
    }

    /**
     * Sets the current state of a block. Detects if the block's state is different of the current block state. If so, updates the block Client Side
     * @param STATE State of the machine
     */
    public void setState(MachineState STATE) {
        if(this.STATE != STATE){
            this.STATE = STATE;
            markDirty();
            IBlockState blockState = world.getBlockState(pos);
            getWorld().notifyBlockUpdate(pos,blockState,blockState,3);
        }
        this.STATE = STATE;
    }

    @Nonnull
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbtTagCompound = super.getUpdateTag();
        if(STATE == null) STATE = MachineState.OFF;
        nbtTagCompound.setInteger("state", STATE.ordinal());
        return nbtTagCompound;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos,1,getUpdateTag());
    }

    /**
     * Will be called server side each time an update is needed
     * @param net Network Manager for comms
     * @param pkt Package to receive
     */
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        int stateIndex = pkt.getNbtCompound().getInteger("state");
        if(STATE == null) STATE = MachineState.OFF;
        if(world.isRemote && stateIndex != STATE.ordinal()){
            STATE = MachineState.VALUES[stateIndex];
            world.markBlockRangeForRenderUpdate(pos,pos);
        }
    }

    //endregion

    //region Methods
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("itemsIn")) inputStack.deserializeNBT((NBTTagCompound) compound.getTag("itemsIn"));
        if(compound.hasKey("itemsOut")) outputStack.deserializeNBT((NBTTagCompound) compound.getTag("itemsOut"));
        energyStorage.setEnergy(compound.getInteger("energy"));
        progress = compound.getInteger("progress");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("itemsIn", inputStack.serializeNBT());
        compound.setTag("itemsOut", outputStack.serializeNBT());
        compound.setInteger("progress",progress);
        compound.setInteger("energy",energyStorage.getEnergyStored());
        return compound;
    }

    boolean canInteractWith(EntityPlayer playerIn){
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D,0.5D,0.5D)) <= 64D;
    }

    @Override
    public boolean hasCapability(@Nullable Capability<?> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        if(capability == CapabilityEnergy.ENERGY) return true;
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
        if(capability == CapabilityEnergy.ENERGY){
            return CapabilityEnergy.ENERGY.cast(energyStorage);
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
                    setState(MachineState.WORKING);
                    progress = BasicMachinesConfig.basicMachineMaxProgress;
                    markDirty();
                    break;
                }
            }
        }
        setState(MachineState.OFF);
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
        if(!world.isRemote){
            boolean canProcess = false;
            for (int i = 0; i < getInputStackSize(); i++) {
                if(!inputStack.getStackInSlot(i).isEmpty()) canProcess = true;
            }

            if(!canProcess) setProgress(0);
            if(energyStorage.getEnergyStored() < BasicMachinesConfig.basicMachineRfPerTick) return;
            if(progress > 0) {
                setState(MachineState.WORKING);
                int countProcessing = 0;
                for (int i = 0; i < getInputStackSize(); i++) {
                    if(!inputStack.getStackInSlot(i).isEmpty()) countProcessing++;
                }
                if(energyStorage.getEnergyStored() > BasicMachinesConfig.basicMachineRfPerTick * countProcessing){
                    progress--;
                    energyStorage.consumePower(BasicMachinesConfig.basicMachineRfPerTick * countProcessing);
                }
                if (progress <= 0) {
                    attemptSmelt();
                }
                markDirty();
            } else {
                startSmelt();
            }
        }
    }

    @Override
    public ArrayList<String> getDebug() {
        ArrayList<String> out = new ArrayList<>();
        out.add("Energy Capacity: " + energyStorage.getMaxEnergyStored() + "RF");
        out.add("Current Energy: " + energyStorage.getEnergyStored() + "RF");
        out.add("Current Progress: " + progress + " / " + getMaxProgress());

        out.add("------ INPUT SLOTS ------");
        for (int i = 0; i < INPUT_SLOT_SIZE; i++) {
            ItemStack stack = inputStack.getStackInSlot(i);
            if(stack == ItemStack.EMPTY){
                out.add("--- No stack in input slot " + i + " ---");
            }else{
                out.add("--- Stack in input slot " + i + " ---");
                out.add("Name : " + stack.getDisplayName());
                out.add("Size : " + stack.getCount());
                out.add("Is burnable : " + !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty());
                out.add("Is Fuel : " + (net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack) >= 0));
                out.add("Fuel time : " + net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack));
            }
        }
        out.add("------ OUTPUT SLOTS ------");
        for (int i = 0; i < OUTPUT_SLOT_SIZE; i++) {
            ItemStack stack = outputStack.getStackInSlot(i);
            if(stack == ItemStack.EMPTY){
                out.add("--- No stack in input slot " + i + " ---");
            }else{
                out.add("--- Stack in input slot " + i + " ---");
                out.add("Name : " + stack.getDisplayName());
                out.add("Size : " + stack.getCount());
                out.add("Is burnable : " + !FurnaceRecipes.instance().getSmeltingResult(stack).isEmpty());
                out.add("Is Fuel : " + (net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack) >= 0));
                out.add("Fuel time : " + net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack));
            }
        }
        return out;
    }


    //endregion
}
