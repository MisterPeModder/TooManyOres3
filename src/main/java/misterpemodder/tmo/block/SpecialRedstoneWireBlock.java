package misterpemodder.tmo.block;

import java.util.Random;
import javax.annotation.Nullable;
import misterpemodder.tmo.hook.BlockHook;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.client.render.block.BlockColorMap;
import net.minecraft.particle.DustParticleParameters;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SpecialRedstoneWireBlock extends RedstoneWireBlock implements BlockHook {
  public final WireType type;

  public SpecialRedstoneWireBlock(WireType type, Block.Settings settings) {
    super(settings);
    this.type = type;
  }

  @Override
  public boolean tmoCanConnectToRedstone(BlockState state, @Nullable Direction direction) {
    return true;
  }

  @Override
  public int getStrongRedstonePower(BlockState state, BlockView view, BlockPos pow,
      Direction direction) {
    return this.type == WireType.WEAK ? 0
        : super.getStrongRedstonePower(state, view, pow, direction);
  }

  public boolean shouldConnectToSide(BlockView view, BlockPos pos, Direction direction) {
    return this.type == WireType.STRONG;
  }

  @Environment(EnvType.CLIENT)
  public static void registerBlockColorMapper(BlockColorMap colorMap) {
    colorMap.register((state, wiew, pow, power) -> {
      return getWireColor(state.get(RedstoneWireBlock.POWER), state.getBlock());
    }, TmoBlocks.COPPER_REDSTONE_WIRE, TmoBlocks.TITANIUM_REDSTONE_WIRE);
  }

  @Environment(EnvType.CLIENT)
  public static int getWireColor(int power, Block block) {
    float f = power / 15.0F;
    float r = 0.0F;
    float g = 0.0F;
    float b = 0.0F;

    if (block == TmoBlocks.TITANIUM_REDSTONE_WIRE) {
      r = f * 0.2F + 0.15F;
      b = f * 0.6F + 0.2F;
    } else if (block == TmoBlocks.COPPER_REDSTONE_WIRE) {
      r = f * 0.55F + 0.45F;
      g = f * 0.3F + 0.1F;
    }

    int i = MathHelper.clamp((int) (r * 255.0F), 0, 255);
    int j = MathHelper.clamp((int) (g * 255.0F), 0, 255);
    int k = MathHelper.clamp((int) (b * 255.0F), 0, 255);
    return -16777216 | i << 16 | j << 8 | k;
  }

  @Environment(EnvType.CLIENT)
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    int power = (Integer) state.get(POWER);
    if (power != 0) {
      double double_1 = (double) pos.getX() + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
      double double_2 = (double) ((float) pos.getY() + 0.0625F);
      double double_3 = (double) pos.getZ() + 0.5D + ((double) random.nextFloat() - 0.5D) * 0.2D;
      int color = getWireColor(power, state.getBlock());
      float r = ((color >> 16) & 0xFF) / 255.0F;
      float g = ((color >> 8) & 0xFF) / 255.0F;
      float b = (color & 0xFF) / 255.0F;
      world.addParticle(new DustParticleParameters(r, g, b, 1.0F), double_1, double_2, double_3,
          0.0D, 0.0D, 0.0D);
    }
  }

  public static enum WireType {
    WEAK, STRONG;
  }
}
