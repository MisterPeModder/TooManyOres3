package misterpemodder.tmo.main.apiimpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.handler.ITMORegistryHandler;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipeExtract;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipeInject;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RegistryHandler implements ITMORegistryHandler {

	@Override
	public void registerSlimeBlock(ISlimeBlock slime, Block block) {
		if(TooManyOresAPI.SLIME_BLOCKS.containsKey(block)) {
			TMORefs.LOGGER.info(block.getRegistryName()+" has already been registered has an ISlimeBlock!");
		} else {
			TooManyOresAPI.SLIME_BLOCKS.put(block, slime);
		}
	}
	
	@Override
	public void registerStrongPistonBehavior(IStrongPistonBehavior behavior) {
		if(TooManyOresAPI.STRONG_PISTON_BEHAVIORS.contains(behavior)) {
			TMORefs.LOGGER.info("The behavior "+behavior.toString()+" has already been registered!");
		} else {
			TooManyOresAPI.STRONG_PISTON_BEHAVIORS.add(behavior);
		}
	}
	
	@Override
	public void registerInjectorRecipeInjection(@Nonnull String recipeId, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime) {
		this.registerInjectorRecipeInjection(recipeId, fluidInput, input, output, totalTime, true);
	}
	
	@Override
	public void registerInjectorRecipeInjection(@Nonnull String recipeId, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT) {
		
		if(totalTime <= 0) {
			TMORefs.LOGGER.info("Injector injection recipe "+recipeId+": totalTime must be above 0!");
			return;
		}
		
		for(IInjectorRecipe r : TooManyOresAPI.INJECTOR_RECIPES) {
			if(recipeId.equals(r.getRecipeId())) {
				TMORefs.LOGGER.info("The injector injection recipe "+recipeId+" has already been registered!");
				return;
			}
		}
		
		try {
			TooManyOresAPI.INJECTOR_RECIPES.add(new InjectorSimpleRecipeInject(recipeId, fluidInput, input, output, totalTime, ignoreNBT));
			TMORefs.LOGGER.info("Registered injector injection recipe "+"\""+recipeId+"\"");
		} catch(Exception e) {
			TMORefs.LOGGER.info("An error occured when registering "+recipeId+" as an injector recipe!");
		}
		
	}
	
	@Override
	public void registerInjectorRecipeExtraction(@Nonnull String recipeId, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime) {
		this.registerInjectorRecipeExtraction(recipeId, input, fluidOutput, output, totalTime, true);
	}
	
	@Override
	public void registerInjectorRecipeExtraction(@Nonnull String recipeId, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT) {
		
		if(totalTime <= 0) {
			TMORefs.LOGGER.info("Injector extraction recipe "+recipeId+": totalTime must be above 0!");
			return;
		}
		
		for(IInjectorRecipe r : TooManyOresAPI.INJECTOR_RECIPES) {
			if(recipeId.equals(r.getRecipeId())) {
				TMORefs.LOGGER.info("The injector extraction recipe "+recipeId+" has already been registered!");
				return;
			}
		}
		
		try {
			TooManyOresAPI.INJECTOR_RECIPES.add(new InjectorSimpleRecipeExtract(recipeId, input, fluidOutput, output, totalTime, ignoreNBT));
			TMORefs.LOGGER.info("Registered injector extraction recipe "+"\""+recipeId+"\"");
		} catch(Exception e) {
			TMORefs.LOGGER.info("An error occured when registering "+recipeId+" as an injector recipe!");
		}
		
	}

}
