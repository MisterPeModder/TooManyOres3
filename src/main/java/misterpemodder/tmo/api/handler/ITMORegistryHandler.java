package misterpemodder.tmo.api.handler;

import javax.annotation.Nullable;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.io.IIOType;
import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

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
	 * Registers an itemstack that should be considered as an ender matter item.
	 * Ender matter items can be used as fuel in the crystal destabilizer.
	 * 
	 * @param stack The itemstack to be registered.
	 * @param value How much ender matter this item is worth.
	 */
	public void registerEnderMatterItem(ItemStack stack, int value);
	
	/**
	 * Registers an IOType
	 * 
	 * @return the registered IOType, 
	 * if an IOType with the same id exists, it will return this one instead. 
	 */
	public IIOType registerIOType(IIOType type);
		
	/**
	 * Used to get the instance of the injector recipe registry.
	 * I recommand using the methods below instead of the registry itself to register your recipes.
	 * 
	 * @return the injector recipes registry
	 */
	public IForgeRegistry<IInjectorRecipe> getInjectorRecipesRegistry();
	
	/**
	 * Adds a recipe to the fluid injector.
	 * 
	 * @param id, the id of this recipe. it must be unique. (example: "mymod:cool_recipe")
	 * @param fluidInput The fluid ingredient
	 * @param input	The item ingredient
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 * @param ignoreNBT indicates if the recipe should be sensible to NBT data. 
	 */
	public void registerInjectorRecipeInjection(ResourceLocation id, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, int totalTime, boolean ignoreNBT);
	
	/**
	 * Adds a recipe to the fluid injector.
	 * This recipe will ignore NBT data.
	 * 
	 * @param id, the id of this recipe. it must be unique. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 */
	public void registerInjectorRecipeInjection(ResourceLocation id, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, int totalTime);

	/**
	 * Adds a recipe to the fluid injector.
	 * 
	 * @param id, the id of this recipe. it must be unique. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param fluidOutput The fluid product of this recipe
	 * @param output The item product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 * @param ignoreNBT indicates if the recipe should be sensible to NBT data. 
	 */
	public void registerInjectorRecipeExtraction(ResourceLocation id, ItemStack input, FluidStack fluidOutput, ItemStack output, int totalTime, boolean ignoreNBT);
	
	/**
	 * Adds a recipe to the fluid injector.
	 * This recipe will ignore NBT data.
	 * 
	 * @param id, the id of this recipe. it must be unique. (example: "mymod:cool_recipe")
	 * @param input	The item ingredient
	 * @param fluidOutput The fluid product of this recipe
	 * @param output The product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 */
	public void registerInjectorRecipeExtraction(ResourceLocation id, ItemStack input, FluidStack fluidOutput, ItemStack output, int totalTime);
	
	/**
	 * Used to get the instance of the crystal destabilizer recipe registry.
	 * I recommand using the method below instead of the registry itself to register your recipes.
	 * 
	 * @return the crystal destabilizer recipes registry
	 */
	public IForgeRegistry<IDestabilizerRecipe> getCrystalDestabilizerRecipesRegistry();
	
	/**
	 * Adds a recipe to the crystal destabilizer.
	 * 
	 * @param recipeId the id of this recipe, it must be unique. (example: "mymod:cool_recipe")
	 * @param enderMatterAmount the amount of ender matter needed for the recipe
	 * @param itemInput	The item ingredient
	 * @param fluidOutput The fluid product of this recipe
	 * @param totalTime The time (in ticks) that this recipe should take.
	 */
	public void registerCrystalDestabilizerRecipe(ResourceLocation id, int enderMatterAmount, ItemStack itemInput, FluidStack fluidOutput, int totalTime);
	
}
