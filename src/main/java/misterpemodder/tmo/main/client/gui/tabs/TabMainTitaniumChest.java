package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.hc.main.client.gui.tabs.TabMain;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;

public class TabMainTitaniumChest extends TabMain<ContainerTitaniumChest, TileEntityTitaniumChest> {
	
	public static final String ID = "tmo.main.tChest";
	
	public TabMainTitaniumChest() {
		super();
	}
	
	@Override
	public String getTabID() {
		return ID;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/titanium_chest/main.png"), new Dimension(212, 132), new Dimension(256, 256));
	}
	
}
