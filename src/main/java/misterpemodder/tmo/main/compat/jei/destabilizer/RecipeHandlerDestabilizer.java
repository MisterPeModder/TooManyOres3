package misterpemodder.tmo.main.compat.jei.destabilizer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeHandlerDestabilizer implements IRecipeHandler<RecipeWrapperDestabilizer> {

	@Override
	public Class<RecipeWrapperDestabilizer> getRecipeClass() {
		return RecipeWrapperDestabilizer.class;
	}

	@Override
	public String getRecipeCategoryUid(RecipeWrapperDestabilizer recipe) {
		return RecipeCategoryDestabilizer.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeWrapperDestabilizer recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(RecipeWrapperDestabilizer recipe) {
		return true;
	}

}
