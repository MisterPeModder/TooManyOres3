package misterpemodder.tmo.main.capability;

import net.minecraft.util.EnumFacing;

public interface IMachineElementHandler<E> {
	
	E getSideHandler(EnumFacing side);

}
