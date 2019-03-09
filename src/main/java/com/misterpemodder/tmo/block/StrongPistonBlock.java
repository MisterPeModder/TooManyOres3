package com.misterpemodder.tmo.block;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import com.misterpemodder.tmo.block.entity.StrongPistonBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.PistonType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StrongPistonBlock extends FacingBlock {
  // public static BooleanProperty EXTENDED = Properties.EXTENDED;
  public static EnumProperty<ExtensionType> EXTENSION_TYPE =
      EnumProperty.create("extension_type", ExtensionType.class);

  private static final VoxelShape DOWN_EXTENDED_SHAPE;
  private static final VoxelShape UP_EXTENDED_SHAPE;
  private static final VoxelShape NORTH_EXTENDED_SHAPE;
  private static final VoxelShape SOUTH_EXTENDED_SHAPE;
  private static final VoxelShape WEST_EXTENDED_SHAPE;
  private static final VoxelShape EAST_EXTENDED_SHAPE;

  private final PistonType type;

  public StrongPistonBlock(PistonType type, Block.Settings settings) {
    super(settings);
    setDefaultState(this.stateFactory.getDefaultState().with(FACING, Direction.NORTH)
        .with(EXTENSION_TYPE, ExtensionType.NONE));
    this.type = type;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  public PistonType getType() {
    return this.type;
  }

  public static boolean isFull(BlockState state) {
    return state.get(EXTENSION_TYPE) == ExtensionType.NONE;
  }

  @Override
  public boolean canSuffocate(BlockState state, BlockView view, BlockPos pos) {
    return isFull(state);
  }

  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition) {
    if (isFull(state))
      return VoxelShapes.fullCube();
    switch (state.get(FACING)) {
      case UP:
        return UP_EXTENDED_SHAPE;
      case DOWN:
        return DOWN_EXTENDED_SHAPE;
      case NORTH:
        return NORTH_EXTENDED_SHAPE;
      case SOUTH:
        return SOUTH_EXTENDED_SHAPE;
      case WEST:
        return WEST_EXTENDED_SHAPE;
      default:
        return EAST_EXTENDED_SHAPE;
    }
  }

  @Override
  public boolean hasSolidTopSurface(BlockState state, BlockView view, BlockPos pos) {
    return isFull(state) || state.get(FACING) == Direction.DOWN;
  }

  @Override
  public boolean isSimpleFullBlock(BlockState state, BlockView view, BlockPos pos) {
    return false;
  }

  @Override
  public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer,
      ItemStack stack) {
    if (!world.isClient)
      tryExtend(world, pos, state);
  }

  @Override
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block,
      BlockPos neighborPos) {
    if (!world.isClient)
      tryExtend(world, pos, state);
  }

  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState) {
    if (oldState.getBlock() == state.getBlock())
      return;
    if (!world.isClient && world.getBlockEntity(pos) == null)
      tryExtend(world, pos, state);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext context) {
    return this.getDefaultState().with(FACING, context.getPlayerFacing().getOpposite())
        .with(EXTENSION_TYPE, ExtensionType.NONE);
  }

  protected void tryExtend(World world, BlockPos pos, BlockState state) {
    Direction facing = state.get(FACING);
    boolean shouldExtend = shouldExtend(world, pos, facing);
    if (shouldExtend && isFull(state)) {
      if (new StrongPistonHandler(world, pos, facing, true).calculatePush())
        world.addBlockAction(pos, this, 0, facing.getId());
    } else if (!shouldExtend && !isFull(state)) {
      BlockPos movingBlockPos = pos.offset(facing, 2);
      BlockState movingBlockState = world.getBlockState(movingBlockPos);
      int action = 1;
      if (movingBlockState.getBlock() instanceof MovingStrongPistonBlock
          && movingBlockState.get(FACING) == facing) {
        BlockEntity be = world.getBlockEntity(movingBlockPos);
        if (be instanceof StrongPistonBlockEntity) {
          StrongPistonBlockEntity strongPistonBe = (StrongPistonBlockEntity) be;
          if (strongPistonBe.isExtending() && (strongPistonBe.getProgress(0.0F) < 0.5F
              || world.getTime() == strongPistonBe.getSavedWorldTime()
              || ((ServerWorld) world).isInsideTick())) {
            action = 2;
          }
        }
      }
      world.addBlockAction(pos, this, action, facing.getId());
    }
  }

  protected boolean shouldExtend(World world, BlockPos pos, Direction direction) {
    for (final Direction d : Direction.values()) {
      if (d != direction && world.isEmittingRedstonePower(pos.offset(d), d)) {
        return true;
      }
    }
    if (world.isEmittingRedstonePower(pos, Direction.DOWN)) {
      return true;
    }
    BlockPos pos2 = pos.up();
    for (final Direction d : Direction.values()) {
      if (d != Direction.DOWN && world.isEmittingRedstonePower(pos2.offset(d), d)) {
        return true;
      }
    }
    return false;
  }

  public MovingStrongPistonBlock getMovingBlock() {
    return TmoBlocks.MOVING_STRONG_PISTON;
  }

  public StrongPistonHeadBlock getHeadBlock() {
    return TmoBlocks.STRONG_PISTON_HEAD;
  }

  @Override
  public boolean onBlockAction(BlockState state, World world, BlockPos pos, int type, int data) {
    Direction direction = state.get(FACING);
    if (!world.isClient) {
      boolean shouldExtend = this.shouldExtend(world, pos, direction);
      if (shouldExtend && (type == 1 || type == 2)) {
        world.setBlockState(pos, state.with(EXTENSION_TYPE, ExtensionType.EXTENDED), 2);
        return false;
      }
      if (!shouldExtend && type == 0) {
        return false;
      }
    }
    if (type == 0) {
      if (!this.move(world, pos, direction, true)) {
        return false;
      }
      world.setBlockState(pos, state.with(EXTENSION_TYPE, ExtensionType.PARTIAL), 67);
      world.playSound(null, pos, SoundEvents.BLOCK_PISTON_EXTEND, SoundCategory.BLOCK, 0.5f,
          world.random.nextFloat() * 0.25f + 0.6f);
    } else if (type == 1 || type == 2) {
      BlockEntity be = world.getBlockEntity(pos.offset(direction));
      if (be instanceof StrongPistonBlockEntity)
        ((StrongPistonBlockEntity) be).finishMovement();
      MovingStrongPistonBlock movingBlock = getMovingBlock();
      world.setBlockState(pos,
          movingBlock.getDefaultState().with(MovingStrongPistonBlock.FACING, direction)
              .with(MovingStrongPistonBlock.TYPE, getType()),
          3);
      world.setBlockEntity(pos, movingBlock.createStrongPistonBlockEntity(
          this.getDefaultState().with(FACING, Direction.byId(data & 0x7)), direction, false, true));
      if (getType() == PistonType.STICKY) {
        BlockPos attachmentPos = pos.offset(direction, 2);
        BlockState attachmentState = world.getBlockState(attachmentPos);
        Block attachmentBlock = attachmentState.getBlock();
        boolean blockMovement = false;
        if (attachmentBlock instanceof MovingStrongPistonBlock) {
          BlockEntity attachmentBe = world.getBlockEntity(attachmentPos);
          if (attachmentBe instanceof StrongPistonBlockEntity) {
            StrongPistonBlockEntity pistonBlockEntity14 = (StrongPistonBlockEntity) attachmentBe;
            if (pistonBlockEntity14.getFacing() == direction && pistonBlockEntity14.isExtending()) {
              pistonBlockEntity14.finishMovement();
              blockMovement = true;
            }
          }
        }
        if (!blockMovement) {
          if (type == 1 && !attachmentState.isAir()
              && isMovable(attachmentState, world, attachmentPos, direction.getOpposite(), false,
                  direction)
              && (attachmentState.getPistonBehavior() == PistonBehavior.NORMAL
                  || attachmentBlock instanceof StrongPistonBlock)) {
            this.move(world, pos, direction, false);
          } else {
            world.clearBlockState(pos.offset(direction));
          }
        }
      } else {
        world.clearBlockState(pos.offset(direction));
      }
      world.playSound(null, pos, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCK, 0.5f,
          world.random.nextFloat() * 0.15f + 0.6f);
    }
    return true;
  }

  @Override
  public PistonBehavior getPistonBehavior(BlockState state) {
    if (!isFull(state))
      return PistonBehavior.BLOCK;
    return super.getPistonBehavior(state);
  }

  public static boolean isMovable(BlockState state, World world, BlockPos pos, Direction facing,
      boolean canBreak, Direction movingDirection) {
    Block block = state.getBlock();
    if (block == Blocks.OBSIDIAN) {
      return false;
    }
    if (!world.getWorldBorder().contains(pos)) {
      return false;
    }
    if (pos.getY() < 0 || (facing == Direction.DOWN && pos.getY() == 0)) {
      return false;
    }
    if (pos.getY() > world.getHeight() - 1
        || (facing == Direction.UP && pos.getY() == world.getHeight() - 1)) {
      return false;
    }
    if (block == Blocks.PISTON || block == Blocks.STICKY_PISTON) {
      if (state.get(PistonBlock.field_12191)) {
        return false;
      }
    } else {
      if (state.getHardness(world, pos) == -1.0f) {
        return false;
      }
      switch (state.getPistonBehavior()) {
        case BLOCK:
          return false;
        case DESTROY:
          return canBreak;
        case PUSH_ONLY:
          return facing == movingDirection;
        default:
      }
    }
    return !block.hasBlockEntity();
  }

  private boolean move(World world, BlockPos pos, Direction dir, boolean boolean5) {
    BlockPos blockPos6 = pos.offset(dir);
    MovingStrongPistonBlock movingBlock = getMovingBlock();
    if (!boolean5 && world.getBlockState(blockPos6).getBlock() == getHeadBlock())
      world.setBlockState(blockPos6, Blocks.AIR.getDefaultState(), 20);
    StrongPistonHandler handler = new StrongPistonHandler(world, pos, dir, boolean5);
    if (!handler.calculatePush())
      return false;
    List<BlockPos> toMovePos = handler.getMovedBlocks();
    List<BlockState> toMoveStates =
        toMovePos.stream().map(world::getBlockState).collect(Collectors.toList());
    List<BlockPos> brokenBlocks = handler.getBrokenBlocks();
    int integer11 = toMovePos.size() + brokenBlocks.size();
    BlockState[] var13 = new BlockState[integer11];
    Direction direction13 = boolean5 ? dir : dir.getOpposite();
    Set<BlockPos> set14 = new HashSet<>();
    set14.addAll(toMovePos);
    for (BlockPos brokenBlock : brokenBlocks) {
      BlockState blockState17 = world.getBlockState(brokenBlock);
      BlockEntity blockEntity18 =
          blockState17.getBlock().hasBlockEntity() ? world.getBlockEntity(brokenBlock) : null;
      Block.dropStacks(blockState17, world, brokenBlock, blockEntity18);
      world.setBlockState(brokenBlock, Blocks.AIR.getDefaultState(), 18);
      var13[--integer11] = blockState17;
    }
    for (int var16 = toMovePos.size() - 1; var16 >= 0; --var16) {
      BlockPos blockPos16 = toMovePos.get(var16);
      BlockState blockState17 = world.getBlockState(blockPos16);
      blockPos16 = blockPos16.offset(direction13);
      set14.remove(blockPos16);
      world.setBlockState(blockPos16, movingBlock.getDefaultState().with(FACING, dir), 68);
      world.setBlockEntity(blockPos16,
          movingBlock.createStrongPistonBlockEntity(toMoveStates.get(var16), dir, boolean5, false));
      var13[--integer11] = blockState17;
    }
    if (boolean5) {
      PistonType pistonType15 = getType();
      BlockState blockState16 = getHeadBlock().getDefaultState()
          .with(StrongPistonHeadBlock.FACING, dir).with(StrongPistonHeadBlock.TYPE, pistonType15);
      BlockState blockState17 =
          getMovingBlock().getDefaultState().with(MovingStrongPistonBlock.FACING, dir)
              .with(MovingStrongPistonBlock.TYPE, pistonType15);
      set14.remove(blockPos6);
      world.setBlockState(blockPos6, blockState17, 68);
      world.setBlockEntity(blockPos6,
          movingBlock.createStrongPistonBlockEntity(blockState16, dir, true, true));
    }
    Iterator<BlockPos> iterator15 = set14.iterator();
    while (iterator15.hasNext()) {
      BlockPos blockPos16 = iterator15.next();
      world.setBlockState(blockPos16, Blocks.AIR.getDefaultState(), 66);
    }
    for (int var16 = brokenBlocks.size() - 1; var16 >= 0; --var16) {
      BlockState blockState16 = var13[integer11++];
      BlockPos blockPos17 = brokenBlocks.get(var16);
      blockState16.method_11637(world, blockPos17, 2);
      world.updateNeighborsAlways(blockPos17, blockState16.getBlock());
    }
    for (int var16 = toMovePos.size() - 1; var16 >= 0; --var16) {
      world.updateNeighborsAlways(toMovePos.get(var16), var13[integer11++].getBlock());
    }
    if (boolean5) {
      world.updateNeighborsAlways(blockPos6, TmoBlocks.STRONG_PISTON_HEAD);
    }
    return true;
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
    builder.with(FACING, EXTENSION_TYPE);
  }

  @Override
  public boolean method_9526(BlockState state) {
    return !isFull(state);
  }

  @Override
  public boolean canPlaceAtSide(BlockState world, BlockView view, BlockPos pos,
      BlockPlacementEnvironment env) {
    return false;
  }

  public static enum ExtensionType implements StringRepresentable {
    NONE("none"), PARTIAL("partial"), EXTENDED("extended");

    private final String name;

    private ExtensionType(String name) {
      this.name = name;
    }

    @Override
    public String asString() {
      return this.name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }

  static {
    EAST_EXTENDED_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 12.0, 16.0, 16.0);
    WEST_EXTENDED_SHAPE = Block.createCuboidShape(4.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    SOUTH_EXTENDED_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 12.0);
    NORTH_EXTENDED_SHAPE = Block.createCuboidShape(0.0, 0.0, 4.0, 16.0, 16.0, 16.0);
    UP_EXTENDED_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);
    DOWN_EXTENDED_SHAPE = Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 16.0, 16.0);
  }
}
