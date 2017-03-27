package misterpemodder.tmo.main.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class RenderTank {
	
	//partially taken from com.enderio.core.client.render
	public static void renderTankInGui(FluidStack stack, int capacity, double x, double y, double width, double height) {
		
		if(stack == null || stack.getFluid() == null || stack.amount <= 0) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		TextureAtlasSprite fluidTexture;
		ResourceLocation loc = stack.getFluid().getStill(stack);
		if(loc == null) fluidTexture = getTextureMissingSprite();
		
		final TextureAtlasSprite textureEntry = mc.getTextureMapBlocks().getTextureExtry(loc.toString());
		if(textureEntry != null) {
			fluidTexture = textureEntry;
		} else {
			fluidTexture = getTextureMissingSprite();
		}
		
		mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		
		int renderAmount = (int) Math.max(Math.min(height, stack.amount * height / capacity), 1);
	    int posY = (int) (y + height - renderAmount);
		
		int color = stack.getFluid().getColor(stack);
	    GL11.glColor3ub((byte) (color >> 16 & 0xFF), (byte) (color >> 8 & 0xFF), (byte) (color & 0xFF));

	    GlStateManager.enableBlend();
	    for (int i = 0; i < width; i += 16) {
	      for (int j = 0; j < renderAmount; j += 16) {
	        int drawWidth = (int) Math.min(width - i, 16);
	        int drawHeight = Math.min(renderAmount - j, 16);

	        int drawX = (int) (x + i);
	        int drawY = posY + j;
	        double minU = fluidTexture.getMinU();
	        double maxU = fluidTexture.getMaxU();
	        double minV = fluidTexture.getMinV();
	        double maxV = fluidTexture.getMaxV();

	        Tessellator tessellator = Tessellator.getInstance();
	        VertexBuffer tes = tessellator.getBuffer();
	        tes.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
	        tes.pos(drawX, drawY + drawHeight, 0).tex(minU, minV + (maxV - minV) * drawHeight / 16F).endVertex();
	        tes.pos(drawX + drawWidth, drawY + drawHeight, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV + (maxV - minV) * drawHeight / 16F).endVertex();
	        tes.pos(drawX + drawWidth, drawY, 0).tex(minU + (maxU - minU) * drawWidth / 16F, minV).endVertex();
	        tes.pos(drawX, drawY, 0).tex(minU, minV).endVertex();
	        tessellator.draw();
	      }
	    }
	    GlStateManager.disableBlend();
	}
	
	public static TextureAtlasSprite getTextureMissingSprite() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}
	
	
	
}
