package misterpemodder.tmo.main.network.packet;

import java.io.IOException;
import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import misterpemodder.tmo.main.network.IPacketDataHandler;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.utils.TMOHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketClientToServer extends AbstractPacket {
	
	public PacketClientToServer(){}
	
	public PacketClientToServer(IPacketDataHandler dataHandler, NBTTagCompound data) {
		super(dataHandler, data);
	}
	
	public static class PacketClientToServerHandler implements IMessageHandler<PacketClientToServer, IMessage> {
		
		public PacketClientToServerHandler(){}

		@Override
		public IMessage onMessage(PacketClientToServer message, MessageContext ctx) {
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
				public void run() {
					if(message.data != null || message.dataHandler != null) {
						message.dataHandler.procData(message.data);
					}
				}
			});
			return null;
		}
		
	}

}
