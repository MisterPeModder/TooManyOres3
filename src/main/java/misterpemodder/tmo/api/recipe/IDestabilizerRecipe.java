package misterpemodder.tmo.api.recipe;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public interface IDestabilizerRecipe extends IMachineRecipe<IDestabilizerRecipe> {
	
	int getEnderMaterNeeded();
	
	boolean isValid(ItemStack input);
	
	Pair<ItemStack, FluidStack> onFinish(ItemStack itemStackIn, @Nonnull FluidTank tank);

}
