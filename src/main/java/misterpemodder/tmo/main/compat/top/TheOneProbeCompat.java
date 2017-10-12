package misterpemodder.tmo.main.compat.top;

import com.google.common.base.Function;

import mcjty.theoneprobe.api.ITheOneProbe;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class TheOneProbeCompat {
	
	public static boolean topLoaded = false;
	public static final String TOP_TAG = "theoneprobe";
	
	public static void init() {
		if(TheOneProbeCompat.topLoaded) {
			TMORefs.LOGGER.info("Found The One Probe: Sending request for the ITheOneProbe instance...");
			FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", GetTheOneProbe.class.getName());
		} else {
			TMORefs.LOGGER.info("The One Probe not found: integration not loading");
		}
	}
	
    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			TMORefs.LOGGER.info("=> Registering: Titanium Chest probe config provider"); 
			theOneProbe.registerProbeConfigProvider(new ProbeConfigProviderTitaniumChest());
			return null;
		}
    	
    }
    
}
