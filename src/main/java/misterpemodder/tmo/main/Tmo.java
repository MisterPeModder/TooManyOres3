/*
================================================================================/----------\=====
/------------------\================================/------------------\========|          |=====
|                  |===============================/                    \=======\-------   |=====
|                  |====/-----\==========/-----\===|                    |==============|   |=====
|                  |====|      \========/      |===|     /--------\     |=======/------/   |=====
\----\        /----/====|       \======/       |===|     |========|     |=======\------\   |=====
=====|        |=========|        \====/        |===|     |========|     |==============|   |=====
=====|        |=========|         \--/         |===|     |========|     |=======/------/   |=====
=====|        |=========|                      |===|     |========|     |=======|          |=====
=====|        |=========|     |\        /|     |===|     |========|     |=======\----------/=====
=====|        |=========|     |=\      /=|     |===|     \--------/     |========================
=====|        |=========|     |==\----/==|     |===|                    |========================
=====|        |=========|     |==========|     |===\                    /========================
=====\--------/=========\-----/==========\-----/====\------------------/=========================
=================================================================================================
*/


package misterpemodder.tmo.main;

import org.apache.logging.log4j.Logger;

import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
import misterpemodder.hc.main.AbstractMod;
import misterpemodder.hc.main.compat.craftingtweaks.CraftingTweaksCompat;
import misterpemodder.hc.main.network.HexianNetworkWrapper;
import misterpemodder.hc.main.network.packet.PacketHandler;
import misterpemodder.hc.main.network.proxy.ICommonProxy;
import misterpemodder.hc.main.utils.RegistryHelper;
import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.apiimpl.DefaultStrongPistonBehavior;
import misterpemodder.tmo.main.apiimpl.EnderMatterItems;
import misterpemodder.tmo.main.apiimpl.MethodHandler;
import misterpemodder.tmo.main.apiimpl.RegistryHandler;
import misterpemodder.tmo.main.apiimpl.SlimeBlock;
import misterpemodder.tmo.main.apiimpl.io.IOType;
import misterpemodder.tmo.main.capability.CapabilityFreezing;
import misterpemodder.tmo.main.client.gui.tabs.TMOButtonClickHandlers;
import misterpemodder.tmo.main.commands.CommandTMO;
import misterpemodder.tmo.main.compat.aa.ActAddCompat;
import misterpemodder.tmo.main.compat.top.ProbeConfigProviderTitaniumChest;
import misterpemodder.tmo.main.config.ConfigHandler;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.init.MachineRecipes;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModBrewing;
import misterpemodder.tmo.main.init.ModFluids;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.init.crafting.Crafting;
import misterpemodder.tmo.main.inventory.ContainerDestabilizer;
import misterpemodder.tmo.main.inventory.ContainerInjector;
import misterpemodder.tmo.main.inventory.ContainerTitaniumAnvil;
import misterpemodder.tmo.main.inventory.ContainerTitaniumChest;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.utils.TMORefs;
import misterpemodder.tmo.main.world.OreGen;
import net.minecraftforge.fluids.FluidRegistry;
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

@Mod(
		modid = TMORefs.MOD_ID,
		name = TMORefs.MOD_NAME,
		version = TMORefs.MOD_VERSION,
		dependencies = "required-after:hc@[1.1.0,)",
		acceptedMinecraftVersions = TMORefs.ACCEPTED_MC_VERSIONS,
		guiFactory = "misterpemodder.tmo.main.config.ConfigGuiFactory"
	)
public class Tmo extends AbstractMod {

	@Instance(TMORefs.MOD_ID)
	public static Tmo instance;

	@SidedProxy(modId = TMORefs.MOD_ID, clientSide = TMORefs.CLIENT_PROXY_CLASS, serverSide = TMORefs.SERVER_PROXY_CLASS)
	public static ICommonProxy proxy;
	
	static {
        FluidRegistry.enableUniversalBucket();
    }
	
	@Override
	public ICommonProxy getProxy() {
		return proxy;
	}

	@Override
	protected HexianNetworkWrapper createNetworkWrapper() {
		return new HexianNetworkWrapper(TMORefs.MOD_ID);
	}
	
	@Override
	public Logger getLogger() {
		return TMORefs.LOGGER;
	}

	@Mod.EventHandler
	public void preInitialization(FMLPreInitializationEvent event) {

		PacketHandler.registerPacketHandlers(PacketDataHandlers.values());
		
		new ConfigHandler(event.getSuggestedConfigurationFile());
		ModFluids.registerFluids();
		
		RegistryHandler.createRegistries();
		TooManyOresAPI.methodHandler = new MethodHandler();
		TooManyOresAPI.registryHandler = new RegistryHandler();
		
		IOType.registerDefaultIOTypes();
		
		CapabilityFreezing.register();
		GameRegistry.registerWorldGenerator(new OreGen(), 42);
		
		CraftingTweaksCompat.registerContainer(ContainerTitaniumAnvil.class, 46);
		CraftingTweaksCompat.registerContainer(ContainerTitaniumChest.class, 109);
		CraftingTweaksCompat.registerContainer(ContainerInjector.class, 44);
		CraftingTweaksCompat.registerContainer(ContainerDestabilizer.class, 44);
		
		TMOButtonClickHandlers.registerHandlers();
		
		super.preInitialization(event);
	}

	@Mod.EventHandler
	public void initialization(FMLInitializationEvent event) {
		
		super.initialization(event);
		
		TMORefs.actAddLoaded = Loader.isModLoaded(ActuallyAdditionsAPI.MOD_ID);
		TMORefs.topLoaded = Loader.isModLoaded("theoneprobe");
		TMORefs.baublesEnabled = ConfigValues.BoolValues.BAUBLES_COMPAT.currentValue;
		
		SlimeBlock.registerSlimeBlocksInternal();
		TooManyOresAPI.registryHandler.registerStrongPistonBehavior(new DefaultStrongPistonBehavior());
		EnderMatterItems.register();
		
		RegistryHelper.registerCreativeTabItems(TheItems.values());
		RegistryHelper.registerOreDict(TheItems.values());
		RegistryHelper.registerOreDict(TheBlocks.values());
		Crafting.registerRecipes();
		ModBrewing.registerBrewingRecipes();
		MachineRecipes.registerRecipes();
		ActAddCompat.init();
		ProbeConfigProviderTitaniumChest.init();

	}

	@Mod.EventHandler
	public void postInitialization(FMLPostInitializationEvent event) {
		super.postInitialization(event);
	}
	
	@EventHandler
	public void serverStating(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandTMO());
	}
	
}
