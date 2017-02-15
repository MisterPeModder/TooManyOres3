package misterpemodder.tmo.main.client.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiTabs.EnumTabs;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.fml.client.config.GuiUtils;

public abstract class GuiContainerBase<C extends ContainerBase> extends GuiContainer {
	
	public static final Dimension INV_SIZE_TE = new Dimension(212, 132);
	public static final Dimension INV_SIZE_PLAYER = new Dimension(212, 100);
	public static final int tabOffset = 4;
	protected C container;
	
	protected EnumTabs[] selectedTabs = new EnumTabs[]{EnumTabs.DEFAULT_TOP, EnumTabs.DEFAULT_BOTTOM};
	protected Map<EnumTabs,Point> tabsPos = new HashMap<>();
	
	public GuiContainerBase(C container) {
		super(container);
		this.container = container;
		this.xSize = (INV_SIZE_TE.width + GuiTabs.EnumTabs.SIZE.width) - tabOffset;
        this.ySize = INV_SIZE_TE.height + INV_SIZE_PLAYER.height;
        setTabsPos();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
		selectedTabs[0] = selectedTabs[1] == EnumTabs.IO? EnumTabs.MAIN : selectedTabs[0];
		
	    //Disabled Tabs
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		this.mc.getTextureManager().bindTexture(new ResourceLocation(GuiTabs.PATH));
	    this.drawDisabledTabs();
	    
	    //Bottom part
	    this.mc.getTextureManager().bindTexture(new ResourceLocation(selectedTabs[1].getScreenPath()));
		this.drawModalRectWithCustomSizedTexture(this.getGuiLeft(), this.getGuiTop()+INV_SIZE_TE.height, 0, 0, 212, 100, 256, 128);
		
		//Top Part
		if(selectedTabs[0] == EnumTabs.MAIN) {
			drawTeInv(partialTicks, mouseX, mouseY);
		} else {
			GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		    this.mc.getTextureManager().bindTexture(new ResourceLocation(selectedTabs[0].getScreenPath()));
		    this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.INV_SIZE_TE.height);
		    if(container.te instanceof ILockable) {
		    	try{
		    	if(selectedTabs[0] == EnumTabs.SECURITY && ((ILockable)container.te).getLockItemHandler().getStackInSlot(0).isEmpty()) {
		    		this.mc.getTextureManager().bindTexture(new ResourceLocation(TMORefs.PREFIX+"textures/items/empty_lock_slot.png"));
		    		this.drawModalRectWithCustomSizedTexture(this.guiLeft+8, this.guiTop+18, 0, 0, 16, 16, 16, 16);
		    	}
		    	} catch(Exception e) {
		    		
		    	}
		    }
		}
	    
	    //Enabled Tabs
	    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.mc.getTextureManager().bindTexture(new ResourceLocation(GuiTabs.PATH));
	    GuiTabs.drawTab(this, selectedTabs[0], tabsPos.get(selectedTabs[0]), true);
	    GuiTabs.drawTab(this, selectedTabs[1], tabsPos.get(selectedTabs[1]), true);

	}
	
	protected abstract void drawTeInv(float partialTicks, int mouseX, int mouseY);
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
		TileEntityContainerBase te = this.container.getTileEntity();
		
		String dispName = "";
		
		if(te instanceof IWorldNameable) {
			dispName = ((IWorldNameable)te).getName();
		} else {
			dispName = te.getDisplayName().getUnformattedText();
		}
		
		String nameTop = selectedTabs[0] == EnumTabs.MAIN? dispName : selectedTabs[0].getLocalizedName();
	    this.fontRendererObj.drawString(nameTop, 8, 6, 4210752);
	    
	    String nameBottom = selectedTabs[1] == EnumTabs.PLAYER_INVENTORY? this.container.getPlayerInv().getInventoryPlayer().getDisplayName().getUnformattedText() : selectedTabs[1].getLocalizedName(); 
	    this.fontRendererObj.drawString(nameBottom, 26, 139, 4210752);
        
	    
	    Item teItem = ItemBlock.getItemFromBlock(this.container.getTileEntity().getBlockType());
	    for(EnumTabs tab : tabsPos.keySet()) {
	    	RenderHelper.enableGUIStandardItemLighting();
	    	ItemStack stack = new ItemStack(tab.getItem(teItem));
	    	this.itemRender.renderItemIntoGUI(stack, tabsPos.get(tab).x + 8, tabsPos.get(tab).y + 6);
		    this.itemRender.renderItemOverlays(this.fontRendererObj, stack, tabsPos.get(tab).x + 8, tabsPos.get(tab).y + 6);
		    RenderHelper.disableStandardItemLighting();
		    if(isPointInRegion(tabsPos.get(tab).x+1, tabsPos.get(tab).y, EnumTabs.SIZE.width, EnumTabs.SIZE.height-1, mouseX, mouseY)) {
		    	drawCreativeTabHoveringText(tab.getLocalizedName(), mouseX-this.getGuiLeft(), mouseY-this.getGuiTop());
		    }
	    }
	    
	    if(selectedTabs[0] == EnumTabs.SECURITY && ((ILockable)container.te).getLockItemHandler().getStackInSlot(0).isEmpty()) {
	    	if(isPointInRegion(8, 18, 16, 16, mouseX, mouseY)) {
	    		GuiUtils.drawHoveringText(Arrays.asList(Tmo.proxy.translate("gui.slot.lock.name"), Tmo.proxy.translate("gui.slot.lock.desc")), mouseX-this.getGuiLeft(), mouseY-this.getGuiTop(), width, height, 200, this.fontRendererObj);
		    }
    	}
        
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(EnumTabs tab : tabsPos.keySet()) {
		    if(isPointInRegion(tabsPos.get(tab).x+1, tabsPos.get(tab).y, EnumTabs.SIZE.width, EnumTabs.SIZE.height-1, mouseX, mouseY)) {
		    	onTabClick(tab);
		    }
	    }
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	protected void onTabClick(EnumTabs tab) {
		
		if(tab != selectedTabs[1] && tab != selectedTabs[0]) {
			this.mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
			selectedTabs[tab.isBottom ? 1 : 0] = tab;
			if (selectedTabs[1] == EnumTabs.IO && !tab.isBottom)
				selectedTabs[1] = EnumTabs.DEFAULT_BOTTOM;
			this.container.selectedTabs = selectedTabs;
			this.container.hideSlots();

		}
		
	}
	
	private void setTabsPos() {
		Point pRight = new Point(INV_SIZE_TE.width - tabOffset, 0);
		Point pLeft = new Point(tabOffset-EnumTabs.SIZE.width, 0);
		Point pBottom = new Point(INV_SIZE_TE.width - tabOffset, INV_SIZE_TE.height);

		for(EnumTabs tab : getTabs()) {
			Point p = tab.isLeft?pLeft:(tab.isBottom?pBottom:pRight);
			tabsPos.put(tab, (Point)p.clone());
			p.translate(0, EnumTabs.SIZE.height+1);
		}
	}
	
	protected void drawDisabledTabs() {
		for(EnumTabs tab : tabsPos.keySet()) {
			boolean tet = tab == EnumTabs.SECURITY;;
			if(tab == EnumTabs.SECURITY) {
				int lool = 5;
			}
			if(tab != selectedTabs[0] && tab != selectedTabs[1]) {
				GuiTabs.drawTab(this, tab, tabsPos.get(tab), false);
			}
		}
	}
	
	public abstract List<EnumTabs> getTabs();
	
}
