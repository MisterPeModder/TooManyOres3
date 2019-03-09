package com.misterpemodder.tmo.util;

import net.minecraft.block.Block;
import net.minecraft.util.shape.VoxelShape;

public final class VoxelShapeUtil {
  public static VoxelShape fromDims(double x, double y, double z, double width, double height,
      double depth) {
    return Block.createCuboidShape(x, y, z, x + width, y + height, z + depth);
  }
}
