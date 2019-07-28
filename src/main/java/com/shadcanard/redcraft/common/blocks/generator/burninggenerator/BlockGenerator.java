package com.shadcanard.redcraft.common.blocks.generator.burninggenerator;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.machine.BlockMachineBase;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockGenerator extends BlockMachineBase {
    public BlockGenerator() {
        super(Names.Blocks.BLOCK_RED_GENERATOR);
    }

    @Override
    public TileEntity createNewTileEntity(@Nullable World worldIn, int meta) {
        return new TileGenerator();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(worldIn,pos,state,playerIn,hand,facing,hitX,hitY,hitZ);
        if(!worldIn.isRemote){
            TileEntity te = worldIn.getTileEntity(pos);
            if(!(te instanceof TileGenerator)){
                return false;
            }
            playerIn.openGui(RedCraft.instance, References.GUI_GENERATOR, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);
    }
}