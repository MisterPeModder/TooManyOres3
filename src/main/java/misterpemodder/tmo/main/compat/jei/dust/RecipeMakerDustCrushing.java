package misterpemodder.tmo.main.compat.jei.dust;

import java.util.ArrayList;
import java.util.List;

import mezz.jei.api.ingredients.IIngredientRegistry;
import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.compat.jei.RecipeMaker;
import misterpemodder.tmo.main.init.crafting.Crafting;
import misterpemodder.tmo.main.init.crafting.RecipeDustCrushing;
import net.minecraft.item.ItemStack;

public class RecipeMakerDustCrushing extends RecipeMaker<RecipeWrapperDustCrushing> {

	@Override
	public List<RecipeWrapperDustCrushing> makeRecipe(IIngredientRegistry ingredientRegistry) {
		
		List<ItemStack> hammers = new ArrayList<>();
		for(ItemStack stack : ingredientRegistry.getIngredients(ItemStack.class)) {
			if(stack.getItem() instanceof IItemForgeHammer) {
				hammers.add(stack);
			}
		}
		
		List<RecipeWrapperDustCrushing> recipeWrappers = new ArrayList<>();
		List<RecipeDustCrushing> dustRecipes = Crafting.getCrushingRecipes();
		if(!hammers.isEmpty() && !dustRecipes.isEmpty()) {
			for(RecipeDustCrushing r : dustRecipes) {
				if(!r.ores.isEmpty() && !r.dusts.isEmpty()) {
					recipeWrappers.add(new RecipeWrapperDustCrushing(hammers, r.ores, r.dusts.get(0)));
				}
			}
		}
		
		return recipeWrappers;
	}

}
