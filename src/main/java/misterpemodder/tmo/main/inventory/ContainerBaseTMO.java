package misterpemodder.tmo.main.inventory;

import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.entity.player.InventoryPlayer;

public abstract class ContainerBaseTMO<TE extends TileEntityContainerBase> extends ContainerBase<TE> {

	public ContainerBaseTMO(TE te, InventoryPlayer playerInv, int bPartOffset) {
		super(te, playerInv, bPartOffset);
	}
	
	public ContainerBaseTMO(TE te, InventoryPlayer playerInv, int bPartOffset, boolean hasArmorTab) {
		super(te, playerInv, bPartOffset, hasArmorTab);
	}
	
	@Override
	protected boolean shouldEnableBaublesCompat() {
		return TMORefs.baublesEnabled;
	}
	
}
