package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventoryTMO;
import misterpemodder.tmo.main.client.gui.tabs.TabIO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerDestabilizer extends GuiContainerBaseTMO<ContainerDestabilizer, TileEntityDestabilizer> {

	public GuiContainerDestabilizer(InventoryPlayer playerInv, TileEntityDestabilizer te) {
		super(new ContainerDestabilizer(te, playerInv));
	}

	@Override
	public List<TabBase<ContainerDestabilizer, TileEntityDestabilizer>> registerTabs() {
		List<TabBase<ContainerDestabilizer, TileEntityDestabilizer>> list = new ArrayList<>();
		list.add(new TabMainDestabilizer());
		list.add(new TabInfo<ContainerDestabilizer, TileEntityDestabilizer>(false).setEnabled(false));
		list.add(new TabPlayerInventory<ContainerDestabilizer, TileEntityDestabilizer>().setEnabled(false));
		list.add(new TabArmorInventoryTMO<ContainerDestabilizer, TileEntityDestabilizer>().setEnabled(false));
		list.add(new TabIO<ContainerDestabilizer, TileEntityDestabilizer>(container.getTileEntity().getIoConfigHandler()).setEnabled(false));
		return list;
	}
	
}
