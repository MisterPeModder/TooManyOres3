package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.tmo.main.client.gui.ContainerTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;

public class TabMainTitaniumChest extends TabMain<ContainerTitaniumChest, TileEntityTitaniumChest> {
	
	public TabMainTitaniumChest() {
		super();
	}
	
	@Override
	public TabID getTabID() {
		return TabID.MAIN_TC;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/titanium_chest/main.png"), new Dimension(212, 132));
	}
	
}
