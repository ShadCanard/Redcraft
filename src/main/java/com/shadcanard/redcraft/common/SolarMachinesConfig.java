package com.shadcanard.redcraft.common;

import com.shadcanard.redcraft.common.helpers.References;
import net.minecraftforge.common.config.Config;

@Config(modid = References.MOD_ID, category = "Solar Machines", name = "Solar Machines")
public class SolarMachinesConfig {

    @Config.Comment("How many ticks a solar machine needs to run completely")
    public static int solarMachineMaxProgress = 400;

    @Config.Comment("Does the solar machines have an internal storage ?")
    public static boolean doesSolarHaveRfStorage = true;
}
