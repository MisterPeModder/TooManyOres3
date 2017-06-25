package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.main.client.gui.GuiContainerBase;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class TabBase<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> {
	
	public static final int WIDTH = 32;
	public static final int HEIGHT = 28;
	public static final ResourceLocation DEFAULT_TAB_LOCATION = new ResourceLocationTmo("textures/gui/container/tabs.png");
	
	protected TabPos pos;
	protected GuiContainerBase<C, TE> guiContainer;
	protected List<GuiButton> buttons;
	
	protected TabBase(TabPos pos) {
		this.pos = pos;
		buttons = new ArrayList<GuiButton>();
	}
	
	public abstract TabID getTabID();
	
	public abstract String getUnlocalizedName();
	
	public abstract ItemStack getItemStack();
	
	public abstract TabTexture getTabTexture();
	
	public abstract boolean shouldDisplaySlot(IHidable slot);
	
	public MutablePair<TabBase, TabBase> forceTabConfig() {
		return this.guiContainer.getSelectedTabs();
	}
	
	public TabPos getTabPos() {
		return this.pos;
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
	
	public void initButtons(int topX, int topY) {}
	
	public void onGuiClosed() {}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {}
	
	public boolean keyTyped(char typedChar, int keyCode) throws IOException {
		return false;
	}
	
	public List<GuiButton> getButtonsList() {
		return this.buttons;
	}
	
	public <T extends GuiButton> void onButtonClicked(T button) {}
	
	@SideOnly(Side.CLIENT)
	public static void sendButtonPacket(TabID tabId, int buttonId, WorldClient world, BlockPos pos, NBTTagCompound data) {
		NBTTagCompound toSend = new NBTTagCompound();
		
		toSend.setLong("pos", pos.toLong());
		toSend.setInteger("world_dim_id", world.provider.getDimension());
		toSend.setInteger("tab_id", tabId.ordinal());
		toSend.setInteger("button_id", buttonId);
		toSend.setTag("info", data);
		TMOPacketHandler.network.sendToServer(new PacketClientToServer(PacketDataHandlers.BUTTON_CLICK_HANDLER, toSend));
	}
	
	public void updateButtons() {}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {}
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}
	
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {}
	
	public boolean hasRecipeClickableAreas() {
		return false;
	}
	
	public @Nullable RecipeClickableAreaTMO[] getRecipeClickableAreas() {
		return null;
	}
		
	public static class TabTexture {
		public final ResourceLocation tabTexture;
		public final ResourceLocation screenTexture;
		public final Point enabledCoords;
		public final Point disabledCoords;
		public final Dimension dim;
		public final Dimension textureSize;
		
		public TabTexture(ResourceLocation tabTexture, Point enabledCoords, Point disabledCoords, ResourceLocation screenTexture, Dimension dim) {
			this(tabTexture, enabledCoords, disabledCoords, screenTexture, dim, null);
		}
		
		public TabTexture(ResourceLocation tabTexture, Point enabledCoords, Point disabledCoords, ResourceLocation screenTexture, Dimension dim, Dimension textureSize) {
			this.tabTexture = tabTexture;
			this.screenTexture = screenTexture;
			this.enabledCoords = enabledCoords;
			this.disabledCoords = disabledCoords;
			this.dim = dim;
			this.textureSize = textureSize == null? new Dimension(256, 128) : textureSize;
		}
	}
	
	public static enum TabPos {
		TOP_RIGHT,
		TOP_LEFT,
		BOTTOM_RIGHT,
		BOTTOM_LEFT
	}
	
	public static enum TabID {
		ARMOR_INV,
		INFO,
		IO,
		MAIN_TC,
		PLAYER_INV,
		REDSTONE,
		SECURITY,
		MAIN_TANVIL,
		MAIN_INJECTOR,
		MAIN_DESTABILIZER,
		MISC,
	}
	
}
