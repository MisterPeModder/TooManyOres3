package misterpemodder.tmo.main.inventory.elements;

import misterpemodder.hc.main.inventory.elements.ContainerElementBar;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.util.ResourceLocation;

public abstract class ContainerElementBarTMO extends ContainerElementBar {
	
	public static final ResourceLocation TEXTURE = new ResourceLocationTmo("textures/gui/container/bars.png");
	public static final int BAR_WIDTH = 140;
	public static final int BAR_HEIGHT = 8;
	
	protected ContainerElementBarTMO() {
		super(TEXTURE);
	}
	
	protected ContainerElementBarTMO(int fillPercent, boolean shouldDrawMinimum, boolean drawBackground, boolean drawForeground) {
		super(TEXTURE, fillPercent, shouldDrawMinimum, drawBackground, drawForeground);
	}
	
	@Override
	public int getBarWidth() {
		return BAR_WIDTH;
	}
	
	@Override
	public int getBarHeight() {
		return BAR_HEIGHT;
	}

}
