package com.shadcanard.redcraft.common;


import com.shadcanard.redcraft.common.helpers.References;
import net.minecraftforge.common.config.Config;

@Config(modid = References.MOD_ID, category = "Generator Machines",name = "Generator Machines")
public class BasicGeneratorConfig {

    @Config.Comment("How many RF a red Generator generates")
    public static int redGeneratorRfPerTick = 20;
    public static int redGeneratorMaxOutput = 1500;
}

