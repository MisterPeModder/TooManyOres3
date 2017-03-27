package misterpemodder.tmo.main.client.render;

import misterpemodder.tmo.main.blocks.containers.BlockTitaniumAnvil;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;

public class TileEntityTitaniumAnvilRenderer extends TileEntitySpecialRenderer<TileEntityTitaniumAnvil> {
	
	
	public TileEntityTitaniumAnvilRenderer() {
	}
	
	@Override
	public void renderTileEntityAt(TileEntityTitaniumAnvil te, double x, double y, double z, float partialTicks, int destroyStage) {

        IBlockState state = null;
        
        if (te.hasWorld()) {
            Block block = te.getBlockType();
            state = block.getStateFromMeta(te.getBlockMetadata());
        }
        
        if(!te.getInventory().getStackInSlot(0).isEmpty()) {
        	
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
            	EnumFacing facing = state.getValue(BlockTitaniumAnvil.FACING);
            	j = (int) facing.getHorizontalAngle();
            }
                
            GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        	
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            GlStateManager.translate(0.5F, -0.02, 0.6F);
            GlStateManager.scale(0.7F, -0.7F, -0.7F);
            RenderHelper.enableStandardItemLighting();
            GlStateManager.rotate(90F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(15F, 0.0F, 0.0F, 1.0F);
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
