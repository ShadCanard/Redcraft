package com.shadcanard.redcraft.common;

import com.shadcanard.redcraft.common.helpers.Names;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.holders.ModBlocks;
import com.shadcanard.redcraft.common.holders.ModItems;
import com.shadcanard.redcraft.common.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = References.MOD_ID, name = References.MOD_NAME, version =  References.MOD_VERSION, dependencies = "required-after:forge@[14.23.5.2768,)", useMetadata = true)
public class RedCraft {

    //region Variables
    @SidedProxy(clientSide = References.PROXY_CLIENT, serverSide = References.PROXY_SERVER, modId = References.MOD_ID)
    public static CommonProxy proxy;

    @Mod.Instance
    public static RedCraft instance;

    public static Logger logger;

    public static final CreativeTabs redcraftMachineCreativeTab = new CreativeTabs(Names.Misc.MACHINE_CREATIVE_TAB) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModBlocks.blockGenerator);
        }
    };

    public static final CreativeTabs redcraftResourceCreativeTab = new CreativeTabs(Names.Misc.RESOURCES_CREATIVE_TAB) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.redIngot);
        }
    };

    //endregion

    //region Initializations
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e){
        logger = e.getModLog();
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e){
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e){
        proxy.postInit(e);
    }
    //endregion
}
