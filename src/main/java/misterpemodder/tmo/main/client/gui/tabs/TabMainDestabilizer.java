package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.compat.jei.destabilizer.RecipeCategoryDestabilizer;
import misterpemodder.tmo.main.compat.jei.endermatter.RecipeCategoryEnderMatter;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerMachine;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.items.IItemHandler;

public class TabMainDestabilizer extends TabMain<ContainerDestabilizer, TileEntityDestabilizer>{

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
		
		ContainerElementArrow arrow = ((ContainerMachine)guiContainer.container).arrow;
		
		if(arrow != null) {
			arrow.drawArrow(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, false);
		}
		
		/*GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocationTmo("textures/gui/container/misc.png"));
		Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, 58, 107, 28, 21, 128, 128);
		int p = ((ContainerMachine)this.guiContainer.container).progress;
		int t = 0;
		if(p > 0) {
			t = (int) (Minecraft.getMinecraft().world.getTotalWorldTime() % 4);
			Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+115, guiContainer.getGuiTop()+39, 87, 107, p, 21, 128, 128);
		}
		
		Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+118, guiContainer.getGuiTop()+42, t*15 + t, 91, 15, 5, 128, 128);
		Gui.drawModalRectWithCustomSizedTexture(guiContainer.getGuiLeft()+118, guiContainer.getGuiTop()+52, t*15 + t, 101, 15, 5, 128, 128);
		*/
		
		TileEntityDestabilizer te = (TileEntityDestabilizer) guiContainer.container.getTileEntity();
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
		
		List<String> strs = this.geElementTank() != null? this.geElementTank().getHoverDesc(mouseX, mouseY, guiContainer) : new ArrayList<String>();
		
		if(strs.isEmpty() && guiContainer.isPointInRegion(12, 83, 138, 6, mouseX, mouseY)) {
			TileEntityDestabilizer te = (TileEntityDestabilizer) guiContainer.container.getTileEntity();
			if(te != null) {
				strs.add(Tmo.proxy.translate("gui.bar.ender")+": "+te.getEnderMatterAmount());
			}
		}
		
		if(!strs.isEmpty()) {
			GuiUtils.drawHoveringText(strs, mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
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
		if(slot instanceof SlotHidable && guiContainer.container.getTileEntity() instanceof TileEntityDestabilizer) {
			TileEntityDestabilizer te = (TileEntityDestabilizer)guiContainer.container.getTileEntity();
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
		return ((ContainerDestabilizer)guiContainer.container).tank;
	}

}
