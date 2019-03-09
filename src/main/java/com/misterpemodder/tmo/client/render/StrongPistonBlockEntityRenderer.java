package com.misterpemodder.tmo.client.render;

import java.util.Random;
import com.misterpemodder.tmo.block.StrongPistonBlock;
import com.misterpemodder.tmo.block.StrongPistonBlock.ExtensionType;
import com.misterpemodder.tmo.block.StrongPistonHeadBlock;
import com.misterpemodder.tmo.block.entity.StrongPistonBlockEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.PistonType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class StrongPistonBlockEntityRenderer extends BlockEntityRenderer<StrongPistonBlockEntity> {
  private BlockRenderManager manager;

  public StrongPistonBlockEntityRenderer() {
    this.manager = MinecraftClient.getInstance().getBlockRenderManager();
  }

  @Override
  public void render(StrongPistonBlockEntity be, double xOffset, double yOffset, double zOffset,
      float tickDelta, int blockBreakStage) {
    BlockPos basePos = be.getPos().offset(be.getMovingDirection().getOpposite());
    BlockState pushedBlockState = be.getPushedBlock();
    float stage = be.getProgress(tickDelta);

    if (pushedBlockState.isAir() || stage > 1.0F)
      return;
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferBuilder = tessellator.getBufferBuilder();
    this.bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
    GuiLighting.disable();
    GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableBlend();
    GlStateManager.disableCull();
    if (MinecraftClient.isAmbientOcclusionEnabled())
      GlStateManager.shadeModel(7425);
    else
      GlStateManager.shadeModel(7424);
    bufferBuilder.begin(7, VertexFormats.POSITION_COLOR_UV_LMAP);
    bufferBuilder.setOffset(xOffset - basePos.getX() + be.getRenderOffsetX(tickDelta),
        yOffset - basePos.getY() + be.getRenderOffsetY(tickDelta),
        zOffset - basePos.getZ() + be.getRenderOffsetZ(tickDelta));

    World world = this.getWorld();

    if (pushedBlockState.getBlock() instanceof StrongPistonBlock && be.isSource()) {
      StrongPistonBlock piston = (StrongPistonBlock) pushedBlockState.getBlock();
      PistonType type = piston.getType();
      BlockState headState =
          piston.getHeadBlock().getDefaultState().with(StrongPistonHeadBlock.TYPE, type)
              .with(StrongPistonHeadBlock.FACING, pushedBlockState.get(StrongPistonBlock.FACING));

      if (stage >= 0.4375F) {
        headState = headState.with(StrongPistonHeadBlock.SHORT, true);
        pushedBlockState =
            pushedBlockState.with(StrongPistonBlock.EXTENSION_TYPE, ExtensionType.PARTIAL);
      } else {
        headState = headState.with(StrongPistonHeadBlock.SHORT, false);
        pushedBlockState =
            pushedBlockState.with(StrongPistonBlock.EXTENSION_TYPE, ExtensionType.EXTENDED);
      }

      this.renderBlockState(basePos, headState, bufferBuilder, world, false);
      BlockPos blockPos18 = basePos.offset(be.getMovingDirection());
      bufferBuilder.setOffset(xOffset - blockPos18.getX(), yOffset - blockPos18.getY(),
          zOffset - blockPos18.getZ());
      this.renderBlockState(blockPos18, pushedBlockState, bufferBuilder, world, true);
    } else {
      if (pushedBlockState.getBlock() instanceof StrongPistonHeadBlock && stage <= 0.5625F)
        pushedBlockState = pushedBlockState.with(StrongPistonHeadBlock.SHORT, true);
      this.renderBlockState(basePos, pushedBlockState, bufferBuilder, world, false);
    }

    bufferBuilder.setOffset(0.0, 0.0, 0.0);
    tessellator.draw();
    GuiLighting.enable();
  }

  private boolean renderBlockState(BlockPos blockPos, BlockState blockState,
      BufferBuilder bufferBuilder, World world, boolean boolean6) {
    if (this.manager == null)
      this.manager = MinecraftClient.getInstance().getBlockRenderManager();
    return this.manager.getModelRenderer().tesselate(world, this.manager.getModel(blockState),
        blockState, blockPos, bufferBuilder, boolean6, new Random(),
        blockState.getRenderingSeed(blockPos));
  }
}

