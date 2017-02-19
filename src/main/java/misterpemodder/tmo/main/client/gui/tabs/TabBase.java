package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.GuiContainerBase;
import misterpemodder.tmo.main.client.gui.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class TabBase<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 28;
	public static final ResourceLocation DEFAULT_TAB_LOCATION = new ResourceLocationTmo("textures/gui/container/tabs.png");
	
	protected TabPos pos;
	protected GuiContainerBase<C, TE> guiContainer;
	
	protected TabBase(TabPos pos) {
		this.pos = pos;
	}
	
	public abstract String getUnlocalizedName();
	
	public abstract ItemStack getItemStack();
	
	public abstract TabTexture getTabTexture();
	
	public abstract boolean shouldDisplaySlot(SlotHidable slot);
	
	public MutablePair<TabBase, TabBase> forceTabConfig() {
		return this.guiContainer.getSelectedTabs();
	}
	
	public TabPos getTabPos() {
		return this.pos;
	}
	
	public void onClick() {
	}
	
	public void setGuiContainer(GuiContainerBase<C, TE> guiContainer) {
		this.guiContainer = guiContainer;
	}
	
	public Point getPos() {
		TabPos tp = getTabPos();
		TabTexture texture = getTabTexture();
		int px = tp == TabPos.TOP_RIGHT || tp == TabPos.BOTTOM_RIGHT? texture.dim.width-GuiContainerBase.TAB_OFFSET : GuiContainerBase.TAB_OFFSET-TabBase.WIDTH;
		int py = tp == TabPos.BOTTOM_LEFT || tp == TabPos.BOTTOM_RIGHT? guiContainer.getBottomPartPos()-this.guiContainer.getGuiTop() : 0;
		
		for(TabBase t : guiContainer.getRegisteredTabs()) {
			if(t == this) break;
			if(t.getTabPos() == this.getTabPos()) {
				py += HEIGHT + 1;
			}
		}
		
		return new Point(px, py);
	}
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}
	
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
		
	public static class TabTexture {
		public final ResourceLocation tabTexture;
		public final ResourceLocation screenTexture;
		public final Point enabledCoords;
		public final Point disabledCoords;
		public final Dimension dim;
		
		public TabTexture(ResourceLocation tabTexture, Point enabledCoords, Point disabledCoords, ResourceLocation screenTexture, Dimension dim) {
			this.tabTexture = tabTexture;
			this.screenTexture = screenTexture;
			this.enabledCoords = enabledCoords;
			this.disabledCoords = disabledCoords;
			this.dim = dim;
		}
	}
	
	public static enum TabPos {
		TOP_RIGHT,
		TOP_LEFT,
		BOTTOM_RIGHT,
		BOTTOM_LEFT
	}
	
}
