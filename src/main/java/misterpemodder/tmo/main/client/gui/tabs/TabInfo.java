package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TabInfo<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {
	
	private final boolean isLarge;

	public TabInfo(boolean isLarge) {
		super(TabPos.TOP_RIGHT);
		this.isLarge = isLarge;
	}
	
	@Override
	public TabID getTabID() {
		return TabID.INFO;
	}
	
	@Override
	public String getUnlocalizedName() {
		return "gui.tab.info.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Items.WRITTEN_BOOK);
	}

	@Override
	public TabTexture getTabTexture() {
		if(isLarge) {
			return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,28), new Point(32, 28), new ResourceLocationTmo("textures/gui/container/titanium_chest/info.png"), new Dimension(212, 132), new Dimension(256, 256));
		} else {
			return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,28), new Point(32, 28), new ResourceLocationTmo("textures/gui/container/info.png"), new Dimension(212, 100));
		}
	}

	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		return false;
	}

}
