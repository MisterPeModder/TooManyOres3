package misterpemodder.tmo.main.client.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMOHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public final class GuiTabs {
	
	public static final String PATH = TMOHelper.PREFIX + "textures/gui/container/tabs.png";
	
	public enum EnumTabs {
		MAIN("gui.tab.main.name", null, null, new Point(0,0), new Point(32, 0)),
		INFO("gui.tab.info.name", Items.WRITTEN_BOOK, "textures/gui/container/info.png", new Point(0,28), new Point(32, 28)),
		REDSTONE("gui.tab.redstone.name", Items.REDSTONE , "textures/gui/container/redstone.png", new Point(0,84), new Point(32, 84)),
		SECURITY("gui.tab.security.name", ItemBlock.getItemFromBlock(Blocks.STRUCTURE_VOID), "textures/gui/container/security.png", new Point(96, 0), new Point(64, 0), true, false),
		PLAYER_INVENTORY("gui.tab.playerInv.name", ItemBlock.getItemFromBlock(Blocks.CHEST), "textures/gui/container/player_inventory.png", new Point(0, 0), new Point(32, 0), false, true),
		IO("gui.tab.io.name", ItemBlock.getItemFromBlock(Blocks.HOPPER), "textures/gui/container/io.png", new Point(0, 56), new Point(32, 56), false, true),
		;
		public static final Dimension SIZE = new Dimension(32, 28);
		public static final String PATH = TMOHelper.PREFIX + "textures/gui/container/tabs.png";
		private final String name;
		private final String screenPath;
		public final Item item;
		public final Point coordsEnabled;
		public final Point coordsDisabled;
		public final boolean isLeft;
		public final boolean isBottom;
		
		public static final EnumTabs DEFAULT_TOP = MAIN;
		public static final EnumTabs DEFAULT_BOTTOM = PLAYER_INVENTORY;
		
		public String getLocalizedName() {
			return Tmo.proxy.translate(name);
		}
		
		public Item getItem(Item i) {
			if(item == null) return i;
			return item;
		}

		public String getScreenPath() {
			return TMOHelper.PREFIX + screenPath;
		}

		public static List<EnumTabs> getTabs() {
			return Arrays.asList(EnumTabs.values());
		}
		
		EnumTabs(String name, Item item, String screenPath, Point coordsEnabled, Point coordsDisabled) {
			this(name, item, screenPath, coordsEnabled, coordsDisabled, false, false);
		}
	
		EnumTabs(String name, Item item, String screenPath, Point coordsEnabled, Point coordsDisabled, boolean isLeft, boolean isBottom) {
			this.name = name;
			this.item = item;
			this.screenPath = screenPath;
			this.coordsEnabled = coordsEnabled;
			this.coordsDisabled = coordsDisabled;
			this.isLeft = isLeft;
			this.isBottom = isBottom;
		}
	}
	
	public static void drawTab(GuiContainerBase container, EnumTabs tab, Point pos, boolean enabled) {
		Point uv = enabled? tab.coordsEnabled : tab.coordsDisabled;
		container.drawModalRectWithCustomSizedTexture(pos.x+container.getGuiLeft(), pos.y+container.getGuiTop(), uv.x, uv.y, tab.SIZE.width, tab.SIZE.height, 128, 128);
	}
	
}
