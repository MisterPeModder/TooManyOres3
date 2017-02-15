package misterpemodder.tmo.main.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import misterpemodder.tmo.main.TMOTab;
import misterpemodder.tmo.main.Tmo;
import net.minecraft.creativetab.CreativeTabs;

public class TMORefs {
	public static final String MOD_ID = "tmo";
	public static final String MOD_NAME = "Too Many Ores 3";
	public static final String MOD_VERSION = "1.0.0";
	public static final String ACCEPTED_MC_VERSIONS = "[1.11.2,)";
	
	public static final String CLIENT_PROXY_CLASS = "misterpemodder.tmo.main.proxy.ClientProxy";
	public static final String SERVER_PROXY_CLASS = "misterpemodder.tmo.main.proxy.ServerProxy";
	
	public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
	
	public static CreativeTabs TMO_TAB = new TMOTab("TMO");
	
	public static final String PREFIX = TMORefs.MOD_ID+":";
	public static final String DEFAULT_ITEM_NAME = TMORefs.MOD_ID+".missingNo.name";
	
	public static final String JEI_DESC_UNLOC_NAME = ".jeidesc";
	
	public static boolean ActAddLoaded = false;
	public static boolean topLoaded = false;
	public static final String TOP_TAG = "theoneprobe";
	
}
