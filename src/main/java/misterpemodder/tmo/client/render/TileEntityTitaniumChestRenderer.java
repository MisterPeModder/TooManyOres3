package misterpemodder.tmo.client.render;

import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityTitaniumChestRenderer extends TileEntitySpecialRenderer<TileEntityTitaniumChest> {
	
	private static final ResourceLocation TEXTURE = new ResourceLocation(TMOHelper.MOD_ID,"textures/entity/titanium_chest.png");
	private final ModelChest simpleChest = new ModelChest();
	
	public TileEntityTitaniumChestRenderer() {
	}
	
	@Override
	public void renderTileEntityAt(TileEntityTitaniumChest te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.enableDepth();
        GlStateManager.depthFunc(515);
        GlStateManager.depthMask(true);
        int i;
        IBlockState state = null;
        if (te.hasWorld()) {
        	if(te.getOwner() == "LOL") {
        		Minecraft.getMinecraft().player.sendChatMessage("lol");
        	}
            Block block = te.getBlockType();
            i = te.getBlockMetadata();
            state = block.getStateFromMeta(i);
        }
        else {
            i = 0;
        }
        ModelChest model = this.simpleChest;

        if (destroyStage >= 0) {
        	this.bindTexture(DESTROY_STAGES[destroyStage]);
            GlStateManager.matrixMode(5890);
            GlStateManager.pushMatrix();
            GlStateManager.scale(4.0F, 4.0F, 1.0F);
            GlStateManager.translate(0.0625F, 0.0625F, 0.0625F);
            GlStateManager.matrixMode(5888);
        } else {
        	this.bindTexture(TEXTURE);
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();

        if (destroyStage < 0)
        	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        GlStateManager.translate((float)x, (float)y + 1.0F, (float)z + 1.0F);
        GlStateManager.scale(1.0F, -1.0F, -1.0F);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        int j = 0;

        if(state != null) {
        	EnumFacing facing = state.getValue(BlockTitaniumChest.FACING);
        	j = (int) facing.getHorizontalAngle();
        }
            
        GlStateManager.rotate((float)j, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        float f = te.prevLidAngle + (te.lidAngle - te.prevLidAngle) * partialTicks;

        f = 1.0F - f;
        f = 1.0F - f * f * f;
        model.chestLid.rotateAngleX = -(f * ((float)Math.PI / 2F));
        model.renderAll();
        
        if(!te.getLockItemHandler().getStackInSlot(0).isEmpty() && destroyStage < 0) {
        	
            GlStateManager.disableLighting();
            GlStateManager.pushAttrib();
            GlStateManager.translate(0.5F, 0.6F, 0.0625F/1.5);
            GlStateManager.scale(0.5F, -0.5F, -0.5F);
            RenderHelper.enableStandardItemLighting();
            Minecraft.getMinecraft().getRenderItem().renderItem(te.getLockItemHandler().getStackInSlot(0), TransformType.FIXED);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttrib();
            GlStateManager.enableLighting();

        }
        
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        if (destroyStage >= 0) {
        	GlStateManager.matrixMode(5890);
        	GlStateManager.popMatrix();
        	GlStateManager.matrixMode(5888);
        }
	}
	
}
