package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.compat.jei.injector.RecipeCategoryInjector;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerMachine;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.inventory.slot.IHidable;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.items.IItemHandler;

public class TabMainInjector extends TabMain<ContainerInjector, TileEntityInjector> {
	
	public static final int TOGGLE_MODE_BUTTON_ID = 20;

	@Override
	public TabID getTabID() {
		return TabID.MAIN_INJECTOR;
	}

	@Override
	public TabTexture getTabTexture() {
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,0), new Point(32, 0), new ResourceLocationTmo("textures/gui/container/injector_main.png"), new Dimension(212, 100));
	}
	
	@Override
	protected int getTitleX() {
		return 60;
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		
		TileEntityInjector te = (TileEntityInjector)guiContainer.container.getTileEntity();
		
		if(geElementTank() != null) {
			
			geElementTank().drawTank(mouseX, mouseY, guiContainer);
		}
		
		ContainerElementArrow arrow = ((ContainerMachine)guiContainer.container).arrow;
		
		if(arrow != null) {
			arrow.drawArrow(guiContainer.getGuiLeft()+64, guiContainer.getGuiTop()+39, te.getTransferMode() == TransferMode.EXTRACTION);
		}
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		
		List<String> strs = geElementTank() != null? geElementTank().getHoverDesc(mouseX, mouseY, guiContainer) : new ArrayList<String>();

		if(strs.isEmpty()) {
			for(GuiButton b : (List<GuiButton>)buttons) {
				if(b.id == TOGGLE_MODE_BUTTON_ID && b instanceof TransferModeButton) {
					if(b.isMouseOver()) {
						strs.add(((TransferModeButton)b).mode == TransferMode.INJECTION? TextFormatting.AQUA+Tmo.proxy.translate("gui.injecter.mode.injection") : TextFormatting.GOLD+Tmo.proxy.translate("gui.injecter.mode.extraction"));
						strs.add(TextFormatting.GRAY+""+ TextFormatting.ITALIC+"-"+Tmo.proxy.translate("gui.injecter.mode.desc")+"-");
					}
						
					((TransferModeButton)b).mode = ((TileEntityInjector)guiContainer.container.getTileEntity()).getTransferMode();
				}
			}
		}
		
		if(!strs.isEmpty()) {
			GuiUtils.drawHoveringText(strs, mouseX-guiContainer.getGuiLeft(), mouseY-guiContainer.getGuiTop(), guiContainer.width, guiContainer.height, 200, guiContainer.getFontRenderer());
		}

	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if(geElementTank() == null || !geElementTank().mouseClicked(mouseX, mouseY, mouseButton, guiContainer)) {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public void initButtons(int topX, int topY) {
		buttons.add(new TransferModeButton(TOGGLE_MODE_BUTTON_ID, topX+68, topY+64, 20, 20, ((TileEntityInjector)guiContainer.container.getTileEntity()).getTransferMode()));
	}
	
	@Override
	public void updateButtons() {
		for(GuiButton b : (List<GuiButton>)buttons) {
			if(b.id == TOGGLE_MODE_BUTTON_ID && b instanceof TransferModeButton) {
				((TransferModeButton)b).mode = ((TileEntityInjector)guiContainer.container.getTileEntity()).getTransferMode();
			}
		}
	}
	
	@Override
	public boolean shouldDisplaySlot(IHidable slot) {
		if(slot instanceof SlotHidable && guiContainer.container.getTileEntity() instanceof TileEntityInjector) {
			TileEntityInjector te = (TileEntityInjector)guiContainer.container.getTileEntity();
			IItemHandler h = ((SlotHidable)slot).getItemHandler();
			return h == te.getInventory() || h == te.getOutput();
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
			TabBase.sendButtonPacket(getTabID(), TOGGLE_MODE_BUTTON_ID, guiContainer.mc.world, guiContainer.container.getTileEntity().getPos(), data);
		}
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaTMO[] getRecipeClickableAreas() {
		return new RecipeClickableAreaTMO[]{new RecipeClickableAreaTMO(guiContainer.getGuiTop()+39, guiContainer.getGuiTop()+60, guiContainer.getGuiLeft()+64, guiContainer.getGuiLeft()+92, RecipeCategoryInjector.UID)};
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
	
	private ContainerElementTank geElementTank() {
		return ((ContainerInjector)guiContainer.container).tank;
	}

}
