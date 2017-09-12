package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.client.gui.tabs.TabArmorInventoryTMO;
import misterpemodder.tmo.main.client.gui.tabs.TabIO;
import misterpemodder.tmo.main.client.gui.tabs.TabInfo;
import misterpemodder.tmo.main.client.gui.tabs.TabMainThermoelectricGenerator;
import misterpemodder.tmo.main.inventory.ContainerThermoelectricGenerator;
import misterpemodder.tmo.main.tileentity.TileEntityThemoelectricGenerator;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiContainerThermoelectricGenerator extends GuiContainerBase<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator> {

	public GuiContainerThermoelectricGenerator(InventoryPlayer playerInv, TileEntityThemoelectricGenerator te) {
		super(new ContainerThermoelectricGenerator(te, playerInv));
	}

	@Override
	public List<TabBase<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator>> registerTabs() {
		List<TabBase<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator>> list = new ArrayList<>();
		list.add(new TabMainThermoelectricGenerator());
		list.add(new TabInfo<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator>(false));
		list.add(new TabPlayerInventory<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator>());
		list.add(new TabArmorInventoryTMO<ContainerThermoelectricGenerator, TileEntityThemoelectricGenerator>());
		list.add(new TabIO<>(container.getTileEntity().getIoConfigHandler()));
		return list;
	}

}
