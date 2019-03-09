package com.misterpemodder.tmo.block;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import com.misterpemodder.tmo.block.entity.StrongPistonBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.loot.context.LootContext;
import net.minecraft.world.loot.context.LootContextParameters;

public class MovingStrongPistonBlock extends BlockWithEntity {
  public static final DirectionProperty FACING = StrongPistonHeadBlock.FACING;
  public static final EnumProperty<PistonType> TYPE = StrongPistonHeadBlock.TYPE;

  public MovingStrongPistonBlock(Block.Settings settings) {
    super(settings);
    setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH).with(TYPE,
        PistonType.NORMAL));
  }

  @Override
  public PistonBehavior getPistonBehavior(BlockState blockState_1) {
    return PistonBehavior.BLOCK;
  }

  @Override
  public BlockEntity createBlockEntity(BlockView arg0) {
    return null;
  }

  public StrongPistonBlockEntity createStrongPistonBlockEntity(BlockState pushedBlock,
      Direction direction, boolean extending, boolean source) {
    return new StrongPistonBlockEntity(pushedBlock, direction, extending, source);
  }

  @Override
  public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState,
      boolean flag) {
    if (state.getBlock() == newState.getBlock())
      return;
    BlockEntity be = world.getBlockEntity(pos);
    if (be instanceof StrongPistonBlockEntity)
      ((StrongPistonBlockEntity) be).finishMovement();
  }

  @Override
  public void onBroken(IWorld world, BlockPos pos, BlockState state) {
    BlockPos basePos = pos.offset(state.get(FACING).getOpposite());
    BlockState baseState = world.getBlockState(basePos);
    if (baseState.getBlock() instanceof StrongPistonBlock && !StrongPistonBlock.isFull(baseState))
      world.clearBlockState(basePos);
  }

  @Override
  public boolean isFullBoundsCubeForCulling(BlockState state) {
    return false;
  }

  @Override
  public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player,
      Hand hand, BlockHitResult blockHitResult) {
    if (!world.isClient && world.getBlockEntity(pos) == null) {
      world.clearBlockState(pos);
      return true;
    }
    return false;
  }

  @Override
  public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
    StrongPistonBlockEntity be =
        getBlockEntityPiston(builder.getWorld(), builder.get(LootContextParameters.POSITION));
    if (be == null)
      return Collections.emptyList();
    return be.getPushedBlock().getDroppedStacks(builder);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition) {
    return VoxelShapes.empty();
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition) {
    StrongPistonBlockEntity be = getBlockEntityPiston(view, pos);
    if (be != null)
      return be.getCollisionShape(view, pos);
    return VoxelShapes.empty();
  }

  @Nullable
  private StrongPistonBlockEntity getBlockEntityPiston(BlockView world, BlockPos blockPos) {
    BlockEntity be = world.getBlockEntity(blockPos);
    if (be instanceof StrongPistonBlockEntity)
      return (StrongPistonBlockEntity) be;
    return null;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
    return ItemStack.EMPTY;
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    return state.with(FACING, rotation.rotate(state.get(FACING)));
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirror) {
    return state.rotate(mirror.getRotation(state.get(FACING)));
  }

  @Override
  protected void appendProperties(StateFactory.Builder<Block, BlockState> builder) {
    builder.with(FACING, TYPE);
  }

  @Override
  public boolean canPlaceAtSide(BlockState world, BlockView view, BlockPos pos,
      BlockPlacementEnvironment env) {
    return false;
  }
}
