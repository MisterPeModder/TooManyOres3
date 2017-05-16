package misterpemodder.tmo.main.apiimpl.recipe;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class DestabilizerToolRecipe extends MachineRecipe<IDestabilizerRecipe> implements IDestabilizerRecipe{

	private final int enderMatterAmount;
	private final ToolMaterial material;
	private final FluidStack fluidStack;
	private final int totalTime;

	public DestabilizerToolRecipe(ResourceLocation recipeId, int enderMatterAmount, ToolMaterial material, FluidStack fluidStack, int totalTime) {
		super(recipeId);
		this.enderMatterAmount = enderMatterAmount;
		this.totalTime = totalTime;
		this.material = material;
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
		return input.getItem().getIsRepairable(input, material.getRepairItemStack());
	}

	@Override
	public Pair<ItemStack, FluidStack> onFinish(ItemStack itemStackIn, FluidTank tank) {
		itemStackIn.shrink(1);
		return Pair.of(itemStackIn, this.fluidStack.copy());
	}

}
