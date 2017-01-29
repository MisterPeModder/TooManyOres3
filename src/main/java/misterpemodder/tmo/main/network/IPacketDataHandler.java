package misterpemodder.tmo.main.network;

import misterpemodder.tmo.main.network.packet.AbstractPacket;
import net.minecraft.nbt.NBTTagCompound;

public interface IPacketDataHandler {

	public void procData(NBTTagCompound data);
	
}
