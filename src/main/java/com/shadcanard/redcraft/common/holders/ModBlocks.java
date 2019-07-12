package com.shadcanard.redcraft.common.holders;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.basic.BlockRedIngot;
import com.shadcanard.redcraft.common.blocks.furnace.BlockRedFurnace;
import com.shadcanard.redcraft.common.blocks.generator.BlockCreativeGenerator;
import com.shadcanard.redcraft.common.blocks.generator.BlockGenerator;
import com.shadcanard.redcraft.common.blocks.solarfurnace.BlockSolarFurnace;
import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {

    //region Holders
    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Blocks.BLOCK_RED_INGOT)
    public static BlockRedIngot blockRedIngot;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Blocks.BLOCK_RED_GENERATOR)
    public static BlockGenerator blockGenerator;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Blocks.BLOCK_RED_FURNACE)
    public static BlockRedFurnace blockRedFurnace;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Blocks.BLOCK_SOLAR_FURNACE)
    public static BlockSolarFurnace blockSolarFurnace;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Blocks.BLOCK_RED_GENERATOR + "_creative")
    public static BlockCreativeGenerator blockCreativeGenerator;

    //endregion

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        RedCraft.logger.info("Registering Block Models");
        blockRedIngot.initModel();
        blockGenerator.initModel();
        blockRedFurnace.initModel();
        blockSolarFurnace.initModel();
        blockCreativeGenerator.initModel();
    }
}
