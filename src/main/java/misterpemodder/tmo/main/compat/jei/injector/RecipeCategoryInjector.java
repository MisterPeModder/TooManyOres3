package misterpemodder.tmo.main.compat.jei.injector;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.inventory.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RecipeCategoryInjector extends BlankRecipeCategory<RecipeWrapperInjector> {
	
	private final String localizedName;
	public static final String UID = "tmo.injector.normal";
	
	public static final int X_OFFSET = 8;
	public static final int Y_OFFSET = 7;
	public static final ResourceLocation LOCATION = new ResourceLocationTmo("textures/gui/container/injector_main.png");
	
	private final IDrawable background;
	private final IDrawable fluidGauge;
	
	private IDrawableStatic arrowStaticInjection;
	private IDrawableAnimated arrowAnimatedInjection;
	private IDrawableStatic arrowStaticExtraction;
	private IDrawableAnimated arrowAnimatedExtraction;
	private RecipeWrapperInjector currentRecipe;
	
	public RecipeCategoryInjector(IGuiHelper guiHelper) {
		this.localizedName = Tmo.proxy.translate("gui.jei.category.injector.normal");
		this.background = guiHelper.createDrawable(LOCATION, X_OFFSET, Y_OFFSET, 165, 85, 256, 128);
		this.fluidGauge = guiHelper.createDrawable(ContainerElementTank.TANK_TEXTURE, 0, 0, 40, 80, 128, 128);
		this.arrowStaticInjection = guiHelper.createDrawable(LOCATION, 58, 100, 28, 21, 256, 128);
		this.arrowAnimatedInjection = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(LOCATION, 87, 100, 28, 21, 256, 128), 20, StartDirection.LEFT, false);
		this.arrowStaticExtraction = guiHelper.createDrawable(LOCATION, 0, 100, 28, 21, 256, 128);
		this.arrowAnimatedExtraction = guiHelper.createAnimatedDrawable(guiHelper.createDrawable(LOCATION, 29, 100, 28, 21, 256, 128), 20, StartDirection.RIGHT, false);
	}

	@Override
	public String getUid() {
		return UID;
	}

	@Override
	public String getTitle() {
		return this.localizedName;
	}

	@Override
	public IDrawable getBackground() {
		return this.background;
	}
	
	@Override
	public void drawExtras(Minecraft minecraft) {
		if(currentRecipe != null) {
			
			if(currentRecipe.transferMode == TransferMode.INJECTION) {
				arrowStaticInjection.draw(minecraft, 64-X_OFFSET, 39-Y_OFFSET);
				arrowAnimatedInjection.draw(minecraft, 64-X_OFFSET, 39-Y_OFFSET);
			} else {
				arrowStaticExtraction.draw(minecraft, 64-X_OFFSET, 39-Y_OFFSET);
				arrowAnimatedExtraction.draw(minecraft, 64-X_OFFSET, 39-Y_OFFSET);
			}

		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperInjector recipeWrapper, IIngredients ingredients) {
		
		this.currentRecipe = recipeWrapper;
		
		recipeLayout.setRecipeTransferButton(157-X_OFFSET, 74-Y_OFFSET);
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		
		guiItemStacks.init(0, true, 106-X_OFFSET, 41-Y_OFFSET);
		guiItemStacks.init(1, false, 154-X_OFFSET, 41-Y_OFFSET);
		guiFluidStacks.init(0, false, 11-X_OFFSET, 10-Y_OFFSET, 40, 80, TileEntityInjector.CAPACITY, true, fluidGauge);
		
		List<List<ItemStack>> itemInputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> itemInput = itemInputs.isEmpty()? null : itemInputs.get(0);
		
		List<List<ItemStack>> itemOutputs = ingredients.getOutputs(ItemStack.class);
		List<ItemStack> itemOutput = itemOutputs.isEmpty()? null : itemOutputs.get(0);
		
		List<List<FluidStack>> fluidIOs;
		
		if(recipeWrapper.transferMode == TransferMode.INJECTION) {
			fluidIOs = ingredients.getInputs(FluidStack.class);
		} else {
			fluidIOs = ingredients.getOutputs(FluidStack.class);
		}
		
		List<FluidStack> fluidIO = fluidIOs.isEmpty()? null : fluidIOs.get(0);
		
		guiItemStacks.set(0, itemInput);
		guiItemStacks.set(1, itemOutput);
		if(fluidIO != null && !fluidIO.isEmpty()) {
			guiFluidStacks.set(0, fluidIO);
		}
	}

}
