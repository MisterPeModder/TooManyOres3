package misterpemodder.tmo.main.utils;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class StringUtils {
	
	public static void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y) {
        fontRenderer.drawStringWithShadow(text, x - fontRenderer.getStringWidth(text) / 2, y, Color.WHITE.getRGB());
    }
	
	public static List<String> parseTooltip(String line) {
		
		List<String> expandedLines = Arrays.asList(line.split("\\\\n"));
		
		List<String> list = new ArrayList<>();
		FontRenderer font = Minecraft.getMinecraft().fontRendererObj;
		for(String l : expandedLines) {
			if(font.getStringWidth(l)>200) {
				list.addAll(font.listFormattedStringToWidth(l, 250));
			} else {
				list.add(l);
			}
		}
		
		return list;
	}
}
