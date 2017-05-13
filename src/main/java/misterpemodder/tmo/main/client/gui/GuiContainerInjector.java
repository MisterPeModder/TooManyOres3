package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainInjector;
import misterpemodder.tmo.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerInjector extends GuiContainerBase<ContainerInjector, TileEntityInjector> {
	
	public GuiContainerInjector(InventoryPlayer playerInv, TileEntityInjector te) {
		super(new ContainerInjector(te, playerInv));
	}

	@Override
	public List<TabBase> registerTabs() {
		List<TabBase> list = new ArrayList<>();
		list.add(new TabMainInjector());
		list.add(new TabInfo<ContainerInjector, TileEntityInjector>(false));
		list.add(new TabPlayerInventory<ContainerInjector, TileEntityInjector>());
		list.add(new TabArmorInventory<ContainerInjector, TileEntityInjector>());
		return list;
	}
	
}
