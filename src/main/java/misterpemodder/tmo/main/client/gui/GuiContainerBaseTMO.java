package misterpemodder.tmo.main.client.gui;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;

public abstract class GuiContainerBaseTMO<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends GuiContainerBase<C, TE> {

	public GuiContainerBaseTMO(C container) {
		super(container);
	}
	
}
