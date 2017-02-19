package misterpemodder.tmo.main.utils;

import java.awt.Color;

import net.minecraft.client.gui.FontRenderer;

public class StringUtils {
	
	public static void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y) {
        fontRenderer.drawStringWithShadow(text, (float)(x - fontRenderer.getStringWidth(text) / 2), (float)y, Color.WHITE.getRGB());
    }
	
}
