package com.shadcanard.redcraft.common.blocks.machine;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.BlockBase;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockMachineBase extends BlockBase implements ITileEntityProvider {

    private static final PropertyDirection FACING = BlockHorizontal.FACING;

    protected BlockMachineBase(String blockName) {
        super(blockName);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        setCreativeTab(RedCraft.redcraftMachineCreativeTab);
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nullable World world, @Nullable BlockPos pos, @Nullable EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Nonnull
    @Override
    @SuppressWarnings({"deprecation"})
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y)
        {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return null;
    }
}
