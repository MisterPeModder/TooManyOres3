package misterpemodder.tmo.main.network.packet;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import misterpemodder.tmo.main.network.IPacketDataHandler;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

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

			if(c.hasKey("data") && handlerID < PacketDataHandlers.values().length && handlerID >= 0) {
				this.dataHandler = PacketDataHandlers.values()[handlerID];
				this.data = c.getCompoundTag("data");
			}
			
		} catch (Exception e) {
			TMORefs.LOGGER.error("Something went wrong when trying to read packet!");
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer buffer = new PacketBuffer(buf);
		
		buffer.writeInt(Arrays.asList(PacketDataHandlers.values()).indexOf(dataHandler));
		
		NBTTagCompound c = new NBTTagCompound();
		c.setTag("data", data);
		buffer.writeCompoundTag(c);
	}

}
