package misterpemodder.tmo.main.inventory.elements;

import java.awt.Point;

public class ContainerElementEnderMatterBar extends ContainerElementBar {
	
	private static final Point BACKGROUND_UV = new Point(0, 0);
	private static final Point FOREGROUND_UV = new Point(0, 8);
	
	public ContainerElementEnderMatterBar() {
		super();
	}
	
	public ContainerElementEnderMatterBar(int fillPercent, boolean shouldDrawMinimum, boolean drawBackground, boolean drawForeground) {
		super(fillPercent, shouldDrawMinimum, drawBackground, drawForeground);
	}

	@Override
	protected Point getBackgroundUV() {
		return BACKGROUND_UV;
	}
	
	@Override
	protected Point getForegroundUV() {
		return FOREGROUND_UV;
	}

}
