package misterpemodder.tmo.main.client.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.client.gui.tabs.TabBase.TabPos;
import misterpemodder.tmo.main.client.gui.tabs.TabBase.TabTexture;
import misterpemodder.tmo.main.client.gui.tabs.TabMain;
import misterpemodder.tmo.main.client.gui.tabs.TabPlayerInventory;
import misterpemodder.tmo.main.compat.jei.JeiPlugin;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public abstract class GuiContainerBase<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends GuiContainer {
	
	public static final int TAB_OFFSET = 4;
	public C container;
	
	protected MutablePair<TabBase, TabBase> selectedTabs;
	protected List<TabBase> tabs;
	
	protected List<GuiButton> selectedButtonsLeft;
	protected List<GuiButton> selectedButtonsRight;
	
	public GuiContainerBase(C container) {
		super(container);
		this.container = container;
		this.tabs = registerTabs();
		
		if(tabs.size() < 2) throw new IllegalArgumentException("There must be at least 2 tabs!");
		initTabs();
		
		Pair<TabBase, TabBase> p = getDefaultPair();
        this.selectedTabs = MutablePair.of(p.getLeft(), p.getRight());
        
        Dimension dl = selectedTabs.left.getTabTexture().dim;
		Dimension dr = selectedTabs.right.getTabTexture().dim;
        this.xSize = (Math.max(dl.width, dr.width) + TabBase.WIDTH);
      	this.ySize = dl.height + dr.height;
      	
      	this.selectedButtonsLeft = new ArrayList<>();
      	this.selectedButtonsRight = new ArrayList<>();
	}
	
	private void initTabs() {
		for(TabBase tab : tabs) {
			tab.setGuiContainer(this);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
		for(TabBase tab : tabs) {
			tab.initButtons(this.guiLeft, this.guiTop);
		}
	}
	
	protected Pair<TabBase, TabBase> getDefaultPair() {
		MutablePair<TabBase, TabBase> p = new MutablePair<>();
		for(TabBase tab : tabs) {
			if(tab instanceof TabMain) {
				p.setLeft(tab);
			} else if(tab instanceof TabPlayerInventory) {
				p.setRight(tab);
			}
		}
		if(p.getLeft() == null || p.getRight() == null) {
			p.setLeft(tabs.get(0));
			p.setRight(tabs.get(1));
		}
		return p;
	}
	
	public abstract List<TabBase> registerTabs();
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.selectedTabs.left.drawScreen(mouseX, mouseY, partialTicks);
		this.selectedTabs.right.drawScreen(mouseX, mouseY, partialTicks);
		addButtons();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
	    //Disabled Tabs
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
	    this.drawDisabledTabs();
	    
		//Top Part
	    TabTexture lt = selectedTabs.getLeft().getTabTexture();
		this.mc.getTextureManager().bindTexture(lt.screenTexture);
		Gui.drawModalRectWithCustomSizedTexture(this.guiLeft, this.guiTop, 0, 0, lt.dim.width, lt.dim.height, lt.textureSize.width, lt.textureSize.height);
		
		//Bottom part
		TabTexture rt = selectedTabs.getRight().getTabTexture();
	    this.mc.getTextureManager().bindTexture(rt.screenTexture);
	    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft, getBottomPartPos(), 0, 0, rt.dim.width, rt.dim.height, rt.textureSize.width, rt.textureSize.height);
		
	    //Enabled Tabs
	    this.drawTab(selectedTabs.getLeft(), true);
	    selectedTabs.getLeft().drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	    this.drawTab(selectedTabs.getRight(), true);
	    selectedTabs.getRight().drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
	    
	    GlStateManager.popMatrix();
	}
	
	protected void drawTab(TabBase tab, boolean enabled) {
		TabTexture texture = tab.getTabTexture();
		Point uv = enabled? texture.enabledCoords : texture.disabledCoords;
		Point coords = tab.getPos();
		this.mc.getTextureManager().bindTexture(tab.getTabTexture().tabTexture);
		Gui.drawModalRectWithCustomSizedTexture(this.getGuiLeft()+coords.x , this.getGuiTop()+coords.y, (float) uv.x, (float) uv.y, TabBase.WIDTH, TabBase.HEIGHT, 128F, 128F);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	    selectedTabs.getLeft().drawGuiContainerForegroundLayer(mouseX, mouseY);
	    selectedTabs.getRight().drawGuiContainerForegroundLayer(mouseX, mouseY);
	    
	    selectedTabs.getLeft().updateButtons();
	    selectedTabs.getRight().updateButtons();
	    
	    boolean flag = true;
	    for(TabBase tab : tabs) {
	    	RenderHelper.enableGUIStandardItemLighting();
	    	ItemStack stack = tab.getItemStack();
	    	Point tabPos = tab.getPos();
	    	this.itemRender.renderItemIntoGUI(stack, tabPos.x + 8, tabPos.y + 6);
		    this.itemRender.renderItemOverlays(this.fontRendererObj, stack, tabPos.x + 8, tabPos.y + 6);
		    RenderHelper.disableStandardItemLighting();
		    if(isPointInRegion(tab.getPos().x+1, tab.getPos().y, TabBase.WIDTH, TabBase.HEIGHT-1, mouseX, mouseY)) {
		    	flag = false;
		    	drawCreativeTabHoveringText(Tmo.proxy.translate(tab.getUnlocalizedName()), mouseX-this.getGuiLeft(), mouseY-this.getGuiTop());
		    }
	    }
	    
	    if(flag && JeiPlugin.jeiRuntime != null) {
	    	List<RecipeClickableAreaTMO> list = getRecipeClickableAreas();
	    	for(RecipeClickableAreaTMO r : list) {
	    		if(r.checkHover(mouseX, mouseY)) {
	    			drawCreativeTabHoveringText(Tmo.proxy.translate("jei.tooltip.show.recipes"), mouseX-this.getGuiLeft(), mouseY-this.getGuiTop());
	    		}
	    	}
	    }
	    
	}
	
	private List<RecipeClickableAreaTMO> getRecipeClickableAreas() {
    	List<RecipeClickableAreaTMO> l = new ArrayList<>();
    	if(selectedTabs.getLeft().hasRecipeClickableAreas()) {
    		l.addAll(Arrays.asList(selectedTabs.getLeft().getRecipeClickableAreas()));
    	}
    	if(selectedTabs.getRight().hasRecipeClickableAreas()) {
    		l.addAll(Arrays.asList(selectedTabs.getRight().getRecipeClickableAreas()));
    	}
    	return l;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		this.selectedTabs.left.mouseClicked(mouseX, mouseY, mouseButton);
		this.selectedTabs.right.mouseClicked(mouseX, mouseY, mouseButton);
		
		for(TabBase tab : tabs) {
			Point pos = tab.getPos();
			if(isPointInRegion(pos.x+1, pos.y, TabBase.WIDTH, TabBase.HEIGHT-1, mouseX, mouseY)) {
				if(selectedTabs.getLeft() != tab && selectedTabs.getRight() != tab) {
					this.mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1.0F, 1.0F);
					TabPos tabPos = tab.getTabPos();
					if(tabPos == TabPos.TOP_LEFT || tabPos == TabPos.TOP_RIGHT) {
						selectedTabs.setLeft(tab);
					} else {
						selectedTabs.setRight(tab);
					}
					selectedTabs = tab.forceTabConfig();
					this.container.selectedTabs = selectedTabs;
					this.container.hideSlots();

				}
			} else if(JeiPlugin.jeiRuntime != null) {
		    	List<RecipeClickableAreaTMO> list = getRecipeClickableAreas();
		    	for(RecipeClickableAreaTMO r : list) {
		    		if(r.checkHover(mouseX, mouseY)) {
		    			JeiPlugin.jeiRuntime.getRecipesGui().showCategories(r.getRecipeCategoryUids());
		    		}
		    	}
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void onGuiClosed() {
		this.selectedTabs.left.onGuiClosed();
		this.selectedTabs.right.onGuiClosed();
		super.onGuiClosed();
	}
	
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(!selectedTabs.left.keyTyped(typedChar, keyCode) && !selectedTabs.right.keyTyped(typedChar, keyCode)) {
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	private void addButtons() {
		try {
		List<GuiButton> leftButtons = this.selectedTabs.left.getButtonsList();
		List<GuiButton> rightButtons = this.selectedTabs.right.getButtonsList();
		
		
		if(leftButtons == null || leftButtons.isEmpty()) {
			selectedButtonsLeft.clear();
		}
		else if(!selectedButtonsLeft.containsAll(leftButtons)) {
			selectedButtonsLeft.clear();
			selectedButtonsLeft.addAll(leftButtons);
		}
		
		if(rightButtons == null || rightButtons.isEmpty()) {
			selectedButtonsRight.clear();
		}
		else if(!selectedButtonsRight.containsAll(rightButtons)) {
			selectedButtonsRight.clear();
			selectedButtonsRight.addAll(rightButtons);
		}
		
		this.buttonList.clear();
		this.buttonList.addAll(selectedButtonsLeft);
		this.buttonList.addAll(selectedButtonsRight);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		
		if(selectedButtonsLeft.contains(button)) {
			this.selectedTabs.left.onButtonClicked(button);
		}
		else if(selectedButtonsRight.contains(button)) {
			this.selectedTabs.right.onButtonClicked(button);
		}
	}
	
	public FontRenderer getFontRenderer() {
		return this.fontRendererObj;
	}
	
	public int getBottomPartPos() {
		return this.getGuiTop() + selectedTabs.getLeft().getTabTexture().dim.height;
	}
	
	protected void drawDisabledTabs() {
		for(TabBase tab : tabs) {
			if(tab != selectedTabs.getLeft() && tab != selectedTabs.getRight()) {
				this.drawTab(tab, false);
			}
		}
	}
	
	@Override
	public boolean isPointInRegion(int rectX, int rectY, int rectWidth, int rectHeight, int pointX, int pointY) {
		return super.isPointInRegion(rectX, rectY, rectWidth, rectHeight, pointX, pointY);
	}
	
	public MutablePair<TabBase, TabBase> getSelectedTabs() {
		return this.selectedTabs;
	}

	public List<TabBase> getRegisteredTabs() {
		return this.tabs;
	}
	
	public Minecraft getMinecraft() {
		return this.mc;
	}
	
}
