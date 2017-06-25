package misterpemodder.tmo.main.apiimpl.recipe;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class InjectorSimpleRecipeExtract extends InjectorSimpleRecipe {
	
	public final FluidStack fluidOutput;
	
	public InjectorSimpleRecipeExtract(ResourceLocation recipeId, ItemStack input, FluidStack fluidOutput, ItemStack output, int totalTime, boolean ignoreNBT) {
		super(recipeId, input, output, totalTime, ignoreNBT);
		this.fluidOutput = fluidOutput;
	}
	
	@Override
	public FluidStack getFluidIO() {
		return this.fluidOutput;
	}
	
	@Override
	public TransferMode getRecipeTransferType() {
		return TransferMode.EXTRACTION;
	}

	@Override
	public boolean isValid(FluidTank fluidTank, ItemStack stack) {
		if(stack.getItem() == inputStack.getItem() && stack.getMetadata() == inputStack.getMetadata()) {
			if(stack.getCount() >= inputStack.getCount() && (ignoreNBT || stack.getTagCompound().equals(inputStack.getTagCompound()))) {
				FluidStack fs = fluidTank.getFluid();
				if(fs == null || fs.amount <= 0) {
					return true;
				}
				return fs.isFluidEqual(fluidOutput) && fluidTank.getCapacity() - fs.amount >= fluidOutput.amount;
			}
		}
		return false;
	}

	@Override
	public Triple<FluidStack, ItemStack,ItemStack> onFinish(FluidTank fluidTankIn, ItemStack itemStackIn, ItemStack itemStackOut) {
		fluidTankIn.fill(fluidOutput,true);
		itemStackIn.shrink(inputStack.getCount());
		return Triple.of(fluidTankIn.getFluid(), itemStackIn, outputStack.copy());
	}
	
}