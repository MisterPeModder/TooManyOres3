package misterpemodder.tmo.api.handler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.block.ISlimeBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * Handles tasks related to registration, such as recipes.
 */
public interface ITMORegistryHandler {
	/**
	 * Registers the ISlimeBlock and binds it to the passed block.
	 * Useful for registering ISlimeBlocks for blocks that are not in your own mod.
	 * @param slime
	 * @param block
	 */
	public void registerSlimeBlock(ISlimeBlock slime, Block block);
	
	/**
	 * Registers a strong piston behavior
	 */
	public void registerStrongPistonBehavior(IStrongPistonBehavior behavior);
	
	/**
	 * Adds a recipe to the fluid injector.
	 * 
	 * @param recipeId the id of this recipe, it should be unique to your mod. (example: "mymod:cool_recipe")
	 * @param fluidInput The fluid ingredient
	 * @param input	The item ingredient
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 * @param ignoreNBT indicates if the recipe should be sensible to NBT data. 
	 */
	public void registerInjectorRecipeInjection(@Nonnull String recipeId, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT);
	
	/**
	 * Adds a recipe to the fluid injector.
	 * This recipe will ignore NBT data.
	 * 
	 * @param recipeId the id of this recipe, it should be unique to your mod. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 */
	public void registerInjectorRecipeInjection(@Nonnull String recipeId, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime);

	/**
	 * Adds a recipe to the fluid injector.
	 * 
	 * @param recipeId the id of this recipe, it should be unique to your mod. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param fluidOutput The fluid product of this recipe
	 * @param output The item product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 * @param ignoreNBT indicates if the recipe should be sensible to NBT data. 
	 */
	public void registerInjectorRecipeExtraction(@Nonnull String recipeId, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT);
	
	/**
	 * Adds a recipe to the fluid injector.
	 * This recipe will ignore NBT data.
	 * 
	 * @param recipeId the id of this recipe, it should be unique to your mod. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param fluidOutput The fluid product of this recipe
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 */
	public void registerInjectorRecipeExtraction(@Nonnull String recipeId, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime);
}
