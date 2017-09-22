package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.inventory.ContainerBase;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class TabRedstone<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase<C, TE> {

	private final boolean isLarge;
	
	public static final String ID = TMORefs.MOD_ID + ".redstone";
	
	public TabRedstone(boolean isLarge) {
		super(TabPos.TOP_RIGHT);
		this.isLarge = isLarge;
	}
	
	@Override
	public String getTabID() {
		return ID;
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
		if(isLarge)
			return new TabTexture(TMORefs.TAB_LOCATION, new Point(0, 28), new Point(32, 28), new ResourceLocationTmo("textures/gui/container/titanium_chest/redstone.png"), new Dimension(212, 132), new Dimension(256, 256));
		else
			return new TabTexture(TMORefs.TAB_LOCATION, new Point(0, 28), new Point(32, 28), new ResourceLocationTmo("textures/gui/container/redstone.png"), new Dimension(212, 100));
	}

	@Override
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		return false;
	}

}
