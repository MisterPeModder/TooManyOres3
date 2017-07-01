package misterpemodder.tmo.api.capability.io;

import net.minecraftforge.common.capabilities.Capability;

public interface IIOType<T> {

	String getID();
	
	String getUnlocalizedName();
	
	Capability<T> getCapabilityInstance();
	
}
