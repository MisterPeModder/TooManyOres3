package misterpemodder.tmo.main.init;


import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorFillDrainRecipe;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public final class MachineRecipes {
	
	public static void registerRecipes() {
		TooManyOresAPI.INJECTOR_RECIPES.add(new InjectorFillDrainRecipe(TransferMode.INJECTION));
		TooManyOresAPI.INJECTOR_RECIPES.add(new InjectorFillDrainRecipe(TransferMode.EXTRACTION));
		
		TooManyOresAPI.registryHandler.registerInjectorRecipeExtraction(TMORefs.PREFIX+"magma_to_lava", new ItemStack(Blocks.MAGMA), new FluidStack(FluidRegistry.LAVA, 100), new ItemStack(Blocks.NETHERRACK), 40);
		TooManyOresAPI.registryHandler.registerInjectorRecipeExtraction(TMORefs.PREFIX+"magma_cream_to_lava", new ItemStack(Items.MAGMA_CREAM), new FluidStack(FluidRegistry.LAVA, 25), new ItemStack(Items.SLIME_BALL), 40);
		
		if(!new ItemStack(Items.GLASS_BOTTLE).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			TooManyOresAPI.registryHandler.registerInjectorRecipeInjection(TMORefs.PREFIX+"fill_water_bottle", new FluidStack(FluidRegistry.WATER, 250), new ItemStack(Items.GLASS_BOTTLE), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 20);
			TooManyOresAPI.registryHandler.registerInjectorRecipeExtraction(TMORefs.PREFIX+"empty_water_bottle", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new FluidStack(FluidRegistry.WATER, 250), new ItemStack(Items.GLASS_BOTTLE), 20);
		}
		
	}
	
}
