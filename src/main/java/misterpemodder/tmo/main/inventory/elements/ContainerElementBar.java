package misterpemodder.tmo.main.inventory.elements;

import java.awt.Point;

import mezz.jei.api.gui.IDrawable;
import misterpemodder.tmo.main.inventory.ISyncedContainerElement;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mezz.jei.api.gui.IDrawable", modid = "jei")
public abstract class ContainerElementBar implements ISyncedContainerElement, IDrawable {
	
	public static final ResourceLocation TEXTURE = new ResourceLocationTmo("textures/gui/container/bars.png");
	public static final int BAR_WIDTH = 140;
	public static final int BAR_HEIGHT = 8;
	
	protected int barFillAmount = 0;
	protected boolean drawBackground = true;
	protected boolean drawForeground = true;
	
	//for use inside TMO Tabs
	protected ContainerElementBar() {}
	
	//for use as a JEI IDrawable
	protected ContainerElementBar(int fillPercent, boolean shouldDrawMinimum, boolean drawBackground, boolean drawForeground) {
		int i = (int) ((MathHelper.clamp(fillPercent, 0, 100)/100f) * (getBarSize()-2));
		this.barFillAmount = shouldDrawMinimum && i == 0? 1 : i; 
		
		this.drawBackground = drawBackground;
		this.drawForeground = drawForeground;
	}
	
	public void setFillPercent(int fillPercent, boolean shouldDrawMinimum) {
		int i = (int) ((MathHelper.clamp(fillPercent, 0, 100)/100f) * (getBarSize()-2));
		this.barFillAmount = shouldDrawMinimum && i == 0? 1 : i; 
	}
	
	@Override
	public int getWidth() {
		return BAR_WIDTH;
	}
	
	@Override
	public int getHeight() {
		return BAR_HEIGHT;
	}
	
	@Override
	public void draw(Minecraft minecraft) {
		this.draw(minecraft, 0, 0);
	}

	@Override
	public void draw(Minecraft minecraft, int xOffset, int yOffset) {
		this.drawBar(xOffset, yOffset, this.barFillAmount);
	}
	
	public void drawBar(int x, int y, int fillAmount) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		
		Point backgroundUV = getBackgroundUV();
		Point foregroundUV = getForegroundUV();
		
		if(drawBackground) Gui.drawModalRectWithCustomSizedTexture(x, y, backgroundUV.x, backgroundUV.y, BAR_WIDTH, BAR_HEIGHT, 256, 256);
		if(drawForeground) Gui.drawModalRectWithCustomSizedTexture(x, y, foregroundUV.x, foregroundUV.y, fillAmount, BAR_HEIGHT, 256, 256);
		
		GlStateManager.disableBlend();
	}
	
	protected abstract Point getBackgroundUV();
	
	protected abstract Point getForegroundUV();
	
	@Override
	public boolean shouldSendDataToClient() {
		return false;
	}
	
	@Override
	public void procData(NBTTagCompound data) {}
	
	@Override
	public NBTTagCompound writeData(NBTTagCompound data) {
		return new NBTTagCompound();
	}
	
	protected int getBarSize() {
		return BAR_WIDTH;
	}

}
