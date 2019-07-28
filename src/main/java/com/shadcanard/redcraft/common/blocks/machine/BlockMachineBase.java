package com.shadcanard.redcraft.common.blocks.machine;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.BlockBase;
import com.shadcanard.redcraft.common.blocks.furnace.poweredfurnace.TileRedFurnace;
import com.shadcanard.redcraft.common.holders.ModItems;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Default machine block (orientable, with Tile Entities support)
 */
public abstract class BlockMachineBase extends BlockBase implements ITileEntityProvider {

    /**
     * Contains the direction the block is facing horizontally (it cannot face up/downwards
     */
    protected static final PropertyDirection FACING = BlockHorizontal.FACING;

    /**
     * Contains the current state of the block (ON/OFF)
     */
    public static final PropertyEnum<MachineState> STATE = PropertyEnum.<MachineState>create("state", MachineState.class);

    protected BlockMachineBase(String blockName) {
        super(blockName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(RedCraft.redcraftMachineCreativeTab);
        setHardness(2.0F);
    }

    /**
     * Gets the current state for the placement (iow where is the block facing by default)
     * @param world World of the block
     * @param pos Position of the block
     * @param facing Where is the block attended to be facing
     * @param hitX X Hitbox of the block
     * @param hitY Y hitbox for the block
     * @param hitZ Z hitbox for the block
     * @param meta Metadata of the block
     * @param placer Who placed it
     * @param hand On which hand the block was placed ?
     * @return State for the placement
     */
    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nullable World world, @Nullable BlockPos pos, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * Returns the state of the block from the metadata
     * @param meta Metadata
     * @return State of the block
     */
    @Nonnull
    @Override
    @SuppressWarnings({"deprecation"})
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.byIndex(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * Returns the Metadata from a block state
     * @param state the state of the block
     * @return Metadata of the state
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    /**
     * Creating a state for the block
     * @return Container with the states
     */

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, STATE);
    }

    /**
     * Creates a Tile Entity on Block Creation (Must be overrided)
     * @param worldIn World where to spawn the Tile Entity
     * @param meta Metadata of the Block for the Tileentity creation
     * @return Created Tile Entity to place in the world
     */
    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return null;
    }

    /**
     * Returns the actual state of a block (Working or not)
     * @param state State attended
     * @param worldIn Access of the state of the block
     * @param pos Position of the Block
     * @return State of the block
     */
    @SuppressWarnings("deprecation")
    @Nonnull
    @Override
    public IBlockState getActualState(@Nullable IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity te = worldIn instanceof ChunkCache ? ((ChunkCache)worldIn).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : worldIn.getTileEntity(pos);
        if(te instanceof TileRedFurnace){
            return Objects.requireNonNull(state).withProperty(STATE, ((TileRedFurnace)te).getState());
        }
        return super.getActualState(Objects.requireNonNull(state), worldIn, pos);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TileEntity te = worldIn.getTileEntity(pos);
        if(te instanceof TileRedFurnace){
            for (int i = 0; i < TileRedFurnace.SLOT_SIZE; i++) {
                worldIn.spawnEntity (new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(),((TileRedFurnace) te).combinedStack.getStackInSlot(i)));
            }
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(worldIn,pos,state,playerIn,hand,facing,hitX,hitY,hitZ);
        if(!worldIn.isRemote) {
            if (playerIn.inventory.getCurrentItem().getItem() == ModItems.debugTool) {
                playerIn.sendMessage(new TextComponentString("Debug Infos sent on console. Please read your logs.").setStyle(new Style().setColor(TextFormatting.RED)));
                if (((TileMachineBase) worldIn.getTileEntity(pos)).getDebug() != null) {
                    TileEntity te = worldIn.getTileEntity(pos);
                    if(te instanceof TileMachineBase){
                        ArrayList<String> debug = ((TileMachineBase) te).getDebug();
                        for (String line :
                                debug) {
                            RedCraft.logger.info(line);
                        }
                    }
                }
            }
        }
        return true;
    }
}
