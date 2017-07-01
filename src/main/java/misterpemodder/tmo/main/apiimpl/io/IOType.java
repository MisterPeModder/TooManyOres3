package misterpemodder.tmo.main.apiimpl.io;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.capability.io.IIOType;

public abstract class IOType<T> implements IIOType<T> {
	
	public String getUnlocalizedName() {
		return "tmo.ioType."+getID();
	}
	
	public static void registerDefaultIOTypes() {
		TooManyOresAPI.fluidIoType = new IOTypeFluid();
		TooManyOresAPI.energyIoType = new IOTypeEnergy();
			
		TooManyOresAPI.itemIoType = TooManyOresAPI.registryHandler.registerIOType(new IOTypeItem());
		TooManyOresAPI.fluidIoType = TooManyOresAPI.registryHandler.registerIOType(new IOTypeFluid());
		TooManyOresAPI.energyIoType = TooManyOresAPI.registryHandler.registerIOType(new IOTypeEnergy());
	}
	
}
