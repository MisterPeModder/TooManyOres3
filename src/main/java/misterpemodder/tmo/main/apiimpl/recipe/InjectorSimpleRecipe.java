package misterpemodder.tmo.main.apiimpl.recipe;

import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class InjectorSimpleRecipe implements IInjectorRecipe{
	
	protected final String recipeId;
	public final int totalTime;
	public final ItemStack inputStack;
	public final ItemStack outputStack;
	public final boolean ignoreNBT;
	
	protected InjectorSimpleRecipe(String recipeId, ItemStack input, ItemStack output, int totalTime, boolean ignoreNBT) {
		this.recipeId = recipeId;
		this.totalTime = totalTime;
		this.inputStack = input;
		this.outputStack = output;
		this.ignoreNBT = ignoreNBT;
	}
	
	public abstract FluidStack getFluidIO();
	
	@Override
	public String getRecipeId() {
		return this.recipeId;
	}

	@Override
	public int getTotalTime() {
		return this.totalTime;
	}

}
