package misterpemodder.tmo.main.network;

import net.minecraft.nbt.NBTTagCompound;

public interface IPacketDataHandler {

	public void procData(NBTTagCompound data);
	
}
