package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventoryTMO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerTitaniumAnvil extends GuiContainerBaseTMO<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>{

	public GuiContainerTitaniumAnvil(InventoryPlayer playerInv, TileEntityTitaniumAnvil te) {
		super(new ContainerTitaniumAnvil(te, playerInv));
	}

	@Override
	public List<TabBase<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>> registerTabs() {
		List<TabBase<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>> list = new ArrayList<>();
		list.add(new TabMainTitaniumAnvil());
		list.add(new TabInfo<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>(false));
		list.add(new TabPlayerInventory<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>());
		list.add(new TabArmorInventoryTMO<ContainerTitaniumAnvil, TileEntityTitaniumAnvil>());
		return list;
	}

}
