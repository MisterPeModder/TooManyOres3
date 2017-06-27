package misterpemodder.tmo.main.compat.jei.injector;

import java.awt.Color;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipe;
import misterpemodder.tmo.main.compat.jei.DrawableArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeWrapperInjector extends BlankRecipeWrapper {

	public final ItemStack inputStack;
	public final ItemStack outputStack;
	public final FluidStack fluidIO;
	public final TransferMode transferMode;
	public final DrawableArrow arrow;
	
	public RecipeWrapperInjector(InjectorSimpleRecipe recipe){
		this(recipe.inputStack, recipe.outputStack, recipe.getFluidIO(), recipe.getRecipeTransferType());
	}
	
	public RecipeWrapperInjector(ItemStack inputStack, ItemStack outputStack, FluidStack fluidIO, TransferMode transferMode) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.fluidIO = fluidIO;
		this.transferMode = transferMode;
		this.arrow = new DrawableArrow();
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, inputStack.copy());
		ingredients.setOutput(ItemStack.class, outputStack.copy());
		if(fluidIO != null) {
			if(transferMode == TransferMode.INJECTION) {
				ingredients.setInput(FluidStack.class, fluidIO.copy());
			} else {
				ingredients.setOutput(FluidStack.class, fluidIO.copy());
			}
		}
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		
		arrow.setInverted(transferMode == TransferMode.EXTRACTION);
		arrow.draw(minecraft, 64-RecipeCategoryInjector.X_OFFSET, 39-RecipeCategoryInjector.Y_OFFSET);

		String str1 = Tmo.proxy.translate("gui.jei.category.injector.mode")+": ";
		String str2 = Tmo.proxy.translate("gui.jei.category.injector.mode."+(transferMode == TransferMode.INJECTION? "injection":"extraction"));
		int color = (transferMode == TransferMode.INJECTION?Color.CYAN:Color.ORANGE).getRGB();
			
		int x = 56-RecipeCategoryInjector.X_OFFSET;
		int y = 19-RecipeCategoryInjector.Y_OFFSET;
		int y2 = 65-RecipeCategoryInjector.Y_OFFSET;
			
		FontRenderer fr = minecraft.fontRendererObj;
		fr.drawString(str1, x, y, Color.black.getRGB());
		fr.drawString(str2, x+fr.getStringWidth(str1), y, color);
		
		if(fluidIO != null) {
			fr.drawString(fluidIO.getLocalizedName()+", "+fluidIO.amount+"mB", x, y2, Color.black.getRGB());
		}
	}

}
