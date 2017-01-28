package misterpemodder.tmo.network;

import misterpemodder.tmo.network.packet.AbstractPacket;
import net.minecraft.nbt.NBTTagCompound;

public interface IPacketDataHandler {

	public void procData(NBTTagCompound data);
	
}
