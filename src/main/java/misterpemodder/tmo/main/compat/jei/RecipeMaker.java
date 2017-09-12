package misterpemodder.tmo.main.compat.jei;

import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import mezz.jei.api.recipe.IRecipeWrapper;

@Deprecated
public abstract class RecipeMaker<T extends IRecipeWrapper> {
	
	public RecipeMaker(){}
	
	public abstract List<T> makeRecipe(IIngredientRegistry ingredientRegistry);
	
}
