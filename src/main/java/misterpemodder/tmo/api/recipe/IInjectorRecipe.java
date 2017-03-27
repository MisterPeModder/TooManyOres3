package misterpemodder.tmo.api.recipe;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Triple;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public interface IInjectorRecipe {

	public static enum TransferMode {
		INJECTION, EXTRACTION;
	}
	
	TransferMode getRecipeTransferType();
	
	String getRecipeId();
	
	int getTotalTime();
	
	boolean isValid(@Nonnull FluidTank fluidTank, ItemStack stack);
	
	Triple<FluidStack, ItemStack,ItemStack> onFinish(@Nonnull FluidTank fluidTankIn, ItemStack itemStackIn, ItemStack itemStackOut);
	
}
