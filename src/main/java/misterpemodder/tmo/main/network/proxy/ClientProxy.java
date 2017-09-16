package misterpemodder.tmo.main.network.proxy;

import misterpemodder.hc.main.network.proxy.ICommonProxy;
import misterpemodder.hc.main.utils.GuiHelper;
import misterpemodder.hc.main.utils.RegistryHelper;
import misterpemodder.tmo.main.TooManyOres;
import misterpemodder.tmo.main.blocks.redstone.BlockSpecialRedstoneWire;
import misterpemodder.tmo.main.client.render.TileEntityInjectorRenderer;
import misterpemodder.tmo.main.client.render.TileEntityStrongPistonRenderer;
import misterpemodder.tmo.main.client.render.TileEntityTitaniumAnvilRenderer;
import misterpemodder.tmo.main.client.render.TileEntityTitaniumChestRenderer;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.init.ModFluids;
import misterpemodder.tmo.main.init.ModItems.TheItems;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import misterpemodder.tmo.main.tileentity.TileEntityStrongPiston;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy implements ICommonProxy {
	
	public void preInit() {
		RegistryHelper.registerRenders(TheItems.values());
		RegistryHelper.registerRenders(TheBlocks.values());
		ModFluids.registerFluidBlocksRendering();
	}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(TooManyOres.instance, new GuiHelper());
		
		//TESRs
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTitaniumChest.class, new TileEntityTitaniumChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTitaniumAnvil.class, new TileEntityTitaniumAnvilRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStrongPiston.class, new TileEntityStrongPistonRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityInjector.class, new TileEntityInjectorRenderer());
		//TileEntityItemStackRenderer.instance = new TileEntityTitaniumChestInvRenderer();
	}
	
	@Override
	public void postInit() {
		BlockSpecialRedstoneWire.registerColorHandler();
	}
	
}
