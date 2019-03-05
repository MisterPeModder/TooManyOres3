package com.misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import com.misterpemodder.tmo.hook.BlockHook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

@Mixin(Block.class)
public class BlockMixin implements BlockHook {
  @Override
  public boolean tmoCanConnectToRedstone(BlockState state, Direction direction) {
    return false;
  }
}
