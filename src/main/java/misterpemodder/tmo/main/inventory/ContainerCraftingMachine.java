package misterpemodder.tmo.main.inventory;

import java.util.List;

import misterpemodder.hc.main.inventory.elements.ISyncedContainerElement;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.tileentity.TileEntityCraftingMachine;
import net.minecraft.entity.player.InventoryPlayer;

public abstract class ContainerCraftingMachine<TE extends TileEntityCraftingMachine<?>> extends ContainerBaseTMO<TE> {

	public int progress;
	
	public ContainerElementArrow arrow;
	
	public ContainerCraftingMachine(TE te, InventoryPlayer playerInv, int bPartOffset) {
		super(te, playerInv, bPartOffset);
	}
	
	@Override
	protected List<ISyncedContainerElement> addContainerElements(List<ISyncedContainerElement> elements) {
		this.arrow = new ContainerElementArrow(te);
		elements.add(this.arrow);
		return super.addContainerElements(elements);
	}
	
}
