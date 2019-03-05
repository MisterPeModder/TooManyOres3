package com.misterpemodder.tmo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;

public class CutoutBlock extends Block {

  public CutoutBlock(Block.Settings settings) {
    super(settings);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
