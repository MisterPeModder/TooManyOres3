package misterpemodder.tmo.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.api.handler.ITMOMethodHandler;
import misterpemodder.tmo.api.handler.ITMORegistryHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;

public final class TooManyOresAPI {
	
	public static final String MOD_ID = "tmo";
	public static final String API_ID = MOD_ID + "_api";
	
	public static final String API_VERSION = "1.0.0";
	
	/**
	 * Used to register things like custom recipes or slime blocks
	 * Initialized in the preInit phase of TMO.
	 * <p><b>===THIS FIELD MUST NOT BE CHANGED OR OVERRIDEN!===
	 */
	public static ITMORegistryHandler registryHandler;
	
	/**
	 * Provides some methods to deal with the mod.
	 * Initialized in the preInit phase of TMO.
	 * <p><b>===THIS FIELD MUST NOT BE CHANGED OR OVERRIDEN!===
	 */
	public static ITMOMethodHandler methodHandler;
	
	/**
	 * Default IIType for items.
	 * <p><b>===THIS FIELD MUST NOT BE CHANGED OR OVERRIDEN!===
	 */
	public static IIOType<IItemHandler> itemIoType;
	/**
	 * Default IIType for fluids.
	 * <p><b>===THIS FIELD MUST NOT BE CHANGED OR OVERRIDEN!===
	 */
	public static IIOType<IFluidHandler> fluidIoType;
	/**
	 * Default IIType for energy.
	 * <p><b>===THIS FIELD MUST NOT BE CHANGED OR OVERRIDEN!===
	 */
	public static IIOType<IEnergyStorage> energyIoType;

	public static final Map<Block, ISlimeBlock> SLIME_BLOCKS = new HashMap<>();
	public static final List<IStrongPistonBehavior> STRONG_PISTON_BEHAVIORS = new ArrayList<>();
	public static final Map<ItemStack, Integer> ENDER_MATTER_ITEMS = new HashMap<>();
	public static final List<IIOType<?>> IO_TYPES = new ArrayList<>();
	
}
