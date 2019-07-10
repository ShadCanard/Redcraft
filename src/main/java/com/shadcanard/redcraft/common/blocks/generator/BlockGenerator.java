package com.shadcanard.redcraft.common.blocks.generator;

import com.shadcanard.redcraft.common.blocks.machine.BlockMachineBase;
import com.shadcanard.redcraft.common.helpers.Names;
import net.minecraft.block.ITileEntityProvider;

public class BlockGenerator extends BlockMachineBase implements ITileEntityProvider {

    public BlockGenerator() {
        super(Names.Blocks.BLOCK_RED_GENERATOR);
    }
}
