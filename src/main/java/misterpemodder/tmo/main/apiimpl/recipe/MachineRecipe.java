package misterpemodder.tmo.main.apiimpl.recipe;

import misterpemodder.tmo.api.recipe.IMachineRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class MachineRecipe<T  extends IMachineRecipe<T>> extends IForgeRegistryEntry.Impl<T> {
	
	public MachineRecipe(ResourceLocation id) {
		this.setRegistryName(id);
	}

}
