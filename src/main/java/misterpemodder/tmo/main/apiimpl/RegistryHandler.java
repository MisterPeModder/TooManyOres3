package misterpemodder.tmo.main.apiimpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.api.handler.ITMORegistryHandler;
import misterpemodder.tmo.api.recipe.IDestabilizerRecipe;
import misterpemodder.tmo.api.recipe.IInjectorRecipe;
import misterpemodder.tmo.api.recipe.IMachineRecipe;
import misterpemodder.tmo.main.apiimpl.recipe.DestabilizerSimpleRecipe;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipeExtract;
import misterpemodder.tmo.main.apiimpl.recipe.InjectorSimpleRecipeInject;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.RegistryBuilder;

public class RegistryHandler implements ITMORegistryHandler {
	
	public static IForgeRegistry<IInjectorRecipe> injectorRecipesRegistry;
	public static IForgeRegistry<IDestabilizerRecipe> crystalDestabilizerRecipesRegistry;
	
	static {
		injectorRecipesRegistry = new RegistryBuilder<IInjectorRecipe>().setType(IInjectorRecipe.class).setName(new ResourceLocationTmo("injector_recipes")).setIDRange(0, Integer.MAX_VALUE-1).create();
		crystalDestabilizerRecipesRegistry = new RegistryBuilder<IDestabilizerRecipe>().setType(IDestabilizerRecipe.class).setName(new ResourceLocationTmo("crystal_destabilizer_recipes")).setIDRange(0, Integer.MAX_VALUE-1).create();
	}

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
	public void registerEnderMatterItem(ItemStack stack, int value) {
		if(TooManyOresAPI.methodHandler.isEnderMatterItem(stack)) {
			TMORefs.LOGGER.info("The ender matter item "+stack.toString()+" has already been registered!");
		} else {
			TooManyOresAPI.ENDER_MATTER_ITEMS.put(stack, value);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> IIOType<T> registerIOType(IIOType<T> type) {
		for(IIOType<?> t : TooManyOresAPI.IO_TYPES) {
			if(t.getID().equals(type.getID()) && t.getCapabilityInstance().equals(type.getCapabilityInstance())) {
				return (IIOType<T>) t;
			}
		}
		TooManyOresAPI.IO_TYPES.add(type);
		return type;
	}
	
	private <T extends IMachineRecipe<T>> void registerRecipe(T recipe, IForgeRegistry<T> registry) {
		if(recipe.getTotalTime() <= 0) {
			TMORefs.LOGGER.info("Injector injection recipe "+recipe.getRegistryName()+": totalTime must be above 0!");
			return;
		}
		registry.register(recipe);
	}
	
	public IForgeRegistry<IInjectorRecipe> getInjectorRecipesRegistry() {
		return injectorRecipesRegistry;
	}
	
	@Override
	public void registerInjectorRecipeInjection(ResourceLocation id, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime) {
		this.registerInjectorRecipeInjection(id, fluidInput, input, output, totalTime, true);
	}
	
	@Override
	public void registerInjectorRecipeInjection(ResourceLocation id, @Nullable FluidStack fluidInput, ItemStack input, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT) {
		registerRecipe(new InjectorSimpleRecipeInject(id, fluidInput, input, output, totalTime, ignoreNBT), injectorRecipesRegistry);
	}
	
	@Override
	public void registerInjectorRecipeExtraction(ResourceLocation id, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime) {
		this.registerInjectorRecipeExtraction(id, input, fluidOutput, output, totalTime, true);
	}
	
	@Override
	public void registerInjectorRecipeExtraction(ResourceLocation id, ItemStack input, FluidStack fluidOutput, ItemStack output, @Nonnull int totalTime, boolean ignoreNBT) {
		registerRecipe(new InjectorSimpleRecipeExtract(id, input, fluidOutput, output, totalTime, ignoreNBT), injectorRecipesRegistry);
	}
	
	@Override
	public IForgeRegistry<IDestabilizerRecipe> getCrystalDestabilizerRecipesRegistry() {
		return crystalDestabilizerRecipesRegistry;
	}
	
	@Override
	public void registerCrystalDestabilizerRecipe(ResourceLocation id, int enderMatterAmount, ItemStack itemInput, FluidStack fluidOutput, int totalTime) {
		registerRecipe(new DestabilizerSimpleRecipe(id, enderMatterAmount, itemInput, fluidOutput, totalTime), crystalDestabilizerRecipesRegistry);
	}

}
