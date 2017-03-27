package misterpemodder.tmo.main.compat.jei.injector;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipe;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;

public class InjectorRecipeMaker extends RecipeMaker<RecipeWrapperInjector> {

	@Override
	public List<RecipeWrapperInjector> makeRecipe(IIngredientRegistry ingredientRegistry) {
		List<RecipeWrapperInjector> recipes = new ArrayList<>();
		
		for(IInjectorRecipe ir : TooManyOresAPI.INJECTOR_RECIPES) {
			if(ir instanceof InjectorSimpleRecipe) {
				recipes.add(new RecipeWrapperInjector((InjectorSimpleRecipe)ir));
			}
		}
		
		return recipes;
	}

}
