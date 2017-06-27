package misterpemodder.tmo.main.compat.jei;

import static misterpemodder.tmo.main.inventory.elements.ContainerElementArrow.HEIGHT;
import static misterpemodder.tmo.main.inventory.elements.ContainerElementArrow.WIDTH;

import mezz.jei.api.gui.IDrawable;
import misterpemodder.tmo.main.inventory.elements.ContainerElementArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class DrawableArrow implements IDrawable {
	
	private boolean inverted = false;
	
	public void setInverted(boolean inverted) {
		this.inverted = inverted;
	}

	@Override
	public int getWidth() {
		return ContainerElementArrow.WIDTH;
	}

	@Override
	public int getHeight() {
		return ContainerElementArrow.HEIGHT;
	}

	@Override
	public void draw(Minecraft minecraft) {
		this.draw(minecraft, 0, 0);
	}

	@Override
	public void draw(Minecraft minecraft, int x, int y) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(ContainerElementArrow.TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(x, y, inverted? 0:58, 107, WIDTH, HEIGHT, 128, 128);
		
		int p=0;
		int t=0;
		if(Minecraft.getMinecraft().world != null) {
			Long time = Minecraft.getMinecraft().world.getTotalWorldTime();
			p = (int) (time % 28);
			t = (int) (time % 3);
		}
		
		if(p > 0) {
			if(inverted) {
				Gui.drawModalRectWithCustomSizedTexture(x+(WIDTH-p), y, 57-p, 107, p, HEIGHT, 128, 128);
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 87, 107, p, HEIGHT, 128, 128);
			}
		}
		
		int offset = 3;
		if(inverted) {
			offset = 10;
			t = 3-t;
		}
		Gui.drawModalRectWithCustomSizedTexture(x+offset, y+3, t*15+t, 91, 15, 5, 128, 128);
		Gui.drawModalRectWithCustomSizedTexture(x+offset, y+13, t*15 + t, 101, 15, 5, 128, 128);
		
		GlStateManager.disableBlend();
	}

}
