package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TabRedstone<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase<C, TE> {

	private final boolean isLarge;
	
	public TabRedstone(boolean isLarge) {
		super(TabPos.TOP_RIGHT);
		this.isLarge = isLarge;
	}
	
	@Override
	public TabID getTabID() {
		return TabID.REDSTONE;
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
		if(isLarge) {
			return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,84), new Point(32, 84), new ResourceLocationTmo("textures/gui/container/titanium_chest/redstone.png"), new Dimension(212, 132), new Dimension(256, 256));
		} else {
			return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,84), new Point(32, 84), new ResourceLocationTmo("textures/gui/container/redstone.png"), new Dimension(212, 100));
		}
	}

	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		return false;
	}

}
