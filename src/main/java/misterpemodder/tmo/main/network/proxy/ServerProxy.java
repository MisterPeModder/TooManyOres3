package misterpemodder.tmo.main.network.proxy;

import misterpemodder.hc.main.network.proxy.ICommonProxy;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy implements ICommonProxy {
	public void preInit() {}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(Tmo.instance, new GuiHandler());
	}
	
	@Override
	public void postInit() {}
	
}
