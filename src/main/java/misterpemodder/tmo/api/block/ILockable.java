package misterpemodder.tmo.api.block;

import net.minecraftforge.items.IItemHandler;

public interface ILockable {
	
	public IItemHandler getLockItemHandler();
	public boolean isLocked();
	
}
