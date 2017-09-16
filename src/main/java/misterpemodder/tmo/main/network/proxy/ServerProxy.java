package misterpemodder.tmo.main.network.proxy;

import misterpemodder.hc.main.network.proxy.ICommonProxy;
import misterpemodder.hc.main.utils.GuiHelper;
import misterpemodder.tmo.main.TooManyOres;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class ServerProxy implements ICommonProxy {
	public void preInit() {}
	
	public void init() {
		NetworkRegistry.INSTANCE.registerGuiHandler(TooManyOres.instance, new GuiHelper());
	}
	
	@Override
	public void postInit() {}
	
}
