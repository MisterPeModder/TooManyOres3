package misterpemodder.tmo.main.proxy;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy implements CommonProxy {
	public void preInit() {}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Tmo.instance, new GuiHandler());
	}
	
	@Override
	public void postInit() {}
	
	@Override
	public String translate(String translateKey, Object... params) {
		return I18n.translateToLocalFormatted(translateKey, params);
	}
}
