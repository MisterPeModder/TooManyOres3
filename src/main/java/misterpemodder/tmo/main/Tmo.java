

/*
================================================================================/——————————\=====
/——————————————————\================================/——————————————————\========|          |=====
|                  |===============================/                    \=======\———————   |=====
|                  |====/—————\==========/—————\===|                    |==============|   |=====
|                  |====|      \========/      |===|     /————————\     |=======/——————/   |=====
\————\        /————/====|       \======/       |===|     |========|     |=======\——————\   |=====
=====|        |=========|        \====/        |===|     |========|     |==============|   |=====
=====|        |=========|         \——/         |===|     |========|     |=======/——————/   |=====
=====|        |=========|                      |===|     |========|     |=======|          |=====
=====|        |=========|     |\        /|     |===|     |========|     |=======\——————————/=====
=====|        |=========|     |=\      /=|     |===|     \————————/     |============/=\=========
=====|        |=========|     |==\————/==|     |===|                    |===========/===\========
=====|        |=========|     |==========|     |===\                    /======(c)2016-16 MPM====
=====\————————/=========\—————/==========\—————/====\——————————————————/============\===/========
=====================================================================================\-/=========
*/


package misterpemodder.tmo.main;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.apiimpl.DefaultStrongPistonBehavior;
import misterpemodder.tmo.main.apiimpl.MethodHandler;
import misterpemodder.tmo.main.apiimpl.RegistryHandler;
import misterpemodder.tmo.main.apiimpl.SlimeBlock;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest.TChestPistonBehavior;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.commands.CommandTMO;
import misterpemodder.tmo.main.compat.aa.ActAddCompat;
import misterpemodder.tmo.main.compat.top.ProbeConfigProviderTitaniumChest;
import misterpemodder.tmo.main.config.ConfigHandler;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.init.Crafting;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.proxy.CommonProxy;
import misterpemodder.tmo.main.utils.TMORefs;
import misterpemodder.tmo.main.world.OreGen;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = TMORefs.MOD_ID, name = TMORefs.MOD_NAME, version = TMORefs.MOD_VERSION, acceptedMinecraftVersions = TMORefs.ACCEPTED_MC_VERSIONS, guiFactory="misterpemodder.tmo.main.config.ConfigGuiFactory")
public class Tmo {

	@Instance(TMORefs.MOD_ID)
	public static Tmo instance;

	@SidedProxy(modId = TMORefs.MOD_ID, clientSide = TMORefs.CLIENT_PROXY_CLASS, serverSide = TMORefs.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	// Events

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		TMORefs.LOGGER.info("Pre-Init!");

		new ConfigHandler(event.getSuggestedConfigurationFile());
		TMOPacketHandler.init();
		PacketDataHandlers.registerHandlers();
		TooManyOresAPI.methodHandler = new MethodHandler();
		TooManyOresAPI.registryHandler = new RegistryHandler();
		CapabilityOwner.register();
		
		GameRegistry.registerWorldGenerator(new OreGen(), 42);

		proxy.preInit();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		TMORefs.LOGGER.info("Init!");
		SlimeBlock.registerSlimeBlocksInternal();
		TooManyOresAPI.registryHandler.registerStrongPistonBehavior(new DefaultStrongPistonBehavior());
		TooManyOresAPI.registryHandler.registerStrongPistonBehavior(new TChestPistonBehavior());
		TMORefs.actAddLoaded = Loader.isModLoaded(ActuallyAdditionsAPI.MOD_ID);
		TMORefs.topLoaded = Loader.isModLoaded("theoneprobe");
		TMORefs.baublesLoaded = Loader.isModLoaded("baubles");
		TMORefs.baublesEnabled = TMORefs.baublesLoaded && ConfigValues.BoolValues.BAUBLES_COMPAT.currentValue;
		ModItems.registerOreDict();
		ModBlocks.registerOreDict();
		Crafting.registerRecipes();
		ActAddCompat.init();
		ProbeConfigProviderTitaniumChest.init();
		
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TMORefs.LOGGER.info("Post-Init!");
		
		proxy.postInit();
	}
	
	@EventHandler
	public void serverStating(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTMO());
	}
}
