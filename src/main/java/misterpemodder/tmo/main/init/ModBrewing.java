package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.init.ModPotions.ThePotionTypes;
import misterpemodder.tmo.main.items.ItemVariant.GemVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import net.minecraft.init.PotionTypes;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public final class ModBrewing {
	
	public static void registerBrewingRecipes() {
		BrewingRecipeRegistry.addRecipe(ItemStackUtils.makePotion(PotionTypes.AWKWARD), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.FROZIUM_GEM), ItemStackUtils.makePotion(ThePotionTypes.FREEZING));
		
		//PotionHelper.registerPotionItemConversion(Items.POTIONITEM, new ItemPredicateInstance(Items.GUNPOWDER), Items.SPLASH_POTION);
	}

}
