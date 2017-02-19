package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TabRedstone<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {

	public TabRedstone() {
		super(TabPos.TOP_RIGHT);
	}
	
	@Override
	public String getUnlocalizedName() {
		return "gui.tab.redstone.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Items.REDSTONE);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,84), new Point(32, 84), new ResourceLocationTmo("textures/gui/container/redstone.png"), new Dimension(212, 132));
	}

	@Override
	public boolean shouldDisplaySlot(SlotHidable slot) {
		return false;
	}

}
