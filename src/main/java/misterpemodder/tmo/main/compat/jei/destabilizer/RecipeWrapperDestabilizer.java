package misterpemodder.tmo.main.compat.jei.destabilizer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeWrapperDestabilizer extends BlankRecipeWrapper {

	public final List<ItemStack> stacks;
	public final FluidStack fluid;
	public final int enderCost;
	
	
	public RecipeWrapperDestabilizer(ItemStack stack, FluidStack fluid, int enderCost) {
		this(Collections.singletonList(stack), fluid, enderCost);
	}
	
	public RecipeWrapperDestabilizer(List<ItemStack> stacks, FluidStack fluid, int enderCost) {
		this.stacks = stacks;
		this.fluid = fluid;
		this.enderCost = enderCost;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<ItemStack>> inputList = new ArrayList<>();
		List<ItemStack> enderMatterItems = new ArrayList<>();
		TooManyOresAPI.ENDER_MATTER_ITEMS.keySet().forEach(stack -> enderMatterItems.add(stack));
		inputList.add(enderMatterItems);
		inputList.add(stacks);
		
		ingredients.setInputLists(ItemStack.class, inputList);
		ingredients.setOutput(FluidStack.class, fluid);
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(RecipeCategoryDestabilizer.LOCATION);
		
		int p = (enderCost*140)/TileEntityDestabilizer.MAX_ENDER_MATTER;
		Gui.drawModalRectWithCustomSizedTexture(11-RecipeCategoryDestabilizer.X_OFFSET, 82-RecipeCategoryDestabilizer.Y_OFFSET, 72, 100, p<=0? 2:p, 8, 256, 128);
	
		String str = Tmo.proxy.translate("gui.jei.category.destabilizer.cost")+": ";
		FontRenderer fr = minecraft.fontRendererObj;
		
		int x = 11-RecipeCategoryDestabilizer.X_OFFSET;
		int y = 15-RecipeCategoryDestabilizer.Y_OFFSET;
		
		fr.drawString(str, x, y, 0);
		fr.drawString(String.valueOf(enderCost), x+fr.getStringWidth(str), y, Color.CYAN.getRGB());
	}

}
