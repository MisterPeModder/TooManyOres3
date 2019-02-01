package misterpemodder.tmo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;

public class TmoOreBlock extends OreBlock {
  public TmoOreBlock() {
    super(Block.Settings.of(Material.STONE).strength(3.0F, 5.0F));
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean isFullBoundsCubeForCulling(BlockState blockState_1) {
    return true;
  }
}
