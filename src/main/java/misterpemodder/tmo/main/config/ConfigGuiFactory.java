package misterpemodder.tmo.main.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import misterpemodder.tmo.main.config.ConfigValues.ConfigCategories;
import misterpemodder.tmo.main.utils.TMOHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class ConfigGuiFactory implements IModGuiFactory {

	@Override
	public void initialize(Minecraft minecraftInstance) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return ConfigGui.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}
	
	public static class ConfigGui extends GuiConfig {
		
		public ConfigGui(GuiScreen parent) {

			super(parent, getConfigElements(), TMOHelper.MOD_ID, false, false, I18n.format("tmo.config.gui.mainTitle", new Object[]{}));

		}

		

		private static List<IConfigElement> getConfigElements() {
			List<IConfigElement> list = new ArrayList<IConfigElement>();
			
			for(ConfigCategories c : ConfigCategories.values()) {
		        ConfigHandler.config.setCategoryComment(c.name, c.desc);
		        list.add(new ConfigElement(ConfigHandler.config.getCategory(c.name)));
		    }
			return list;
		}
	}

}
