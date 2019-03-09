package com.misterpemodder.tmo.block;

import com.misterpemodder.tmo.util.VoxelShapeUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.ViewableWorld;
import net.minecraft.world.World;

public class StrongPistonHeadBlock extends FacingBlock {
  public static final EnumProperty<PistonType> TYPE = Properties.PISTON_TYPE;
  public static final BooleanProperty SHORT = Properties.SHORT;

  protected static final VoxelShape SHORT_SHAPE_DOWN;
  protected static final VoxelShape SHORT_SHAPE_UP;
  protected static final VoxelShape SHORT_SHAPE_NORTH;
  protected static final VoxelShape SHORT_SHAPE_SOUTH;
  protected static final VoxelShape SHORT_SHAPE_WEST;
  protected static final VoxelShape SHORT_SHAPE_EAST;

  protected static final VoxelShape LONG_SHAPE_DOWN;
  protected static final VoxelShape LONG_SHAPE_UP;
  protected static final VoxelShape LONG_SHAPE_NORTH;
  protected static final VoxelShape LONG_SHAPE_SOUTH;
  protected static final VoxelShape LONG_SHAPE_WEST;
  protected static final VoxelShape LONG_SHAPE_EAST;

  public StrongPistonHeadBlock(Block.Settings settings) {
    super(settings);
    setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH)
        .with(TYPE, PistonType.NORMAL).with(SHORT, false));
  }

  @Override
  public PistonBehavior getPistonBehavior(BlockState blockState_1) {
    return PistonBehavior.BLOCK;
  }

  @Override
  public boolean method_9526(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition) {
    boolean isShort = state.get(SHORT);
    switch (state.get(FACING)) {
      case DOWN:
        return isShort ? SHORT_SHAPE_DOWN : LONG_SHAPE_DOWN;
      case UP:
        return isShort ? SHORT_SHAPE_UP : LONG_SHAPE_UP;
      case NORTH:
        return isShort ? SHORT_SHAPE_NORTH : LONG_SHAPE_NORTH;
      case SOUTH:
        return isShort ? SHORT_SHAPE_SOUTH : LONG_SHAPE_SOUTH;
      case WEST:
        return isShort ? SHORT_SHAPE_WEST : LONG_SHAPE_WEST;
      default:
        return isShort ? SHORT_SHAPE_EAST : LONG_SHAPE_EAST;
    }
  }

  @Override
  public boolean hasSolidTopSurface(BlockState state, BlockView view, BlockPos pos) {
    return state.get(FACING) == Direction.UP;
  }

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (!world.isClient && player.abilities.creativeMode) {
      BlockPos basePos = pos.offset(state.get(FACING).getOpposite());
      Block baseBlock = world.getBlockState(basePos).getBlock();
      if (baseBlock instanceof StrongPistonBlock)
        world.clearBlockState(basePos);
    }
    super.onBreak(world, pos, state, player);
  }

  @Override
  public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState neighborState,
      boolean flag) {
    if (state.getBlock() == neighborState.getBlock())
      return;
    super.onBlockRemoved(state, world, pos, neighborState, flag);
    BlockPos basePos = pos.offset(state.get(FACING).getOpposite());
    BlockState baseState = world.getBlockState(basePos);
    if (baseState.getBlock() instanceof StrongPistonBlock && !StrongPistonBlock.isFull(baseState)) {
      Block.dropStacks(baseState, world, basePos);
      world.clearBlockState(basePos);
    }
  }

  @Override
  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction,
      BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
    if (direction.getOpposite() == state.get(StrongPistonHeadBlock.FACING)
        && !state.canPlaceAt(world, pos)) {
      return Blocks.AIR.getDefaultState();
    }
    return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos,
        neighborPos);
  }

  @Override
  public boolean canPlaceAt(BlockState state, ViewableWorld world, BlockPos pos) {
    Block block =
        world.getBlockState(pos.offset(state.get(StrongPistonHeadBlock.FACING).getOpposite()))
            .getBlock();
    return block == TmoBlocks.STRONG_PISTON || block == TmoBlocks.STICKY_STRONG_PISTON
        || block == TmoBlocks.MOVING_STRONG_PISTON;
  }

  @Override
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block,
      BlockPos neighborPos) {
    if (state.canPlaceAt(world, pos)) {
      BlockPos basePos = pos.offset(state.get(FACING).getOpposite());
      world.getBlockState(basePos).neighborUpdate(world, basePos, block, neighborPos);
    }
  }

  @Override
  @Environment(EnvType.CLIENT)
  public ItemStack getPickStack(BlockView view, BlockPos pos, BlockState state) {
    return new ItemStack(state.get(TYPE) == PistonType.STICKY ? TmoBlocks.STICKY_STRONG_PISTON
        : TmoBlocks.STRONG_PISTON);
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }

  protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
    builder.with(FACING, SHORT, TYPE);
  }

  @Override
  public boolean canPlaceAtSide(BlockState state, BlockView view, BlockPos pos,
      BlockPlacementEnvironment environment) {
    return false;
  }

  static {
    final VoxelShape headDown =
        VoxelShapes.union(VoxelShapeUtil.fromDims(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            VoxelShapeUtil.fromDims(5.0, 4.0, 5.0, 6.0, 1.0, 6.0));
    final VoxelShape headUp =
        VoxelShapes.union(VoxelShapeUtil.fromDims(0.0, 12.0, 0.0, 16.0, 4.0, 16.0),
            VoxelShapeUtil.fromDims(5.0, 11.0, 5.0, 6.0, 1.0, 6.0));
    final VoxelShape headNorth =
        VoxelShapes.union(VoxelShapeUtil.fromDims(0.0, 0.0, 0.0, 16.0, 16.0, 4.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, 4.0, 6.0, 6.0, 1.0));
    final VoxelShape headSouth =
        VoxelShapes.union(VoxelShapeUtil.fromDims(0.0, 0.0, 12.0, 16.0, 16.0, 4.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, 11.0, 6.0, 6.0, 1.0));
    final VoxelShape headWest =
        VoxelShapes.union(VoxelShapeUtil.fromDims(0.0, 0.0, 0.0, 4.0, 16.0, 16.0),
            VoxelShapeUtil.fromDims(4.0, 5.0, 5.0, 1.0, 6.0, 6.0));
    final VoxelShape headEast =
        VoxelShapes.union(VoxelShapeUtil.fromDims(12.0, 0.0, 0.0, 4.0, 16.0, 16.0),
            VoxelShapeUtil.fromDims(11.0, 5.0, 5.0, 1.0, 6.0, 6.0));
    SHORT_SHAPE_DOWN =
        VoxelShapes.union(headDown, VoxelShapeUtil.fromDims(6.0, 5.0, 6.0, 4.0, 3.0, 4.0),
            VoxelShapeUtil.fromDims(5.0, 8.0, 5.0, 6.0, 8.0, 6.0));
    LONG_SHAPE_DOWN =
        VoxelShapes.union(headDown, VoxelShapeUtil.fromDims(6.0, 5.0, 6.0, 4.0, 7.0, 4.0),
            VoxelShapeUtil.fromDims(5.0, 12.0, 5.0, 6.0, 8.0, 6.0));
    SHORT_SHAPE_UP =
        VoxelShapes.union(headUp, VoxelShapeUtil.fromDims(6.0, 8.0, 6.0, 4.0, 3.0, 4.0),
            VoxelShapeUtil.fromDims(5.0, 0.0, 5.0, 6.0, 8.0, 6.0));
    LONG_SHAPE_UP = VoxelShapes.union(headUp, VoxelShapeUtil.fromDims(6.0, 4.0, 6.0, 4.0, 7.0, 4.0),
        VoxelShapeUtil.fromDims(5.0, -4.0, 5.0, 6.0, 8.0, 6.0));
    SHORT_SHAPE_NORTH =
        VoxelShapes.union(headNorth, VoxelShapeUtil.fromDims(6.0, 6.0, 5.0, 4.0, 4.0, 3.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, 8.0, 6.0, 6.0, 8.0));
    LONG_SHAPE_NORTH =
        VoxelShapes.union(headNorth, VoxelShapeUtil.fromDims(6.0, 6.0, 5.0, 4.0, 4.0, 7.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, 12.0, 6.0, 6.0, 8.0));
    SHORT_SHAPE_SOUTH =
        VoxelShapes.union(headSouth, VoxelShapeUtil.fromDims(6.0, 6.0, 8.0, 4.0, 4.0, 3.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, 0.0, 6.0, 6.0, 8.0));
    LONG_SHAPE_SOUTH =
        VoxelShapes.union(headSouth, VoxelShapeUtil.fromDims(6.0, 6.0, 4.0, 4.0, 4.0, 7.0),
            VoxelShapeUtil.fromDims(5.0, 5.0, -4.0, 6.0, 6.0, 8.0));
    SHORT_SHAPE_WEST =
        VoxelShapes.union(headWest, VoxelShapeUtil.fromDims(5.0, 6.0, 6.0, 3.0, 4.0, 4.0),
            VoxelShapeUtil.fromDims(8.0, 5.0, 5.0, 8.0, 6.0, 6.0));
    LONG_SHAPE_WEST =
        VoxelShapes.union(headWest, VoxelShapeUtil.fromDims(5.0, 6.0, 6.0, 7.0, 4.0, 4.0),
            VoxelShapeUtil.fromDims(12.0, 5.0, 5.0, 8.0, 6.0, 6.0));
    SHORT_SHAPE_EAST =
        VoxelShapes.union(headEast, VoxelShapeUtil.fromDims(8.0, 6.0, 6.0, 3.0, 4.0, 4.0),
            VoxelShapeUtil.fromDims(0.0, 5.0, 5.0, 8.0, 6.0, 6.0));
    LONG_SHAPE_EAST =
        VoxelShapes.union(headEast, VoxelShapeUtil.fromDims(4.0, 6.0, 6.0, 7.0, 4.0, 4.0),
            VoxelShapeUtil.fromDims(-4.0, 5.0, 5.0, 8.0, 6.0, 6.0));
  }
}
