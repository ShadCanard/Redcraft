package com.shadcanard.redcraft.common.items;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item {

    private final ResourceLocation resourceLocation;
    private final String itemName;

    ItemBase(String name){
        this.itemName = name;
        resourceLocation = new ResourceLocation(References.MOD_ID, name);
        setName();
        setCreativeTab(RedCraft.redcraftResourceCreativeTab);
    }

    private void setName(){
        setUnlocalizedName(References.MOD_ID + "." + itemName);
        setRegistryName(resourceLocation);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
