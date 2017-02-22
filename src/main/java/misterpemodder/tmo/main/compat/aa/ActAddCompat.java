package misterpemodder.tmo.main.compat.aa;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.IngotVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class ActAddCompat {
	
	public static void init() {
		if(TMORefs.actAddLoaded) {
			TMORefs.LOGGER.info("Found Actually Additions: Loading integration...");
			
			registerMiningLensOres();
			registerEmpowererRecipes();
			registerReconstructorRecipes();
		} else {
			TMORefs.LOGGER.info("Actually Additions not found: integration not loading");
		}
	}
	
	private static void registerMiningLensOres() {
		if(ConfigValues.BoolValues.ACT_ADD_COMPAT_MINING.currentValue) {
			TMORefs.LOGGER.info("=> Registering: mining lens ores");
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreTitanium", 60);
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreGoldAncient", 20);
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreDarkanium", 30);
			ActuallyAdditionsAPI.addMiningLensNetherOre("oreDarkaniumNether", 30);
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreFrozium", 1500);
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreIgnum", 1500);
			ActuallyAdditionsAPI.addMiningLensNetherOre("oreIgnumNether", 1500);
			ActuallyAdditionsAPI.addMiningLensStoneOre("oreCarbon", 1200);
		} else {
			TMORefs.LOGGER.info("=> Disabled: Mining lens custom ores");
		}
	}
	
	private static void registerEmpowererRecipes() {
		if(ConfigValues.BoolValues.ACT_ADD_COMPAT_EMPOWERER.currentValue) {
			TMORefs.LOGGER.info("=> Registering: empowerer recipes");
			ItemStack lapis = new ItemStack(Items.DYE, 1, 4);
			ActuallyAdditionsAPI.addEmpowererRecipe(ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT_POOR), ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.TITANIUM_INGOT), lapis, lapis, lapis, new ItemStack(Items.DIAMOND), 2000, 50, new float[]{0F, 51F/255F, 108F/255F});
		} else {
			TMORefs.LOGGER.info("=> Disabled: empowerer recipes");
		}
	}
	
	private static void registerReconstructorRecipes() {
		if(ConfigValues.BoolValues.ACT_ADD_COMPAT_EMPOWERER.currentValue) {
			TMORefs.LOGGER.info("=> Registering: reconstructor recipes");
			ActuallyAdditionsAPI.addReconstructorLensConversionRecipe(ItemStackUtils.newVariantStack(TheItems.INGOT, IngotVariant.CARBON_INGOT), new ItemStack(Items.COAL, 3), 5000);
		} else {
			TMORefs.LOGGER.info("=> Disabled: reconstructor recipes");
		}
	}
	
}
