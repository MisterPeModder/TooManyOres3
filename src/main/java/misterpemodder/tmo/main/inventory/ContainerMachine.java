package misterpemodder.tmo.main.inventory;

import java.util.List;

import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.entity.player.InventoryPlayer;

public abstract class ContainerMachine<TE extends TileEntityMachine<?>> extends ContainerBase<TE> {

	public int progress;
	
	public ContainerElementArrow arrow;
	
	public ContainerMachine(TE te, InventoryPlayer playerInv, int bPartOffset) {
		super(te, playerInv, bPartOffset);
	}
	
	@Override
	protected List<ISyncedContainerElement> addContainerElements(List<ISyncedContainerElement> elements) {
		this.arrow = new ContainerElementArrow(te);
		elements.add(this.arrow);
		return super.addContainerElements(elements);
	}
	
}
