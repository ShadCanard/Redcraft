package com.shadcanard.redcraft.common.blocks;

import com.shadcanard.redcraft.common.helpers.References;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

public class BlockBase extends Block {

    private final String blockName;
    public ResourceLocation resourceLocation;

    public BlockBase(Material materialIn, String blockName) {
        super(materialIn);
        this.blockName = blockName;
        setName();
    }

    protected BlockBase(String blockName) {
        super(Material.IRON);
        this.blockName = blockName;
        resourceLocation = new ResourceLocation(References.MOD_ID, blockName);
        setName();
        setHarvestLevel("pickaxe",1);
    }

    private void setName(){
        setRegistryName(resourceLocation);
        setUnlocalizedName(References.MOD_ID + "." + blockName);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "inventory"));
    }
}
