package misterpemodder.tmo.proxy;

import misterpemodder.tmo.Tmo;
import misterpemodder.tmo.client.gui.GuiHandler;
import misterpemodder.tmo.client.render.TileEntityTitaniumChestInvRenderer;
import misterpemodder.tmo.client.render.TileEntityTitaniumChestRenderer;
import misterpemodder.tmo.main.init.ModBlocks;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ClientProxy implements CommonProxy {
	
	public void preInit() {
		ModItems.registerRenders();
		ModBlocks.registerRenders();
	}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Tmo.instance, new GuiHandler());
		
		//TESRs
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTitaniumChest.class, new TileEntityTitaniumChestRenderer());
		TileEntityItemStackRenderer.instance = new TileEntityTitaniumChestInvRenderer();
	}
	
	@Override
	public void postInit() {}
	
	@Override
	public String translate(String translateKey, Object... params) {
		return I18n.format(translateKey, params);
	}
	
}
