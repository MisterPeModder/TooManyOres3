package misterpemodder.tmo.main.client.gui.tabs;

import misterpemodder.hc.main.client.gui.tabs.TabArmorInventory;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import net.minecraft.item.ItemStack;

public class TabArmorInventoryTMO<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabArmorInventory<C, TE> {
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(TheItems.TITANIUM_CHESTPLATE.getItem());
	}
	
}
