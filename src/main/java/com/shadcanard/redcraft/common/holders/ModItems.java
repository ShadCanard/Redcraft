package com.shadcanard.redcraft.common.holders;

import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.items.*;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {

    //region Holders
    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.RED_INGOT)
    public static ItemRedIngot redIngot;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.RED_DUST)
    public static ItemRedDust redDust;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.GOLD_DUST)
    public static ItemGoldDust goldDust;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.IRON_BOWL)
    public static ItemIronBowl ironBowl;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.SCREEN)
    public static ItemScreen screen;

    @GameRegistry.ObjectHolder(References.MOD_ID + ":" + Names.Items.CRAFTMOJI_TABLET)
    public static ItemCraftmojiTablet craftmojiTablet;
    //endregion

    @SideOnly(Side.CLIENT)
    public static void initModels(){
        redIngot.initModel();
        redDust.initModel();
        goldDust.initModel();
        ironBowl.initModel();
        screen.initModel();
        craftmojiTablet.initModel();
    }
}
