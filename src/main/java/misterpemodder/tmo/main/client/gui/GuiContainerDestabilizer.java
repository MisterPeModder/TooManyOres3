package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.client.gui.tabs.TabIO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainDestabilizer;
import misterpemodder.tmo.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerDestabilizer extends GuiContainerBase<ContainerDestabilizer, TileEntityDestabilizer>{

	public GuiContainerDestabilizer(InventoryPlayer playerInv, TileEntityDestabilizer te) {
		super(new ContainerDestabilizer(te, playerInv));
	}

	@Override
	public List<TabBase> registerTabs() {
		List<TabBase> list = new ArrayList<>();
		list.add(new TabMainDestabilizer());
		list.add(new TabInfo<ContainerDestabilizer, TileEntityDestabilizer>(false));
		list.add(new TabPlayerInventory<ContainerDestabilizer, TileEntityDestabilizer>());
		list.add(new TabArmorInventory<ContainerDestabilizer, TileEntityDestabilizer>());
		list.add(new TabIO<>());
		return list;
	}
	
}
