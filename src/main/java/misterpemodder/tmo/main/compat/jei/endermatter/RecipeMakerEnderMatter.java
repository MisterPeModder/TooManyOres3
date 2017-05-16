package misterpemodder.tmo.main.compat.jei.endermatter;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;
import net.minecraft.item.ItemStack;

public class RecipeMakerEnderMatter extends RecipeMaker<RecipeWrapperEnderMatter> {

	@Override
	public List<RecipeWrapperEnderMatter> makeRecipe(IIngredientRegistry ingredientRegistry) {
		List<RecipeWrapperEnderMatter> recipes = new ArrayList<>();

		for(ItemStack stack : TooManyOresAPI.ENDER_MATTER_ITEMS.keySet()) {
			recipes.add(new RecipeWrapperEnderMatter(stack, TooManyOresAPI.ENDER_MATTER_ITEMS.get(stack)));
		}
		
		return recipes;
	}

}
