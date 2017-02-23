package misterpemodder.tmo.api.block;

import net.minecraftforge.items.IItemHandler;

public interface ILockable {
	
	/**
	 * Returns the {@code IItemHandler} that can contain an {@code IItemLock} item.
	 * Its recommended that this {@code IItemHandler} has a size 1 of and accepts
	 * only items that implements {@code IItemLock}.
	 * 
     * @return The IItemHandler.
     */
	IItemHandler getLockItemHandler();
	
	/**
	 * Used to if this container is locked.
	 * 
	 * @return true if it is locked, false if not.
	 */
	boolean isLocked();
	
	/**
	 * @param locked
	 * 			  - to lock or not to lock?
	 */
	void setLocked(boolean locked);
	
}
