package misterpemodder.tmo.main.compat.jei.destabilizer;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import misterpemodder.tmo.main.apiimpl.recipe.DestabilizerSimpleRecipe;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public class RecipeMakerDestabilizer extends RecipeMaker<RecipeWrapperDestabilizer> {

	@Override
	public List<RecipeWrapperDestabilizer> makeRecipe(IIngredientRegistry ingredientRegistry) {
		List<RecipeWrapperDestabilizer> recipes = new ArrayList<>();
		
		for(IDestabilizerRecipe dr : TooManyOresAPI.registryHandler.getCrystalDestabilizerRecipesRegistry().getValues()) {
			if(dr instanceof DestabilizerSimpleRecipe) {
				DestabilizerSimpleRecipe r = (DestabilizerSimpleRecipe) dr;
				recipes.add(new RecipeWrapperDestabilizer(r.getItemInput(), r.getFluidOuput(), r.getEnderMaterNeeded()));
			}
			else {
				for(ItemStack s : ingredientRegistry.getIngredients(ItemStack.class)) {
					ItemStack stack = s.copy();
					stack.setCount(stack.getMaxStackSize());
					if(dr.isValid(stack)) {
						recipes.add(new RecipeWrapperDestabilizer(s, dr.onFinish(stack, new FluidTank(TileEntityDestabilizer.CAPACITY)).getRight(), dr.getEnderMaterNeeded()));
					}
				}
			}
		}
		
		return recipes;
	}

}
