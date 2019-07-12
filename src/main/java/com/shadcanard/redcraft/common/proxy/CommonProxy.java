package com.shadcanard.redcraft.common.proxy;

import com.google.common.util.concurrent.ListenableFuture;
import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.blocks.basic.BlockRedIngot;
import com.shadcanard.redcraft.common.blocks.furnace.BlockRedFurnace;
import com.shadcanard.redcraft.common.blocks.furnace.TileRedFurnace;
import com.shadcanard.redcraft.common.blocks.generator.BlockCreativeGenerator;
import com.shadcanard.redcraft.common.blocks.generator.BlockGenerator;
import com.shadcanard.redcraft.common.blocks.solarfurnace.BlockSolarFurnace;
import com.shadcanard.redcraft.common.blocks.solarfurnace.TileSolarFurnace;
import com.shadcanard.redcraft.common.gui.GuiHandler;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.helpers.SmeltingHelper;
import com.shadcanard.redcraft.common.holders.ModBlocks;
import com.shadcanard.redcraft.common.items.*;
import com.shadcanard.redcraft.common.network.Messages;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    //region Initialization

    public void preInit(FMLPreInitializationEvent e){
        RedCraft.logger.info(References.MOD_NAME + " Pre-Init");
        Messages.registerMessages(References.MOD_ID);

    }

    public void init(FMLInitializationEvent e){
        RedCraft.logger.info(References.MOD_NAME + " Init");
        SmeltingHelper.addSmelting();
        NetworkRegistry.INSTANCE.registerGuiHandler(RedCraft.instance, new GuiHandler());
    }

    public void postInit(FMLPostInitializationEvent e){
        RedCraft.logger.info(References.MOD_NAME + " Post-Init");

        RedCraft.logger.info(References.MOD_NAME + " is fully initialized !");

    }
    //endregion

    //region Registering

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event){
        RedCraft.logger.info("Registering Blocks");
        //region Basic Blocks
        event.getRegistry().register(new BlockRedIngot());
        event.getRegistry().register(new BlockGenerator());
        event.getRegistry().register(new BlockRedFurnace());
        event.getRegistry().register(new BlockSolarFurnace());
        event.getRegistry().register(new BlockCreativeGenerator());
        //endregion

        //region Tile Entities
        registerTileEntities();
        //endregion
    }

    private static void registerTileEntities() {
        RedCraft.logger.info("Registering Tile Entities");
        GameRegistry.registerTileEntity(TileRedFurnace.class, TileRedFurnace.resourceLocation);
        GameRegistry.registerTileEntity(TileSolarFurnace.class, TileSolarFurnace.resourceLocation);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event){
        //region Item Blocks
        registerItemBlocks(event);
        //endregion

        RedCraft.logger.info("Registering Items");
        //region Items
        event.getRegistry().register(new ItemRedIngot());
        event.getRegistry().register(new ItemRedDust());
        event.getRegistry().register(new ItemGoldDust());
        event.getRegistry().register(new ItemIronBowl());
        event.getRegistry().register(new ItemScreen());
        event.getRegistry().register(new ItemCraftmojiTablet());
        //endregion
    }

    private static void registerItemBlocks(RegistryEvent.Register<Item> event){
        RedCraft.logger.info("Registering Item Blocks");
        event.getRegistry().register(new ItemBlock(ModBlocks.blockRedIngot).setRegistryName(ModBlocks.blockRedIngot.resourceLocation));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockGenerator).setRegistryName(ModBlocks.blockGenerator.resourceLocation));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockRedFurnace).setRegistryName(ModBlocks.blockRedFurnace.resourceLocation));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockSolarFurnace).setRegistryName(ModBlocks.blockSolarFurnace.resourceLocation));
        event.getRegistry().register(new ItemBlock(ModBlocks.blockCreativeGenerator){
            //effet de brillance
            @Override
            public boolean hasEffect(ItemStack stack) {
                return true;
            }
        }.setRegistryName(ModBlocks.blockCreativeGenerator.resourceLocation));
    }

    //endregion

    public ListenableFuture<Object> addScheduledTaskClient(Runnable runnableToSchedule){
        throw new IllegalStateException("This should only be called from client side !");
    }

    public EntityPlayer getClientPlayer(){
        throw new IllegalStateException("This should only be called from client side !");
    }
}
