package misterpemodder.tmo.main.capability;

import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraftforge.items.ItemStackHandler;

public class ComparatorSyncedItemHandler extends ItemStackHandler {
	
	private TileEntityContainerBase te;
	
	public ComparatorSyncedItemHandler(TileEntityContainerBase te, int size) {
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