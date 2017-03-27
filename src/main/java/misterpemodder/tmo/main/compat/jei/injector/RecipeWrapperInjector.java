package misterpemodder.tmo.main.compat.jei.injector;

import java.awt.Color;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeWrapperInjector extends BlankRecipeWrapper {
	
	public final InjectorSimpleRecipe recipe;
	
	public RecipeWrapperInjector(InjectorSimpleRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, recipe.inputStack.copy());
		ingredients.setOutput(ItemStack.class, recipe.outputStack.copy());
		if(recipe.getFluidIO() != null) {
			if(recipe.getRecipeTransferType() == TransferMode.INJECTION) {
				ingredients.setInput(FluidStack.class, recipe.getFluidIO().copy());
			} else {
				ingredients.setOutput(FluidStack.class, recipe.getFluidIO().copy());
			}
		}
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		String str1 = Tmo.proxy.translate("gui.jei.category.injector.mode")+": ";
		String str2 = Tmo.proxy.translate("gui.jei.category.injector.mode."+(recipe.getRecipeTransferType() == TransferMode.INJECTION? "injection":"extraction"));
		int color = (recipe.getRecipeTransferType() == TransferMode.INJECTION?Color.CYAN:Color.ORANGE).getRGB();
			
		int x = 56-RecipeCategoryInjector.X_OFFSET;
		int y = 19-RecipeCategoryInjector.Y_OFFSET;
		int y2 = 73-RecipeCategoryInjector.Y_OFFSET;
			
		FontRenderer fr = minecraft.fontRendererObj;
		fr.drawString(str1, x, y, Color.black.getRGB());
		fr.drawString(str2, x+fr.getStringWidth(str1), y, color);
		FluidStack fluidIO = recipe.getFluidIO();
		if(fluidIO != null) {
			fr.drawString(fluidIO.getLocalizedName()+", "+fluidIO.amount+"mB", x, y2, Color.black.getRGB());
		}
	}

}
