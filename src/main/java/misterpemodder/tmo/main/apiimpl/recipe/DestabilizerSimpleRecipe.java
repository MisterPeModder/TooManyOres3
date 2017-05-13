package misterpemodder.tmo.main.apiimpl.recipe;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class DestabilizerSimpleRecipe extends MachineRecipe<IDestabilizerRecipe> implements IDestabilizerRecipe {

	private final int enderMatterAmount;
	private final ItemStack itemInput;
	private final FluidStack fluidStack;
	private final int totalTime;

	public DestabilizerSimpleRecipe(ResourceLocation recipeId, int enderMatterAmount, ItemStack itemInput, FluidStack fluidStack, int totalTime) {
		super(recipeId);
		this.enderMatterAmount = enderMatterAmount;
		this.totalTime = totalTime;
		this.itemInput = itemInput;
		this.fluidStack = fluidStack;
	}

	@Override
	public int getTotalTime() {
		return this.totalTime;
	}

	@Override
	public int getEnderMaterNeeded() {
		return this.enderMatterAmount;
	}

	@Override
	public boolean isValid(ItemStack input) {
		return ItemStack.areItemsEqual(input, this.itemInput) && input.getCount() >= this.itemInput.getCount();
	}

	@Override
	public Pair<ItemStack, FluidStack> onFinish(ItemStack itemStackIn, FluidTank tank) {
		itemStackIn.shrink(this.itemInput.getCount());
		return Pair.of(itemStackIn, this.fluidStack.copy());
	}

}
