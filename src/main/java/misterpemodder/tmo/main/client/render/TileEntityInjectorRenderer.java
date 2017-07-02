package misterpemodder.tmo.main.client.render;

import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class TileEntityInjectorRenderer extends TileEntitySpecialRenderer<TileEntityInjector> {
	
	public TileEntityInjectorRenderer() {
	}
	
	@Override
	public void renderTileEntityAt(TileEntityInjector te, double x, double y, double z, float partialTicks, int destroyStage) {
        
        IBlockState state = null;
        
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            state = block.getStateFromMeta(te.getBlockMetadata());
        }
        
        ItemStack stack = te.getInventory().getStackInSlot(0);
        if(!stack.isEmpty()) {
        	
        	GlStateManager.enableDepth();
            GlStateManager.depthFunc(515);
            GlStateManager.depthMask(true);
        	
        	GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
            GlStateManager.scale(1.0F, -1.0F, -1.0F);
            GlStateManager.translate(0.5F, 0.5F, 0.5F);
            int j = 0;

            if(state != null) {
            	EnumFacing facing = state.getValue(BlockInjector.FACING);
            	j = (int) facing.getHorizontalAngle();
            }
                
            GlStateManager.rotate(j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        	
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            
            GlStateManager.translate(0.5F, 0.35F, 0.27F);
            GlStateManager.scale(0.6F, -0.6F, -0.6F);
         
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(22.5F, 1.0F, 0.0F, 0.0F);
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(te.getInventory().getStackInSlot(0), TransformType.FIXED);
            
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();

            GlStateManager.disableRescaleNormal();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        }
        
	}
	
}
