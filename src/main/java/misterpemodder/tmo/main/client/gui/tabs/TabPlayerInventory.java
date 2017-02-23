package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class TabPlayerInventory<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase{

	public TabPlayerInventory() {
		super(TabPos.BOTTOM_RIGHT);
	}
	
	@Override
	public TabID getTabID() {
		return TabID.PLAYER_INV;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		guiContainer.getFontRenderer().drawString(guiContainer.container.getPlayerInv().getInventoryPlayer().getDisplayName().getUnformattedText(), 26, 139, 4210752);
	}
	
	public boolean shouldDisplaySlot(IHidable slot) {
		return slot instanceof SlotHidable && ((SlotHidable)slot).getItemHandler() == this.guiContainer.container.getPlayerInv();
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.playerInv.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Blocks.CHEST);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/player_inventory.png"), new Dimension(212, 100));
	}

}
