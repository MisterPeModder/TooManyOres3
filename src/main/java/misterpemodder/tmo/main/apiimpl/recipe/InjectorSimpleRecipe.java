package misterpemodder.tmo.main.apiimpl.recipe;

import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public abstract class InjectorSimpleRecipe extends MachineRecipe<IInjectorRecipe> implements IInjectorRecipe{
	
	public final int totalTime;
	public final ItemStack inputStack;
	public final ItemStack outputStack;
	public final boolean ignoreNBT;
	
	protected InjectorSimpleRecipe(ResourceLocation recipeId, ItemStack input, ItemStack output, int totalTime, boolean ignoreNBT) {
		super(recipeId);
		this.totalTime = totalTime;
		this.inputStack = input;
		this.outputStack = output;
		this.ignoreNBT = ignoreNBT;
	}
	
	public abstract FluidStack getFluidIO();

	@Override
	public int getTotalTime() {
		return this.totalTime;
	}

}
