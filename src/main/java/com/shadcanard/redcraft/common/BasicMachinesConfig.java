package com.shadcanard.redcraft.common;

import com.shadcanard.redcraft.common.helpers.References;
import net.minecraftforge.common.config.Config;

@Config(modid = References.MOD_ID, category = "Basic Machines", name = "Basic Machines")
public class BasicMachinesConfig {

    @Config.Comment(value = "Maximum amount a basic machine can handle")
    public static int basicMachineMaxPower = 100000;

    @Config.Comment(value = "How much RF per tick a basic machine will consume")
    public static int basicMachineRfPerTick = 20;

    @Config.Comment("How much energy a basic machine can receive per tick")
    public static int basicMachineMaxReceive = 1500;

    @Config.Comment("How many ticks a basic machine needs to run completely")
    public static int basicMachineMaxProgress = 40;
}

