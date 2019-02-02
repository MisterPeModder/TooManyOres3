package misterpemodder.tmo.block;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;

public class TmoStairsBlock extends StairsBlock {
  public TmoStairsBlock(Block fullBlock) {
    super(fullBlock.getDefaultState(), Block.Settings.copy(fullBlock));
  }
}
