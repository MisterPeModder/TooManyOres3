package misterpemodder.tmo.main.inventory.elements;

import misterpemodder.tmo.main.inventory.ISyncedContainerElement;
import misterpemodder.tmo.main.tileentity.TileEntityCraftingMachine;
import misterpemodder.tmo.main.utils.ResourceLocationTmo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerElementArrow implements ISyncedContainerElement {
	
	private int lastProgress;
	private boolean hasRecipe;
	private TileEntityCraftingMachine<?> te;
	
	public static final ResourceLocation TEXTURE = new ResourceLocationTmo("textures/gui/container/misc.png");
	public static final int WIDTH = 28;
	public static final int HEIGHT = 21;
	
	public ContainerElementArrow(TileEntityCraftingMachine<?> te) {
		this.te = te;
		this.lastProgress = te.getProgress();
		this.hasRecipe = te.getCurrentRecipe() != null;
	}
	
	@SideOnly(Side.CLIENT)
	public void drawArrow(int x, int y, boolean inverted) {
		GlStateManager.enableBlend();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);
		Gui.drawModalRectWithCustomSizedTexture(x, y, inverted? 0:58, 107, WIDTH, HEIGHT, 128, 128);
		
		if(lastProgress > 0) {
			if(inverted) {
				Gui.drawModalRectWithCustomSizedTexture(x+(WIDTH-lastProgress), y, 57-lastProgress, 107, lastProgress, HEIGHT, 128, 128);
			} else {
				Gui.drawModalRectWithCustomSizedTexture(x, y, 87, 107, lastProgress, HEIGHT, 128, 128);
			}
		}
		
		int t = (int) (Minecraft.getMinecraft().world.getTotalWorldTime() % 3);
		
		int offset = 3;
		if(inverted) {
			offset = 10;
			t = 3-t;
		}
		if(!hasRecipe) {
			t = 0;
		}
		Gui.drawModalRectWithCustomSizedTexture(x+offset, y+3, t*15+t, 91, 15, 5, 128, 128);
		Gui.drawModalRectWithCustomSizedTexture(x+offset, y+13, t*15 + t, 101, 15, 5, 128, 128);
		
		GlStateManager.disableBlend();
	}

	@Override
	public boolean shouldSendDataToClient() {
        int teProgress = 0;
        boolean recipe = false;
        if(te.getCurrentRecipe() != null) {
        	teProgress = (te.getProgress()*28)/te.getChangedCraftingTime(te.getCurrentRecipe());
        	recipe = true;
        }
        boolean b = this.lastProgress != teProgress || this.hasRecipe != recipe;
        this.lastProgress = teProgress;
        this.hasRecipe = recipe;
        return b;
	}

	@Override
	public NBTTagCompound writeData(NBTTagCompound data) {
		data.setInteger("progress", lastProgress);
		data.setBoolean("has_recipe", hasRecipe);
		return data;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void procData(NBTTagCompound data) {
		if(data.hasKey("progress") && data.hasKey("has_recipe")) {
			this.lastProgress = data.getInteger("progress");
			this.hasRecipe = data.getBoolean("has_recipe");
		}
	}

}
