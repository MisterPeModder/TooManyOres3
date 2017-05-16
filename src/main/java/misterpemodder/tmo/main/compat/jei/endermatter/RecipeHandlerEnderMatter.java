package misterpemodder.tmo.main.compat.jei.endermatter;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeHandlerEnderMatter implements IRecipeHandler<RecipeWrapperEnderMatter> {

	@Override
	public Class<RecipeWrapperEnderMatter> getRecipeClass() {
		return RecipeWrapperEnderMatter.class;
	}

	@Override
	public String getRecipeCategoryUid(RecipeWrapperEnderMatter recipe) {
		return RecipeCategoryEnderMatter.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeWrapperEnderMatter recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(RecipeWrapperEnderMatter recipe) {
		return true;
	}

}
