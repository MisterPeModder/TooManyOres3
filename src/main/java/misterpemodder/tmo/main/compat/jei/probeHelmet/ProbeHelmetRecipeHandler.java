package misterpemodder.tmo.main.compat.jei.probehelmet;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

public class ProbeHelmetRecipeHandler implements IRecipeHandler<ProbeHelmetRecipeWrapper> {

	@Override
	public Class<ProbeHelmetRecipeWrapper> getRecipeClass() {
		return ProbeHelmetRecipeWrapper.class;
	}

	@Override
	public String getRecipeCategoryUid(ProbeHelmetRecipeWrapper recipe) {
		return VanillaRecipeCategoryUid.CRAFTING;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ProbeHelmetRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(ProbeHelmetRecipeWrapper recipe) {
		return true;
	}
	
}
