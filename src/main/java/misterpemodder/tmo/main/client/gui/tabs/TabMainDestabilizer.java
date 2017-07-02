package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiContainerBase;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeCategoryDestabilizer;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeCategoryEnderMatter;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.items.IItemHandler;

public class TabMainDestabilizer extends TabMain<ContainerDestabilizer, TileEntityDestabilizer> {

	@Override
	public TabID getTabID() {
		return TabID.MAIN_DESTABILIZER;
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
		GlStateManager.enableBlend();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.getTabTexture().screenTexture);
		if(te != null && te.getEnderMatterAmount() > 0) {
			int e = (te.getEnderMatterAmount()*138)/TileEntityDestabilizer.MAX_ENDER_MATTER;
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+12, guiContainer.getGuiTop()+83, 73, 101, e <= 0? 1:e, 6, 256, 128);
		}
		
		GlStateManager.disableBlend();
		
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		List<String> strs = this.geElementTank() != null? this.geElementTank().getHoverDesc(mouseX, mouseY, guiContainer) : new ArrayList<>();
		
		if(strs.isEmpty() && guiContainer.isPointInRegion(12, 83, 138, 6, mouseX, mouseY)) {
			TileEntityDestabilizer te = getTileEntity();
			if(te != null) {
				strs.add(Tmo.proxy.translate("gui.bar.ender")+": "+te.getEnderMatterAmount());
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
	public boolean shouldDisplaySlot(IHidable slot) {
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
	public RecipeClickableAreaTMO[] getRecipeClickableAreas() {
		return new RecipeClickableAreaTMO[]{
			new RecipeClickableAreaTMO(guiContainer.getGuiTop()+64, guiContainer.getGuiTop()+81, guiContainer.getGuiLeft()+15, guiContainer.getGuiLeft()+32, RecipeCategoryEnderMatter.UID),
			new RecipeClickableAreaTMO(guiContainer.getGuiTop()+39, guiContainer.getGuiTop()+60, guiContainer.getGuiLeft()+115, guiContainer.getGuiLeft()+143, RecipeCategoryDestabilizer.UID),
		};
	}
	
	private ContainerElementTank geElementTank() {
		return guiContainer.container.tank;
	}

}
