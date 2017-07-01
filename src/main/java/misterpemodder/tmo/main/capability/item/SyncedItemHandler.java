package misterpemodder.tmo.main.capability.item;

import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraftforge.items.ItemStackHandler;

public class SyncedItemHandler extends ItemStackHandler {
	
	protected TileEntityContainerBase te;
	
	public SyncedItemHandler(TileEntityContainerBase te, int size) {
		super(size);
		this.te = te;
	}
	
	@Override
	protected void onContentsChanged(int slot) {
		super.onContentsChanged(slot);
		te.sync();
		te.markDirty();
	}
}