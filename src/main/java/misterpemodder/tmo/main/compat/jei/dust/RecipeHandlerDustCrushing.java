package misterpemodder.tmo.main.compat.jei.dust;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class RecipeHandlerDustCrushing implements IRecipeHandler<RecipeWrapperDustCrushing> {
	
	@Override
	public Class<RecipeWrapperDustCrushing> getRecipeClass() {
		return RecipeWrapperDustCrushing.class;
	}

	@Override
	public String getRecipeCategoryUid(RecipeWrapperDustCrushing recipe) {
		return RecipeCategoryDustCrushing.UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(RecipeWrapperDustCrushing recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(RecipeWrapperDustCrushing recipe) {
		return true;
	}

}
