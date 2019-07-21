package com.shadcanard.redcraft.common.blocks.generator;

import com.shadcanard.redcraft.common.BasicGeneratorConfig;
import com.shadcanard.redcraft.common.BasicMachinesConfig;
import com.shadcanard.redcraft.common.blocks.machine.MachineState;
import com.shadcanard.redcraft.common.blocks.machine.TileMachineBase;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.tools.RedcraftEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileGenerator extends TileMachineBase implements ITickable {

    //region Variables
    public static final ResourceLocation resourceLocation = new ResourceLocation(References.MOD_ID, "tile_" + Names.Blocks.BLOCK_RED_GENERATOR);
    private static final int INPUT_SLOT_SIZE = 1;
    private static final int OUTPUT_SLOT_SIZE = 0;
    public static final int SLOT_SIZE = INPUT_SLOT_SIZE + OUTPUT_SLOT_SIZE;

    private MachineState STATE = MachineState.OFF;
    private int progress = 0;
    protected int currentItemBurnTime = -1;
    private int clientProgress = -1;

    private int clientEnergy = -1;

    //region Handlers
    private final RedcraftEnergyStorage energyStorage = new RedcraftEnergyStorage(BasicMachinesConfig.basicMachineMaxPower, 0,0);
    public final ItemStackHandler inputStack = new ItemStackHandler(INPUT_SLOT_SIZE){
        @Override
        protected void onContentsChanged(int slot) {
            TileGenerator.this.markDirty();
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return isItemBurnable(stack);
        }
    };
    //endregion

    //endregion

    //region Getters and Setters

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


    public static int getMaxProgress() {
        return BasicMachinesConfig.basicMachineMaxProgress;
    }

    public int getClientEnergy() {
        return clientEnergy;
    }

    public void setClientEnergy(int clientEnergy) {
        this.clientEnergy = clientEnergy;
    }

    public int getEnergy(){
        return energyStorage.getEnergyStored();
    }

    public MachineState getState() {
        return STATE;
    }

    /**
     * Sets the current state of a block. Detects if the block's state is different of the current block state. If so, updates the block Client Side
     * @param STATE
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
     * @param net
     * @param pkt
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
        energyStorage.setEnergy(compound.getInteger("energy"));
        progress = compound.getInteger("progress");
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("itemsIn", inputStack.serializeNBT());
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
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(inputStack);
        }
        if(capability == CapabilityEnergy.ENERGY){
            return CapabilityEnergy.ENERGY.cast(energyStorage);
        }
        return super.getCapability(capability, facing);
    }

    private int getVanillaBurnTime(ItemStack item){
        return TileEntityFurnace.getItemBurnTime(item);
    }

    private boolean isItemBurnable(ItemStack item){
        return getVanillaBurnTime(item) > 0;
    }

    @Override
    public void update() {
        if(!world.isRemote){
            if(Minecraft.getMinecraft().world.getRedstonePower(pos,null) < 15){
                if(progress == 0 && inputStack.getStackInSlot(0) != ItemStack.EMPTY && isItemBurnable(inputStack.getStackInSlot(0))){
                    progress += getVanillaBurnTime(inputStack.getStackInSlot(0));
                    currentItemBurnTime = progress;
                    inputStack.extractItem(0,1,false);
                }
                generatePower();
            }else{
                STATE = MachineState.OFF;
            }
        sendOutEnergy();
        }
    }

    private void generatePower() {
        if(progress > 0){
            energyStorage.generatePower(BasicGeneratorConfig.redGeneratorRfPerTick);
            progress--;
            if(STATE != MachineState.WORKING){
                STATE = MachineState.WORKING;
                this.markDirty();
            }
        }else{
            if(STATE != MachineState.OFF){
                STATE = MachineState.OFF;
                this.markDirty();
            }
        }
    }

    public int getMaxEnergy() {
        return energyStorage.getMaxEnergyStored();
    }

    private void sendOutEnergy() {
        int energyStored = energyStorage.getEnergyStored();
        for (EnumFacing face : EnumFacing.VALUES) {
            BlockPos pos = getPos().offset(face);
            TileEntity te = getWorld().getTileEntity(pos);
            EnumFacing opposite = face.getOpposite();
            if(te != null && te.hasCapability(CapabilityEnergy.ENERGY, opposite)){
                IEnergyStorage capability = te.getCapability(CapabilityEnergy.ENERGY, opposite);
                if(capability.canReceive()){
                    int canSend = energyStored >= BasicGeneratorConfig.redGeneratorMaxOutput ? BasicGeneratorConfig.redGeneratorMaxOutput : energyStored;
                    int drained = capability.receiveEnergy(canSend,false);
                    this.energyStorage.consumePower(drained);
                }
            }
        }
    }

    @Override
    public ArrayList<String> getDebug() {

        ArrayList<String> out = new ArrayList<String>();
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
        return out;
    }

    //endregion
}
