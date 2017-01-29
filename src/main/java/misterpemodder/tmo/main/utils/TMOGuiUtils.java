package misterpemodder.tmo.main.utils;

import misterpemodder.tmo.main.client.gui.GuiContainerTitaniumChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

@Deprecated
public final class TMOGuiUtils {
	
	public static final String PLAYER_INV_PATH = "textures/gui/container/player_inventory.png";
	
	public static void drawPlayerInv(GuiContainer container, int x, int y) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		container.mc.getTextureManager().bindTexture(new ResourceLocation(TMOHelper.PREFIX + TMOGuiUtils.PLAYER_INV_PATH));
		container.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 212, 100, 256, 128);
	}
}


