package misterpemodder.tmo.main.proxy;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import misterpemodder.tmo.main.client.render.TileEntityStrongPistonRenderer;
import misterpemodder.tmo.main.client.render.TileEntityTitaniumAnvilRenderer;
import misterpemodder.tmo.main.client.render.TileEntityTitaniumChestRenderer;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.tileentity.TileEntityStrongPiston;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy extends CommonProxy {
	
	public void preInit() {
		ModItems.registerRenders();
		ModBlocks.registerRenders();
	}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Tmo.instance, new GuiHandler());
		
		//TESRs
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTitaniumChest.class, new TileEntityTitaniumChestRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTitaniumAnvil.class, new TileEntityTitaniumAnvilRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityStrongPiston.class, new TileEntityStrongPistonRenderer());
		//TileEntityItemStackRenderer.instance = new TileEntityTitaniumChestInvRenderer();
	}
	
	@Override
	public void postInit() {}
	
	@Override
	public String translate(String translateKey, Object... params) {
		return I18n.format(translateKey, params);
	}
	
}
