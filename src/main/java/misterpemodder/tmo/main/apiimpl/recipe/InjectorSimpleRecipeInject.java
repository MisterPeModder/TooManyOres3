package misterpemodder.tmo.main.apiimpl.recipe;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class InjectorSimpleRecipeInject extends InjectorSimpleRecipe {
	
	public final FluidStack fluidInput;
	
	public InjectorSimpleRecipeInject(ResourceLocation recipeId, FluidStack fluidInput, ItemStack input, ItemStack output, int totalTime, boolean ignoreNBT) {
		super(recipeId, input, output, totalTime, ignoreNBT);
		this.fluidInput = fluidInput;
	}
	
	@Override
	public FluidStack getFluidIO() {
		return this.fluidInput;
	}
	
	@Override
	public TransferMode getRecipeTransferType() {
		return TransferMode.INJECTION;
	}

	@Override
	public boolean isValid(FluidTank fluidTank, ItemStack stack) {
		if(stack.getItem() == inputStack.getItem() && stack.getMetadata() == inputStack.getMetadata()) {
			if(stack.getCount() >= inputStack.getCount() && (ignoreNBT || stack.getTagCompound().equals(inputStack.getTagCompound()))) {
				FluidStack fs = fluidTank.getFluid();
				if(fs == null) {
					return fluidInput == null;
				}
				return fs.containsFluid(fluidInput);
			}
		}
		return false;
	}

	@Override
	public Triple<FluidStack, ItemStack,ItemStack> onFinish(FluidTank fluidTankIn, ItemStack itemStackIn, ItemStack itemStackOut) {
		fluidTankIn.drain(fluidInput, true);
		itemStackIn.shrink(inputStack.getCount());
		return Triple.of(fluidTankIn.getFluid(), itemStackIn, outputStack.copy());
	}

}
