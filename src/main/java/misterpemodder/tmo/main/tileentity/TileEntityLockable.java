package misterpemodder.tmo.main.tileentity;

import net.minecraftforge.items.IItemHandler;

public interface TileEntityLockable {
	
	public IItemHandler getLockItemHandler();
	public boolean isLocked();
	
}
