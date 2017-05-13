package misterpemodder.tmo.main.init;


import static misterpemodder.tmo.api.TooManyOresAPI.registryHandler;

import misterpemodder.tmo.api.recipe.IInjectorRecipe.TransferMode;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorFillDrainRecipe;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModFluids.TheFluids;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.items.ItemVariant.GemVariant;
import misterpemodder.tmo.main.utils.ItemStackUtils;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
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
		registryHandler.getInjectorRecipesRegistry().register(new InjectorFillDrainRecipe(TransferMode.INJECTION));
		registryHandler.getInjectorRecipesRegistry().register(new InjectorFillDrainRecipe(TransferMode.EXTRACTION));
		
		registryHandler.registerInjectorRecipeExtraction(new ResourceLocationTmo("magma_to_lava"), new ItemStack(Blocks.MAGMA), new FluidStack(FluidRegistry.LAVA, 100), new ItemStack(Blocks.NETHERRACK), 40);
		registryHandler.registerInjectorRecipeExtraction(new ResourceLocationTmo("magma_cream_to_lava"), new ItemStack(Items.MAGMA_CREAM), new FluidStack(FluidRegistry.LAVA, 25), new ItemStack(Items.SLIME_BALL), 15);
		registryHandler.registerInjectorRecipeInjection(new ResourceLocationTmo("fiery_exploder"), new FluidStack(TheFluids.IGNUM_FUEL.getFluid(),500), new ItemStack(TheBlocks.EXPLODER.getBlock(),1,0), new ItemStack(TheBlocks.EXPLODER.getBlock(),1,1), 45);
		
		if(!new ItemStack(Items.GLASS_BOTTLE).hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)) {
			registryHandler.registerInjectorRecipeInjection(new ResourceLocationTmo("fill_water_bottle"), new FluidStack(FluidRegistry.WATER, 250), new ItemStack(Items.GLASS_BOTTLE), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), 20);
			registryHandler.registerInjectorRecipeExtraction(new ResourceLocationTmo("empty_water_bottle"), PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.WATER), new FluidStack(FluidRegistry.WATER, 250), new ItemStack(Items.GLASS_BOTTLE), 20);
		}
		
		registryHandler.registerCrystalDestabilizerRecipe(new ResourceLocationTmo("ignum_fuel"), 10, ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.IGNUM_GEM), new FluidStack(TheFluids.IGNUM_FUEL.getFluid(), 250), 40);
		registryHandler.registerCrystalDestabilizerRecipe(new ResourceLocationTmo("frozium"), 10, ItemStackUtils.newVariantStack(TheItems.GEM, GemVariant.FROZIUM_GEM), new FluidStack(TheFluids.FROZIUM_FUEL.getFluid(), 250), 40);
		
	}
	
}
