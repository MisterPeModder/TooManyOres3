package misterpemodder.tmo.hook;

import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.Direction;

public interface BlockHook {
  public default boolean tmoCanConnectToRedstone(BlockState state, @Nullable Direction direction) {
    return false;
  }
}
