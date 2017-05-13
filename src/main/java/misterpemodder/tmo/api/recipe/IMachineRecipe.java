package misterpemodder.tmo.api.recipe;

import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public interface IMachineRecipe<V> extends IForgeRegistryEntry<V> {
	
	int getTotalTime();
	
}
