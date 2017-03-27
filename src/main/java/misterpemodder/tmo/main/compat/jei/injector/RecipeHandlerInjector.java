package misterpemodder.tmo.main.compat.jei.injector;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeHandlerInjector implements IRecipeHandler<RecipeWrapperInjector> {

	@Override
	public Class<RecipeWrapperInjector> getRecipeClass() {
		return RecipeWrapperInjector.class;
	}

	@Override
	public String getRecipeCategoryUid(RecipeWrapperInjector recipe) {
		return RecipeCategoryInjector.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeWrapperInjector recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(RecipeWrapperInjector recipe) {
		return true;
	}

}
