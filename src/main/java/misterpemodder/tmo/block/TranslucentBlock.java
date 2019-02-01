package misterpemodder.tmo.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public class TranslucentBlock extends Block {
  public TranslucentBlock(Block.Settings settings) {
    super(settings);
  }

  @Override
  @Environment(EnvType.CLIENT)
  public boolean skipRenderingSide(BlockState current, BlockState neighbor, Direction direction) {
    return neighbor.getBlock() == this ? true
        : super.skipRenderingSide(current, neighbor, direction);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
