package misterpemodder.tmo.main.config;

import java.io.File;

import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {
	
	public static Configuration config;
	
	public ConfigHandler(File file) {
		
		MinecraftForge.EVENT_BUS.register(this);
		config = new Configuration(file);
		config.load();
		updateConfig();
		
	}
	
	private static void updateConfig() {
		ConfigValues.setValues(config);
		
		if(config.hasChanged()) {
			config.save();
		}
		TMORefs.baublesEnabled = TMORefs.baublesLoaded && ConfigValues.BoolValues.BAUBLES_COMPAT.currentValue;
	}
	
	@SubscribeEvent
	public void onConfigChangeEvent(OnConfigChangedEvent event) {
		if(event.getModID().equalsIgnoreCase(TMORefs.MOD_ID)) updateConfig();
	}
	
}
