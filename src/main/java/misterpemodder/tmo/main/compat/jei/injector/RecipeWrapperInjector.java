package misterpemodder.tmo.main.compat.jei.injector;

import java.awt.Color;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipe;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeWrapperInjector extends BlankRecipeWrapper {

	public final ItemStack inputStack;
	public final ItemStack outputStack;
	public final FluidStack fluidIO;
	public final TransferMode transferMode;
	
	public RecipeWrapperInjector(InjectorSimpleRecipe recipe){
		this(recipe.inputStack, recipe.outputStack, recipe.getFluidIO(), recipe.getRecipeTransferType());
	}
	
	public RecipeWrapperInjector(ItemStack inputStack, ItemStack outputStack, FluidStack fluidIO, TransferMode transferMode) {
		this.inputStack = inputStack;
		this.outputStack = outputStack;
		this.fluidIO = fluidIO;
		this.transferMode = transferMode;
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
		
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocationTmo("textures/gui/container/injector_main.png"));
		Gui.drawModalRectWithCustomSizedTexture(64-RecipeCategoryInjector.X_OFFSET, 39-RecipeCategoryInjector.Y_OFFSET, transferMode == TransferMode.INJECTION? 58:0, 100, 28, 21, 256, 128);
		
		if(minecraft.world != null) {
			int p = (int) (minecraft.world.getTotalWorldTime()%29);
			if(p > 0) {
				if(transferMode == TransferMode.EXTRACTION) {
					Gui.drawModalRectWithCustomSizedTexture(64-RecipeCategoryInjector.X_OFFSET+(28-p), 39-RecipeCategoryInjector.Y_OFFSET, 57-p, 100, p, 21, 256, 128);
				} else {
					Gui.drawModalRectWithCustomSizedTexture(64-RecipeCategoryInjector.X_OFFSET, 39-RecipeCategoryInjector.Y_OFFSET, 87, 100, p, 21, 256, 128);
				}
			}
		}

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
