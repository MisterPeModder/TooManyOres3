package misterpemodder.tmo.main.compat.jei.destabilizer;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.compat.jei.DrawableArrow;
import misterpemodder.tmo.main.inventory.elements.ContainerElementEnderMatterBar;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class RecipeCategoryDestabilizer extends BlankRecipeCategory<RecipeWrapperDestabilizer> {
	
	private final String localizedName;
	public static final String UID = "tmo.destabilizer";

	public static final int X_OFFSET = 11;
	public static final int Y_OFFSET = 5;
	public final static ResourceLocation LOCATION = new ResourceLocationTmo("textures/gui/container/destabilizer_main.png");

	private final IDrawable background;
	private final IDrawable background_left;
	private final IDrawable tank;
	private final IDrawable fluidGauge;
	private final DrawableArrow arrow;
	
	private final ContainerElementEnderMatterBar bar;
	
	private final ITickTimer timer;
	
	public RecipeCategoryDestabilizer(IGuiHelper helper) {
		this.localizedName = Tmo.proxy.translate("gui.jei.category.destabilizer");
		
		this.tank = helper.createDrawable(LOCATION, 160, 8, 42, 82, 256, 128);
		this.fluidGauge = helper.createDrawable(ContainerElementTank.TANK_TEXTURE, 0, 0, 40, 80, 128, 128);
		this.background_left = helper.createDrawable(LOCATION, X_OFFSET, Y_OFFSET, 141, 88, 256, 128);
		this.background = helper.createBlankDrawable(182, 88);
		this.arrow = new DrawableArrow();
		this.bar = new ContainerElementEnderMatterBar(0, false, true, true);
		this.timer = helper.createTickTimer(100, 100, false);
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
		this.background_left.draw(minecraft, 0, 0);
		this.tank.draw(minecraft, 154-X_OFFSET, 8-Y_OFFSET);
		this.arrow.draw(minecraft, 115-X_OFFSET, 39-Y_OFFSET);
		this.bar.setFillPercent(this.timer.getValue(), false);
		this.bar.draw(minecraft, 11-X_OFFSET, 82-Y_OFFSET);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RecipeWrapperDestabilizer recipeWrapper,IIngredients ingredients) {
		recipeLayout.setRecipeTransferButton(138-X_OFFSET, 67-Y_OFFSET);
		
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
		IGuiFluidStackGroup guiFluidStacks = recipeLayout.getFluidStacks();
		
		guiItemStacks.init(0, true, 15-X_OFFSET, 41-Y_OFFSET);
		guiItemStacks.init(1, true, 77-X_OFFSET, 41-Y_OFFSET);
		guiFluidStacks.init(0, false, 155-X_OFFSET, 9-Y_OFFSET, 40, 80, TileEntityDestabilizer.CAPACITY, true, fluidGauge);
		
		List<List<ItemStack>> itemInputs = ingredients.getInputs(ItemStack.class);
		List<ItemStack> enderInput = itemInputs.isEmpty()? null : itemInputs.get(0);
		List<ItemStack> itemInput = itemInputs.isEmpty()? null : itemInputs.get(1);
		
		List<List<FluidStack>> fluidOutputs = ingredients.getOutputs(FluidStack.class);
		List<FluidStack> fluidOutput = fluidOutputs.isEmpty()? null : fluidOutputs.get(0);
		
		guiItemStacks.set(0, enderInput);
		guiItemStacks.set(1, itemInput);
		if(fluidOutput != null && !fluidOutput.isEmpty()) {
			guiFluidStacks.set(0, fluidOutput);
		}
		
	}

}
