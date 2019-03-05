package misterpemodder.tmo.block;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneTorchWallBlock;
import net.minecraft.particle.DustParticleParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class WeakRedstoneTorchWallBlock extends RedstoneTorchWallBlock {
  public WeakRedstoneTorchWallBlock(Block.Settings settings) {
    super(settings);
  }

  public int getStrongRedstonePower(BlockState state, BlockView view, BlockPos pos,
      Direction direction) {
    return 0;
  }

  @Environment(EnvType.CLIENT)
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if ((Boolean) state.get(LIT)) {
      double double_1 = (double) pos.getX() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
      double double_2 = (double) pos.getY() + 0.7D + (random.nextDouble() - 0.5D) * 0.2D;
      double double_3 = (double) pos.getZ() + 0.5D + (random.nextDouble() - 0.5D) * 0.2D;
      world.addParticle(new DustParticleParameters(0.35F, 0.0F, 0.8F, 1.0F), double_1, double_2,
          double_3, 0.0D, 0.0D, 0.0D);
    }
  }
}
