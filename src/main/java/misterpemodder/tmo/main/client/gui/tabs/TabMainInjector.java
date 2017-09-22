package misterpemodder.tmo.main.client.gui.tabs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.client.gui.GuiContainerBase;
import misterpemodder.hc.main.client.gui.RecipeClickableAreaHC;
import misterpemodder.hc.main.client.gui.tabs.TabBase;
import misterpemodder.hc.main.client.gui.tabs.TabMain;
import misterpemodder.hc.main.inventory.slot.IHidableSlot;
import misterpemodder.hc.main.inventory.slot.SlotDisableable;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.compat.jei.injector.RecipeCategoryInjector;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.items.IItemHandler;

public class TabMainInjector extends TabMain<ContainerInjector, TileEntityInjector> {
	
	public static final int TOGGLE_MODE_BUTTON_ID = 20;
	public static final String ID = "tmo.main.injector";

	@Override
	public String getTabID() {
		return ID;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(new ResourceLocationTmo("textures/gui/container/injector_main.png"));
	}
	
	@Override
	protected int getTitleX() {
		return 60;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		TileEntityInjector te = getTileEntity();
		
		if(getElementTank() != null) {
			
			getElementTank().drawTank(mouseX, mouseY, guiContainer);
		}
		
		ContainerElementArrow arrow = guiContainer.container.arrow;
		
		if(arrow != null) {
			arrow.drawArrow(guiContainer.getGuiLeft()+64, guiContainer.getGuiTop()+39, te.getTransferMode() == TransferMode.EXTRACTION);
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		List<String> strs = getElementTank() != null? getElementTank().getHoverDesc(mouseX, mouseY, guiContainer) : new ArrayList<>();

		if(strs.isEmpty()) {
			for(GuiButton b : buttons) {
				if(b.id == TOGGLE_MODE_BUTTON_ID && b instanceof TransferModeButton) {
					if(b.isMouseOver()) {
						strs.add(((TransferModeButton)b).mode == TransferMode.INJECTION? TextFormatting.AQUA+StringUtils.translate("gui.injecter.mode.injection") : TextFormatting.GOLD+StringUtils.translate("gui.injecter.mode.extraction"));
						strs.add(TextFormatting.GRAY+""+ TextFormatting.ITALIC+"-"+StringUtils.translate("gui.injecter.mode.desc")+"-");
					}
						
					((TransferModeButton)b).mode = getTileEntity().getTransferMode();
				}
			}
		}
		
		if(!strs.isEmpty()) {
			GuiContainerBase.addHoveringText(strs, 250);
		}

	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(getElementTank() == null || !getElementTank().mouseClicked(mouseX, mouseY, mouseButton, guiContainer)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void initButtons(int topX, int topY) {
		buttons.add(new TransferModeButton(TOGGLE_MODE_BUTTON_ID, topX+68, topY+64, 20, 20, getTileEntity().getTransferMode()));
	}
	
	@Override
	public void updateButtons() {
		for(GuiButton b : buttons) {
			if(b.id == TOGGLE_MODE_BUTTON_ID && b instanceof TransferModeButton) {
				((TransferModeButton)b).mode = getTileEntity().getTransferMode();
			}
		}
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidableSlot slot) {
		if(slot instanceof SlotDisableable) {
			TileEntityInjector te = getTileEntity();
			IItemHandler h = ((SlotDisableable)slot).getItemHandler();
			return h == te.getInventory() || h == te.output;
		}
		return false;
	}
	
	@Override
	public void onButtonClicked(GuiButton button) {
		if(button.id == TOGGLE_MODE_BUTTON_ID && button instanceof TransferModeButton) {
			TransferModeButton b = (TransferModeButton) button;
			NBTTagCompound data = new NBTTagCompound();
			TransferMode newMode = b.mode == TransferMode.INJECTION? TransferMode.EXTRACTION : TransferMode.INJECTION;
			data.setInteger("mode", newMode.ordinal());
			TabBase.sendButtonPacket(getTabID(), TOGGLE_MODE_BUTTON_ID, guiContainer.mc.world, getTileEntity().getPos(), data);
		}
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaHC[] getRecipeClickableAreas() {
		return new RecipeClickableAreaHC[]{new RecipeClickableAreaHC(guiContainer.getGuiTop()+39, guiContainer.getGuiTop()+60, guiContainer.getGuiLeft()+64, guiContainer.getGuiLeft()+92, RecipeCategoryInjector.UID)};
	}
	
	public static class TransferModeButton extends GuiButton {
		
		public static final ResourceLocation BUTTON_TEXTURES = new ResourceLocationTmo("textures/gui/container/injector_main.png"); 
		public TransferMode mode;
		
		public TransferModeButton(int buttonId, int x, int y, int widthIn, int heightIn, TransferMode mode) {
			super(buttonId, x, y, widthIn, heightIn, "");
			this.mode = mode;
		}
		
		public void drawButton(Minecraft mc, int mouseX, int mouseY) {
	        if (this.visible) {
	            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
	            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	            int i = this.getHoverState(this.hovered);
	            Gui.drawModalRectWithCustomSizedTexture(xPosition, yPosition, 236-i*width, 100, width, height, 256, 128);
	            
	        }
	    }
		
		@Override
		protected int getHoverState(boolean mouseOver) {
			int i = mode == TransferMode.INJECTION? 0 : 2;
			i += mouseOver? 1 : 0;
			return i;
		}
		
	}
	
	private ContainerElementTank getElementTank() {
		return guiContainer.container.tank;
	}

}
