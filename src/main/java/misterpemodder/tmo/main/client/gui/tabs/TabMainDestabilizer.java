package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.RecipeClickableAreaHC;
import misterpemodder.hc.main.client.gui.tabs.TabMain;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotHidable;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeCategoryDestabilizer;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeCategoryEnderMatter;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraftforge.items.IItemHandler;

public class TabMainDestabilizer extends TabMain<ContainerDestabilizer, TileEntityDestabilizer> {

	public static final String ID = "tmo.main.destabilizer";
	
	@Override
	public String getTabID() {
		return ID;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/destabilizer_main.png"), new Dimension(212, 100));
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		if(geElementTank() != null) {
			geElementTank().drawTank(mouseX, mouseY, guiContainer);
		}
		
		ContainerElementArrow arrow = guiContainer.container.arrow;
		
		if(arrow != null) {
			arrow.drawArrow(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, false);
		}
		
		TileEntityDestabilizer te = getTileEntity();
		if(te != null && te.getEnderMatterAmount() > 0) {
			int e = (te.getEnderMatterAmount()*138)/TileEntityDestabilizer.MAX_ENDER_MATTER;
			guiContainer.container.enderMatterBar.drawBar(guiContainer.getGuiLeft()+11, guiContainer.getGuiTop()+82, e <= 0? 2:e+1);
		} else {
			guiContainer.container.enderMatterBar.drawBar(guiContainer.getGuiLeft()+11, guiContainer.getGuiTop()+82, 0);
		}
		
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		List<String> strs = this.geElementTank() != null? this.geElementTank().getHoverDesc(mouseX, mouseY, guiContainer) : new ArrayList<>();
		
		if(strs.isEmpty() && guiContainer.isPointInRegion(12, 83, 138, 6, mouseX, mouseY)) {
			TileEntityDestabilizer te = getTileEntity();
			if(te != null) {
				strs.add(StringUtils.translate("gui.bar.ender")+": "+te.getEnderMatterAmount());
			}
		}
		
		if(!strs.isEmpty()) {
			GuiContainerBase.addHoveringText(strs, 250);
		}

	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(geElementTank() == null || !this.geElementTank().mouseClicked(mouseX, mouseY, mouseButton, guiContainer)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		if(slot instanceof SlotHidable) {
			TileEntityDestabilizer te = getTileEntity();
			IItemHandler h = ((SlotHidable)slot).getItemHandler();
			return h == te.getInventory() || h == te.getEnder();
		}
		return false;
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaHC[] getRecipeClickableAreas() {
		return new RecipeClickableAreaHC[]{
			new RecipeClickableAreaHC(guiContainer.getGuiTop()+64, guiContainer.getGuiTop()+81, guiContainer.getGuiLeft()+15, guiContainer.getGuiLeft()+32, RecipeCategoryEnderMatter.UID),
			new RecipeClickableAreaHC(guiContainer.getGuiTop()+39, guiContainer.getGuiTop()+60, guiContainer.getGuiLeft()+115, guiContainer.getGuiLeft()+143, RecipeCategoryDestabilizer.UID),
		};
	}
	
	private ContainerElementTank geElementTank() {
		return guiContainer.container.tank;
	}

}
