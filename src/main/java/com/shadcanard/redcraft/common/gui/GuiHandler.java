package com.shadcanard.redcraft.common.gui;

import com.shadcanard.redcraft.client.gui.GuiDebugTool;
import com.shadcanard.redcraft.client.gui.GuiGenerator;
import com.shadcanard.redcraft.client.gui.GuiRedFurnace;
import com.shadcanard.redcraft.client.gui.GuiSolarFurnace;
import com.shadcanard.redcraft.common.blocks.furnace.ContainerRedFurnace;
import com.shadcanard.redcraft.common.blocks.furnace.TileRedFurnace;
import com.shadcanard.redcraft.common.blocks.generator.ContainerGenerator;
import com.shadcanard.redcraft.common.blocks.generator.TileGenerator;
import com.shadcanard.redcraft.common.blocks.solarfurnace.ContainerSolarFurnace;
import com.shadcanard.redcraft.common.blocks.solarfurnace.TileSolarFurnace;
import com.shadcanard.redcraft.common.helpers.References;
import com.shadcanard.redcraft.common.items.ContainerDebug;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.awt.*;

public class GuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRedFurnace) {
            return new ContainerRedFurnace(player.inventory, (TileRedFurnace) te);
        }
        if(te instanceof TileSolarFurnace){
            return new ContainerSolarFurnace(player.inventory, (TileSolarFurnace) te);
        }
        if(te instanceof TileGenerator){
            return new ContainerGenerator(player.inventory, (TileGenerator)te);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileRedFurnace) {
            TileRedFurnace containerTileEntity = (TileRedFurnace) te;
            return new GuiRedFurnace(containerTileEntity, new ContainerRedFurnace(player.inventory, containerTileEntity), player.inventory);
        }
        if (te instanceof TileSolarFurnace) {
            TileSolarFurnace containerTileEntity = (TileSolarFurnace) te;
            return new GuiSolarFurnace(containerTileEntity, new ContainerSolarFurnace(player.inventory, containerTileEntity), player.inventory);
        }
        if (te instanceof TileGenerator) {
            TileGenerator containerTileEntity = (TileGenerator) te;
            return new GuiGenerator(containerTileEntity, new ContainerGenerator(player.inventory, containerTileEntity), player.inventory);
        }
        /** Item GUIs (By ID) */
        if(ID == References.GUI_DEBUG_TOOL){
            return new GuiDebugTool(new ContainerDebug(player),player.inventory);
        }
        return null;
    }
}
