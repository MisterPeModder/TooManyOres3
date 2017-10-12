package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventoryTMO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainTitaniumChest;
import misterpemodder.tmo.main.client.gui.tabs.TabRedstone;
import misterpemodder.tmo.main.client.gui.tabs.TabSecurity;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerTitaniumChest extends GuiContainerBaseTMO<ContainerTitaniumChest, TileEntityTitaniumChest>{

	public GuiContainerTitaniumChest(InventoryPlayer playerInv, TileEntityTitaniumChest te) {
		super(new ContainerTitaniumChest(te, playerInv));
	}

	@Override
	public List<TabBase<ContainerTitaniumChest, TileEntityTitaniumChest>> registerTabs() {
		List<TabBase<ContainerTitaniumChest, TileEntityTitaniumChest>> list = new ArrayList<>();
		list.add(new TabMainTitaniumChest());
		list.add(new TabInfo<ContainerTitaniumChest, TileEntityTitaniumChest>(true));
		list.add(new TabRedstone<ContainerTitaniumChest, TileEntityTitaniumChest>(true));
		
		list.add(new TabSecurity<ContainerTitaniumChest, TileEntityTitaniumChest>(true));
		
		list.add(new TabPlayerInventory<ContainerTitaniumChest, TileEntityTitaniumChest>());
		
		list.add(new TabArmorInventoryTMO<ContainerTitaniumChest, TileEntityTitaniumChest>());
		return list;
	}

}
