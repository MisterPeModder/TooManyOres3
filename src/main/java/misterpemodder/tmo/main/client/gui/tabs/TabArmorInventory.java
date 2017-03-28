package misterpemodder.tmo.main.client.gui.tabs;

import java.awt.Dimension;
import java.awt.Point;

import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.ContainerBase;
import misterpemodder.tmo.main.client.gui.RecipeClickableAreaTMO;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TabArmorInventory<C extends ContainerBase<TE>, TE extends TileEntityContainerBase> extends TabBase{
	
	public TabArmorInventory() {
		super(TabPos.BOTTOM_RIGHT);
	}
	
	@Override
	public TabID getTabID() {
		return TabID.ARMOR_INV;
	}
	
	@Override
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		guiContainer.getFontRenderer().drawString(Tmo.proxy.translate(getUnlocalizedName()), 10, guiContainer.getBottomPartPos()-guiContainer.getGuiTop()+5, 4210752);
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		int i = guiContainer.getGuiLeft();
	    int j = guiContainer.getGuiTop();
	    GuiInventory.drawEntityOnScreen(i + 35, j + 78 + guiContainer.container.BPART_OFFSET, 30, (float)(i + 35) - mouseX, (float)(j + 78 + guiContainer.container.BPART_OFFSET - 50) - mouseY, guiContainer.mc.player);
	}
	
	public boolean shouldDisplaySlot(IHidable slot) {
		if(slot instanceof SlotItemHandler) {
			IItemHandler h = ((SlotHidable)slot).getItemHandler();
			boolean flag1 = h == guiContainer.container.getPlayerOffandInv() || h == guiContainer.container.getPlayerArmorInv();
			boolean flag2 = h == guiContainer.container.baublesInv;
			return flag1 || flag2;
		}
		else if(slot instanceof Slot){
			IInventory i = ((Slot)slot).inventory;
			return i == guiContainer.container.craftResult || i == guiContainer.container.craftMatrix;
		} else {
			return false;
		}
	}

	@Override
	public String getUnlocalizedName() {
		return "gui.tab.playerArmorInv.name";
	}

	@Override
	public ItemStack getItemStack() {
		return new ItemStack(TheItems.TITANIUM_CHESTPLATE.getItem());
	}

	@Override
	public TabTexture getTabTexture() {
		String str = TMORefs.baublesEnabled? "textures/gui/container/armor_inventory_baubles.png" : "textures/gui/container/armor_inventory.png";
		return new TabTexture(DEFAULT_TAB_LOCATION, new Point(0,56), new Point(32, 56), new ResourceLocationTmo(str), new Dimension(212, 100));
	}
	
	@Override
	public boolean hasRecipeClickableAreas() {
		return true;
	}
	
	@Override
	public RecipeClickableAreaTMO[] getRecipeClickableAreas() {
		return new RecipeClickableAreaTMO[]{new RecipeClickableAreaTMO(guiContainer.getBottomPartPos()+72, guiContainer.getBottomPartPos()+90, guiContainer.getGuiLeft()+156, guiContainer.getGuiLeft()+172, VanillaRecipeCategoryUid.CRAFTING)};
	}
}
