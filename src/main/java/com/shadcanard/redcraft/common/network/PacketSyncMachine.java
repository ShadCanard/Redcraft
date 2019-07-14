package com.shadcanard.redcraft.common.network;

import com.shadcanard.redcraft.common.RedCraft;
import com.shadcanard.redcraft.common.tools.IMachineContainer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSyncMachine implements IMessage {

    private int energy;
    private int progress;

    @Override
    public void fromBytes(ByteBuf buf) {
        energy = buf.readInt();
        progress = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(energy);
        buf.writeByte(progress);
    }

    public PacketSyncMachine(){

    }

    public PacketSyncMachine(int energy, int progress){
        this.energy = energy;
        this.progress = progress;
    }

    public static class Handler implements IMessageHandler<PacketSyncMachine,IMessage>{

        @Override
        public IMessage onMessage(PacketSyncMachine message, MessageContext ctx) {
            RedCraft.proxy.addScheduledTaskClient(() -> handle(message,ctx));
            return null;
        }

        private void handle(PacketSyncMachine message, MessageContext ctx){
            EntityPlayer player = RedCraft.proxy.getClientPlayer();
            if(player.openContainer instanceof IMachineContainer){
                ((IMachineContainer) player.openContainer).sync(message.energy, message.progress);
            }
        }
    }
}
