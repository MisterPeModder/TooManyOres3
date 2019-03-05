package misterpemodder.tmo.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StrongRedstoneBlock extends Block {
  public StrongRedstoneBlock(Block.Settings settings) {
    super(settings);
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean emitsRedstonePower(BlockState state) {
    return true;
  }

  @Override
  public int getWeakRedstonePower(BlockState state, BlockView view, BlockPos pos,
      Direction direction) {
    return 15;
  }

  @Override
  public int getStrongRedstonePower(BlockState state, BlockView view, BlockPos pos,
      Direction direction) {
    return 15;
  }

  protected void updateSurronding(World world, BlockPos pos) {
    world.updateNeighbors(pos, this);
    world.updateNeighborsExcept(pos.west(), this, Direction.EAST);
    world.updateNeighborsExcept(pos.east(), this, Direction.WEST);
    world.updateNeighborsExcept(pos.down(), this, Direction.UP);
    world.updateNeighborsExcept(pos.up(), this, Direction.DOWN);
    world.updateNeighborsExcept(pos.north(), this, Direction.SOUTH);
    world.updateNeighborsExcept(pos.south(), this, Direction.NORTH);
  }

  @Override
  public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState otherState,
      boolean flag) {
    if (state.getBlock() != otherState.getBlock()) {
      super.onBlockRemoved(state, world, pos, state, flag);
      updateSurronding(world, pos);
    }
  }

  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState otherState) {
    super.onBlockAdded(state, world, pos, otherState);
    world.updateNeighbors(pos, this);
    updateSurronding(world, pos);
  }
}
