package com.shadcanard.redcraft.common.blocks.furnace;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.machine.BlockMachineBase;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRedFurnace extends BlockMachineBase {
    public BlockRedFurnace() {
        super(Names.Blocks.BLOCK_RED_FURNACE);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileRedFurnace();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote){
            TileEntity te = worldIn.getTileEntity(pos);
            if(!(te instanceof TileRedFurnace)){
                return false;
            }
            playerIn.openGui(RedCraft.instance, References.GUI_RED_FURNACE, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }
}
