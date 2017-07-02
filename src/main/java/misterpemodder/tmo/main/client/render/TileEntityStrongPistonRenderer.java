package misterpemodder.tmo.main.client.render;

import misterpemodder.tmo.main.blocks.BlockStrongPistonExtension;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.tileentity.TileEntityStrongPiston;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockPistonExtension;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityStrongPistonRenderer extends TileEntitySpecialRenderer<TileEntityStrongPiston> {
	
	protected BlockRendererDispatcher blockRenderer;
	
	@Override
    public void renderTileEntityAt(TileEntityStrongPiston te, double x, double y, double z, float partialTicks, int destroyStage) {
        if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        
        BlockPos blockpos = te.getPos();
        IBlockState iblockstate = te.getPistonState();
        Block block = iblockstate.getBlock();
        
        if (iblockstate.getMaterial() != Material.AIR && te.getProgress(partialTicks) < 1.0F) {
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(7425);
            }
            else {
                GlStateManager.shadeModel(7424);
            }

            vertexbuffer.begin(7, DefaultVertexFormats.BLOCK);
            vertexbuffer.setTranslation(x - blockpos.getX() + te.getOffsetX(partialTicks), y - blockpos.getY() + te.getOffsetY(partialTicks), z - blockpos.getZ() + te.getOffsetZ(partialTicks));
            World world = this.getWorld();

            if (block instanceof BlockStrongPistonExtension && te.getProgress(partialTicks) <= 0.25F) {
                iblockstate = iblockstate.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(true));
                this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, true);
            }
            else if (te.shouldPistonHeadBeRendered() && !te.isExtending()) {
                BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = block == TheBlocks.STRONG_PISTON_STICKY.getBlock() ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
                IBlockState iblockstate1 = TheBlocks.STRONG_PISTON_EXTENSION.getBlock().getDefaultState().withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype).withProperty(BlockPistonExtension.FACING, iblockstate.getValue(BlockPistonBase.FACING));
                iblockstate1 = iblockstate1.withProperty(BlockPistonExtension.SHORT, Boolean.valueOf(te.getProgress(partialTicks) >= 0.5F));
                this.renderStateModel(blockpos, iblockstate1, vertexbuffer, world, true);
                vertexbuffer.setTranslation(x - blockpos.getX(), y - blockpos.getY(), z - blockpos.getZ());
                iblockstate = iblockstate.withProperty(BlockPistonBase.EXTENDED, Boolean.valueOf(true));
                this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, true);
            }
            else {
            	if(block.getRenderType(iblockstate) == EnumBlockRenderType.MODEL) {
            		this.renderStateModel(blockpos, iblockstate, vertexbuffer, world, false);
            	}
            	//Causes too many problems
            	/*if(te.containedTileEntityNBT != null && te.containedTileEntityClazz != null && !te.containedTileEntityNBT.hasNoTags()) {
            		try {
            			TileEntity t;
            			t = te.containedTileEntityClazz.newInstance();
            			t.setPos(blockpos);
            			TileEntityRendererDispatcher.instance.renderTileEntity(t, partialTicks, -1);
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
            	}*/
            }

            vertexbuffer.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator.draw();
            RenderHelper.enableStandardItemLighting();
        }
    }

    private boolean renderStateModel(BlockPos pos, IBlockState state, VertexBuffer buffer, World world, boolean checkSides) {
        return this.blockRenderer.getBlockModelRenderer().renderModel(world, this.blockRenderer.getModelForState(state), state, pos, buffer, checkSides);
    }
}
