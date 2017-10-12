package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventoryTMO;
import misterpemodder.tmo.main.client.gui.tabs.TabIO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainInjector;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerInjector extends GuiContainerBaseTMO<ContainerInjector, TileEntityInjector> {
	
	public GuiContainerInjector(InventoryPlayer playerInv, TileEntityInjector te) {
		super(new ContainerInjector(te, playerInv));
	}

	@Override
	public List<TabBase<ContainerInjector, TileEntityInjector>> registerTabs() {
		List<TabBase<ContainerInjector, TileEntityInjector>> list = new ArrayList<>();
		list.add(new TabMainInjector());
		list.add(new TabInfo<ContainerInjector, TileEntityInjector>(false));
		list.add(new TabPlayerInventory<ContainerInjector, TileEntityInjector>());
		list.add(new TabArmorInventoryTMO<ContainerInjector, TileEntityInjector>());
		list.add(new TabIO<>(container.getTileEntity().getIoConfigHandler()));
		return list;
	}
	
}
