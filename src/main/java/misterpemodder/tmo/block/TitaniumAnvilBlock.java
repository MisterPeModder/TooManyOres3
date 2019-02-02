package misterpemodder.tmo.block;

import misterpemodder.tmo.block.entity.TitaniumAnvilBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockPlacementEnvironment;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sortme.ItemScatterer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class TitaniumAnvilBlock extends FallingBlock implements BlockEntityProvider {
  private static final VoxelShape X_AXIS_SHAPE;
  private static final VoxelShape Z_AXIS_SHAPE;
  public static final DirectionProperty FACING = Properties.FACING_HORIZONTAL;

  public TitaniumAnvilBlock(Block.Settings settings) {
    super(settings);
    setDefaultState(getStateFactory().getDefaultState().with(FACING, Direction.NORTH));
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext context) {
    return this.getDefaultState().with(FACING,
        context.getPlayerHorizontalFacing().rotateYClockwise());
  }

  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition_1) {
    Direction direction_1 = (Direction) state.get(FACING);
    return direction_1.getAxis() == Direction.Axis.X ? X_AXIS_SHAPE : Z_AXIS_SHAPE;
  }

  protected void configureFallingBlockEntity(FallingBlockEntity fallingBlockEntity) {
    fallingBlockEntity.setHurtEntities(true);
  }

  public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState,
      boolean flag) {
    if (state.getBlock() != newState.getBlock()) {
      BlockEntity be = world.getBlockEntity(pos);
      if (be instanceof TitaniumAnvilBlockEntity)
        ItemScatterer.spawn(world, pos, ((TitaniumAnvilBlockEntity) be));
      super.onBlockRemoved(state, world, pos, newState, flag);
      world.removeBlockEntity(pos);
    }
  }

  @Override
  public BlockEntity createBlockEntity(BlockView view) {
    return new TitaniumAnvilBlockEntity();
  }

  @Override
  protected void appendProperties(StateFactory.Builder<Block, BlockState> factory) {
    factory.with(FACING);
  }

  @Override
  public boolean hasBlockEntity() {
    return true;
  }

  @Override
  public BlockRenderType getRenderType(BlockState state) {
    return BlockRenderType.MODEL;
  }

  @Override
  public boolean canPlaceAtSide(BlockState state, BlockView view, BlockPos pos,
      BlockPlacementEnvironment environment) {
    return false;
  }

  public boolean onBlockAction(BlockState state, World world, BlockPos pos, int int_1, int int_2) {
    super.onBlockAction(state, world, pos, int_1, int_2);
    BlockEntity blockEntity_1 = world.getBlockEntity(pos);
    return blockEntity_1 == null ? false : blockEntity_1.onBlockAction(int_1, int_2);
  }

  @Override
  public void onLanding(World world, BlockPos pos, BlockState state, BlockState blockState_2) {
    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCK, 0.3F,
        world.random.nextFloat() * 0.1F + 0.9F);
  }

  @Override
  public void onDestroyedOnLanding(World world, BlockPos pos) {
    world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCK, 1.0F,
        world.random.nextFloat() * 0.1F + 0.9F);
  }

  static {
    X_AXIS_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
        Block.createCuboidShape(3.0D, 4.0D, 4.0D, 13.0D, 5.0D, 12.0D),
        Block.createCuboidShape(4.0D, 5.0D, 6.0D, 12.0D, 10.0D, 10.0D),
        Block.createCuboidShape(0.0D, 10.0D, 3.0D, 16.0D, 16.0D, 13.0D));
    Z_AXIS_SHAPE = VoxelShapes.union(Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 4.0D, 14.0D),
        Block.createCuboidShape(4.0D, 4.0D, 3.0D, 12.0D, 5.0D, 13.0D),
        Block.createCuboidShape(6.0D, 5.0D, 4.0D, 10.0D, 10.0D, 12.0D),
        Block.createCuboidShape(3.0D, 10.0D, 0.0D, 13.0D, 16.0D, 16.0D));
  }
}
