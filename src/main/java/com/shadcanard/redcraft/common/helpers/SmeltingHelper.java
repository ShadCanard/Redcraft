package com.shadcanard.redcraft.common.helpers;

import com.shadcanard.redcraft.common.holders.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SmeltingHelper {

    public static void addSmelting(){
        GameRegistry.addSmelting(ModItems.redDust, new ItemStack(ModItems.redIngot), 0.1f);
        GameRegistry.addSmelting(ModItems.goldDust, new ItemStack(Items.GOLD_INGOT), 0.1f);
    }
}
