package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Arrays;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.config.GuiUtils;

public class TabSecurity<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase {
	
	public TabSecurity() {
		super(TabPos.TOP_LEFT);
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.security.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(Blocks.STRUCTURE_VOID);
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(96,0), new Point(64, 0), new ResourceLocationTmo("textures/gui/container/security.png"), new Dimension(212, 132));
	}

	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		TileEntity te = guiContainer.container.getTileEntity();
		if(te instanceof ILockable && slot instanceof SlotHidable) {
			return ((SlotHidable)slot).getItemHandler() == ((ILockable)te).getLockItemHandler();
		}
		return false;
	}
	
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if(guiContainer.container.getTileEntity() instanceof ILockable) {
			if(((ILockable)guiContainer.container.getTileEntity()).getLockItemHandler().getStackInSlot(0).isEmpty()) {
				guiContainer.getMinecraft().getTextureManager().bindTexture(new ResourceLocationTmo("textures/items/empty_lock_slot.png"));
				Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+8, guiContainer.getGuiTop()+18, 0, 0, 16, 16, 16, 16);
			}
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		if(guiContainer.isPointInRegion(8, 18, 16, 16, mouseX, mouseY)) {
    		GuiUtils.drawHoveringText(Arrays.asList(Tmo.proxy.translate("gui.slot.lock.name"), Tmo.proxy.translate("gui.slot.lock.desc")), mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
	    }
	}
	
}
