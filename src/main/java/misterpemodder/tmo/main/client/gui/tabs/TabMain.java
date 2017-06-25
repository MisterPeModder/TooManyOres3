package misterpemodder.tmo.main.client.gui.tabs;

import misterpemodder.tmo.main.inventory.ContainerBase;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public abstract class TabMain<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {
	
	protected TabMain() {
		super(TabPos.TOP_RIGHT);
	}
	
	@Override
	public ItemStack getItemStack() {
		return new ItemStack(guiContainer.container.getTileEntity().getBlockType());
	}

	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		TileEntityContainerBase te = guiContainer.container.getTileEntity();
		
		String dispName = "";
		
		if(te instanceof IWorldNameable) {
			dispName = ((IWorldNameable)te).getName();
		} else {
			dispName = te.getDisplayName().getUnformattedText();
		}
		guiContainer.getFontRenderer().drawString(dispName, getTitleX(), getTitleY(), 4210752);
	}
	
	protected int getTitleX() {
		return 8;
	}
	
	protected int getTitleY() {
		return 6;
	}
	
	public boolean shouldDisplaySlot(IHidable slot) {
		return slot instanceof SlotHidable && ((SlotHidable)slot).getItemHandler() == this.guiContainer.container.getTileEntity().getInventory();
	}
	
	@Override
	public String getUnlocalizedName() {
		return "gui.tab.main.name";
	}

}
