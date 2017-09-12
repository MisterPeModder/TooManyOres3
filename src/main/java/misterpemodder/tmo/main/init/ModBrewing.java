package misterpemodder.tmo.main.init;

import misterpemodder.hc.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.init.ModPotions.ThePotionTypes;
import misterpemodder.tmo.main.items.TMOItemVariants.GemVariant;
import misterpemodder.tmo.main.utils.TMOItemStackUtils;
import net.minecraft.init.PotionTypes;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public final class ModBrewing {
	
	public static void registerBrewingRecipes() {
		BrewingRecipeRegistry.addRecipe(ItemStackUtils.makePotion(PotionTypes.AWKWARD), ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.FROZIUM_GEM), TMOItemStackUtils.makePotion(ThePotionTypes.FREEZING));
		
		//PotionHelper.registerPotionItemConversion(Items.POTIONITEM, new ItemPredicateInstance(Items.GUNPOWDER), Items.SPLASH_POTION);
	}

}
