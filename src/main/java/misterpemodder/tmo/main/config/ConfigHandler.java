package misterpemodder.tmo.main.config;

import java.io.File;

import misterpemodder.tmo.utils.TMOHelper;
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
	}
	
	@SubscribeEvent
	public void onConfigChangeEvent(OnConfigChangedEvent event) {
		if(event.getModID().equalsIgnoreCase(TMOHelper.MOD_ID)) updateConfig();
	}
	
}
