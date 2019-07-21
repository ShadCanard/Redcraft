package com.shadcanard.redcraft.common.helpers;

public class References {

    //region Mod References
    public static final String MOD_ID = "redcraft";
    public static final String MOD_NAME = "RedCraft";
    public static final String MOD_VERSION = "1.0.0-a1";
    //endregion

    //region Packages
    private static final String MOD_PACKAGE_ROOT = "com.shadcanard.redcraft";
    private static final String MOD_PACKAGE_CLIENT = MOD_PACKAGE_ROOT + ".client";
    private static final String MOD_PACKAGE_SERVER = MOD_PACKAGE_ROOT + ".server";
    private static final String MOD_PACKAGE_COMMON = MOD_PACKAGE_ROOT + ".common";
    //endregion

    //region Proxies
    public static final String PROXY_COMMON = MOD_PACKAGE_COMMON + ".proxy.CommonProxy";
    public static final String PROXY_CLIENT = MOD_PACKAGE_CLIENT + ".proxy.ClientProxy";
    public static final String PROXY_SERVER = MOD_PACKAGE_SERVER + ".proxy.ServerProxy";
    //endregion

    //region GUIs
    public static final int GUI_GENERATOR = 0;
    public static final int GUI_RED_FURNACE = 1;
    public static final int GUI_SOLAR_FURNACE = 2;
    public static final int GUI_CRAFTMOJI_TABLET = 3;
    public static final int GUI_DEBUG_TOOL = 4;


    //region Variables
    public static final int SLOT_SIZE = 18;
    //endregion
}
