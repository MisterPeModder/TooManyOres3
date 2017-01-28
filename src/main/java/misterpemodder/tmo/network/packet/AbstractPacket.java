package misterpemodder.tmo.network.packet;

import io.netty.buffer.ByteBuf;
import misterpemodder.tmo.network.IPacketDataHandler;
import misterpemodder.tmo.network.PacketDataHandlers;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class AbstractPacket implements IMessage {

	protected IPacketDataHandler dataHandler;
	protected NBTTagCompound data;
	
	public AbstractPacket(){}
	
	public AbstractPacket(IPacketDataHandler dataHandler, NBTTagCompound data) {
		this.dataHandler = dataHandler;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		try {
			
			int handlerID = buffer.readInt();
			NBTTagCompound c = buffer.readCompoundTag();

			if(c.hasKey("data") && handlerID < PacketDataHandlers.HANDLERS.size() && handlerID >= 0) {
				this.dataHandler = PacketDataHandlers.HANDLERS.get(handlerID);
				this.data = c.getCompoundTag("data");
			}
			
		} catch (Exception e) {
			TMOHelper.LOGGER.error("Something went wrong when trying to read packet!");
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		int h = PacketDataHandlers.HANDLERS.indexOf(dataHandler);
		
		buffer.writeInt(PacketDataHandlers.HANDLERS.indexOf(dataHandler));
		
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("data", data);
		buffer.writeCompoundTag(c);
	}

}
