package misterpemodder.tmo.client.gui;

import java.awt.Color;

import misterpemodder.tmo.main.tileentity.TileEntityItemKeeper;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiContainerItemKeeper extends GuiContainer {
	
	private static final String path = "textures/gui/container/item_keeper.png";
	private IInventory playerInv;
	private TileEntityItemKeeper te;
	
	public GuiContainerItemKeeper(IInventory playerInv, TileEntityItemKeeper te) {
        super(new ContainerItemKeeper(te, playerInv));
        this.xSize = 176;
        this.ySize = 166;
        this.playerInv = playerInv;
        this.te = te;
    }
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation(TMOHelper.PREFIX + GuiContainerItemKeeper.path));
	    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		//"Item Keeper"
	    String s = this.te.getDisplayName().getUnformattedText();
	    this.fontRendererObj.drawString(s, 88 - this.fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
	    //"Inventory"
	    this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 72, 4210752);
	}
	
}
