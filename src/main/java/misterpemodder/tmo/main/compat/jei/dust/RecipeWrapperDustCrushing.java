package misterpemodder.tmo.main.compat.jei.dust;

import java.util.Arrays;
import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class RecipeWrapperDustCrushing extends BlankRecipeWrapper {
	
	private final List<ItemStack> hammers;
	private final List<ItemStack> oreInputs;
	private final ItemStack dustOuput;
	
	public RecipeWrapperDustCrushing(List<ItemStack> hammers, List<ItemStack> oreInputs, ItemStack dustOuput) {
		this.hammers = hammers;
		this.oreInputs = oreInputs;
		this.dustOuput = dustOuput;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, Arrays.asList(this.hammers, oreInputs));
		ingredients.setOutput(ItemStack.class, dustOuput);
	}

}
