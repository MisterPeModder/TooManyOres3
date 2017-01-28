

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


package misterpemodder.tmo;

import misterpemodder.tmo.main.commands.CommandInfo;
import misterpemodder.tmo.main.config.ConfigHandler;
import misterpemodder.tmo.main.init.Crafting;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.world.OreGen;
import misterpemodder.tmo.network.PacketDataHandlers;
import misterpemodder.tmo.network.TMOPacketHandler;
import misterpemodder.tmo.proxy.CommonProxy;
import misterpemodder.tmo.utils.TMOHelper;
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

@Mod(modid = TMOHelper.MOD_ID, name = TMOHelper.MOD_NAME, version = TMOHelper.MOD_VERSION, acceptedMinecraftVersions = TMOHelper.ACCEPTED_MC_VERSIONS, guiFactory="misterpemodder.tmo.main.config.ConfigGuiFactory")
public class Tmo {

	@Instance(TMOHelper.MOD_ID)
	public static Tmo instance;

	@SidedProxy(modId = TMOHelper.MOD_ID, clientSide = TMOHelper.CLIENT_PROXY_CLASS, serverSide = TMOHelper.SERVER_PROXY_CLASS)
	public static CommonProxy proxy;

	// Events

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		TMOHelper.LOGGER.info("Pre-Init!");

		new ConfigHandler(event.getSuggestedConfigurationFile());
		TMOPacketHandler.init();
		PacketDataHandlers.registerHandlers();
		ModItems.register();
		ModBlocks.register();
		GameRegistry.registerWorldGenerator(new OreGen(), 5141);

		proxy.preInit();
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		TMOHelper.LOGGER.info("Init!");
		
		TMOHelper.topLoaded = Loader.isModLoaded("theoneprobe");
		
		ModItems.registerOreDict();
		ModBlocks.registerOreDict();
		Crafting.registerRecipes();
		
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		TMOHelper.LOGGER.info("Post-Init!");
		
		proxy.postInit();
	}
	
	@EventHandler
	public void serverStating(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandInfo());
	}
}
