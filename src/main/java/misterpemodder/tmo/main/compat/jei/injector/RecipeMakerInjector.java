package misterpemodder.tmo.main.compat.jei.injector;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipe;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class RecipeMakerInjector extends RecipeMaker<RecipeWrapperInjector> {

	@Override
	public List<RecipeWrapperInjector> makeRecipe(IIngredientRegistry ingredientRegistry) {
		List<RecipeWrapperInjector> recipes = new ArrayList<>();
		
		for(IInjectorRecipe ir : TooManyOresAPI.registryHandler.getInjectorRecipesRegistry().getValues()) {
			if(ir instanceof InjectorSimpleRecipe) {
				recipes.add(new RecipeWrapperInjector((InjectorSimpleRecipe)ir));
			}
			else {
				for(ItemStack s : ingredientRegistry.getIngredients(ItemStack.class)) {
					ItemStack stack = s.copy();
					stack.setCount(stack.getMaxStackSize());
					FluidTank tank = new FluidTank(TileEntityInjector.CAPACITY);
					
					if(ir.getRecipeTransferType() == TransferMode.EXTRACTION) {
						if(ir.isValid(tank, stack)) {
							Triple<FluidStack, ItemStack, ItemStack> t = ir.onFinish(tank, stack, ItemStack.EMPTY);
							recipes.add(new RecipeWrapperInjector(s, t.getRight(), t.getLeft(), ir.getRecipeTransferType()));
						}
					} else {
						for(FluidStack fs : ingredientRegistry.getIngredients(FluidStack.class)) {
							FluidStack fluidStack = fs.copy();
							fluidStack.amount = TileEntityInjector.CAPACITY;
							tank.fill(fluidStack, true);
							if(ir.isValid(tank, stack)) {
								Triple<FluidStack, ItemStack, ItemStack> t = ir.onFinish(tank, stack, ItemStack.EMPTY);
								recipes.add(new RecipeWrapperInjector(s, t.getRight(), t.getLeft(), ir.getRecipeTransferType()));
							}
						}
					}
					
				}
			}
		}
		
		return recipes;
	}

}
