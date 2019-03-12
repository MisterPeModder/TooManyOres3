package com.misterpemodder.tmo.block.entity;

import java.util.List;
import com.misterpemodder.tmo.block.MovingStrongPistonBlock;
import com.misterpemodder.tmo.block.StrongPistonBlock;
import com.misterpemodder.tmo.block.StrongPistonBlock.ExtensionType;
import com.misterpemodder.tmo.block.StrongPistonHeadBlock;
import com.misterpemodder.tmo.block.TmoBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.TagHelper;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BoundingBox;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class StrongPistonBlockEntity extends BlockEntity implements Tickable {
  protected BlockState pushedBlock;
  protected Direction facing;
  private boolean extending;
  private boolean source;
  private boolean updateBase;
  private static final ThreadLocal<Direction> localDirection;
  private float nextProgress;
  private float progress;
  private long savedWorldTime;

  protected <T extends BlockEntity> StrongPistonBlockEntity(BlockEntityType<T> type,
      BlockState pushedBlock, Direction direction, boolean extending, boolean source) {
    super(type);
    this.pushedBlock = pushedBlock;
    this.facing = direction;
    this.extending = extending;
    this.source = source;
    this.updateBase = source && extending;
  }

  public StrongPistonBlockEntity(BlockState pushedBlock, Direction direction, boolean extending,
      boolean source) {
    this(TmoBlockEntityTypes.STRONG_PISTON, pushedBlock, direction, extending, source);
  }

  public StrongPistonBlockEntity() {
    super(TmoBlockEntityTypes.STRONG_PISTON);
  }

  @Override
  public CompoundTag toInitialChunkDataTag() {
    return toTag(new CompoundTag());
  }

  public boolean isExtending() {
    return this.extending;
  }

  public Direction getFacing() {
    return this.facing;
  }

  public boolean isSource() {
    return source;
  }

  public float getProgress(float delta) {
    return MathHelper.lerp(delta > 1.0F ? 1.0F : delta, this.progress, this.nextProgress);
  }

  public BlockState getPushedBlock() {
    return this.pushedBlock;
  }

  public long getSavedWorldTime() {
    return savedWorldTime;
  }

  @Environment(EnvType.CLIENT)
  public float getRenderOffsetX(float delta) {
    float p = getProgress(delta);
    return this.facing.getOffsetX() * (this.isExtending() ? p - 1.0F : 1.0F - p);
  }

  @Environment(EnvType.CLIENT)
  public float getRenderOffsetY(float delta) {
    float p = getProgress(delta);
    return this.facing.getOffsetY() * (this.isExtending() ? p - 1.0F : 1.0F - p);
  }

  @Environment(EnvType.CLIENT)
  public float getRenderOffsetZ(float delta) {
    float p = getProgress(delta);
    return this.facing.getOffsetZ() * (this.isExtending() ? p - 1.0F : 1.0F - p);
  }

  private BlockState getExtensionState() {
    Block piston = this.pushedBlock.getBlock();
    if (!isExtending() && isSource() && piston instanceof StrongPistonBlock)
      return ((StrongPistonBlock) piston).getHeadBlock().getDefaultState()
          .with(StrongPistonHeadBlock.TYPE, ((StrongPistonBlock) piston).getType())
          .with(StrongPistonHeadBlock.FACING, this.pushedBlock.get(StrongPistonBlock.FACING));
    return this.pushedBlock;
  }

  private void pushEntities(float progress) {
    Direction movingDirection = getMovingDirection();
    double delta = progress - this.nextProgress;
    VoxelShape shape = this.getExtensionState().getCollisionShape(this.world, this.pos);
    if (shape.isEmpty())
      return;
    List<BoundingBox> boundingBoxes = shape.getBoundingBoxList();
    BoundingBox mergedBoundingBox = offsetBoundingBox(mergeBoundingBoxes(boundingBoxes));
    List<Entity> entities = this.world.getVisibleEntities(null,
        getExtensionBoundingBox(mergedBoundingBox, movingDirection, delta)
            .union(mergedBoundingBox));
    if (entities.isEmpty())
      return;
    // TODO: replace slime block by some kind of an interface.
    boolean hasSlimeBlock = this.pushedBlock.getBlock() == Blocks.SLIME_BLOCK;
    for (Entity entity : entities) {
      if (entity.getPistonBehavior() != PistonBehavior.IGNORE) {
        if (hasSlimeBlock) {
          Vec3d velocity = entity.getVelocity();
          double velocityX = velocity.x;
          double velocityY = velocity.y;
          double velocityZ = velocity.z;
          switch (movingDirection.getAxis()) {
            case X:
              velocityX = movingDirection.getOffsetX();
              break;
            case Y:
              velocityY = movingDirection.getOffsetY();
              break;
            default:
              velocityZ = movingDirection.getOffsetZ();
          }
          entity.setVelocity(velocityX, velocityY, velocityZ);
        }
        double d = 0.0;
        for (BoundingBox bb : boundingBoxes) {
          BoundingBox boundingBox =
              getExtensionBoundingBox(offsetBoundingBox(bb), movingDirection, progress);
          BoundingBox entityBoundingBox = entity.getBoundingBox();
          if (boundingBox.intersects(entityBoundingBox)) {
            d = Math.max(d,
                getBoundingBoxesDistance(boundingBox, entityBoundingBox, movingDirection));
            if (d >= delta)
              break;
          }
        }
        if (d > 0.0) {
          d = Math.min(d, delta);
          localDirection.set(movingDirection);
          entity.move(MovementType.PISTON, new Vec3d(d * movingDirection.getOffsetX(),
              d * movingDirection.getOffsetY(), d * movingDirection.getOffsetZ()));
          if (!this.extending && this.source)
            pullEntity(entity, movingDirection, delta);
        }
      }
    }
  }

  public Direction getMovingDirection() {
    return isExtending() ? this.facing : this.facing.getOpposite();
  }

  private BoundingBox mergeBoundingBoxes(List<BoundingBox> boundingBoxes) {
    double minX = 0.0, minY = 0.0, minZ = 0.0;
    double maxX = 1.0, maxY = 1.0, maxZ = 1.0;
    for (BoundingBox boundingBox : boundingBoxes) {
      minX = Math.min(minX, boundingBox.minX);
      minY = Math.min(minY, boundingBox.minY);
      minZ = Math.min(minZ, boundingBox.minZ);
      maxX = Math.min(maxX, boundingBox.maxX);
      maxY = Math.min(maxY, boundingBox.maxY);
      maxZ = Math.min(maxZ, boundingBox.maxZ);
    }
    return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
  }

  private double getBoundingBoxesDistance(BoundingBox boundingBox1, BoundingBox boundingBox2,
      Direction direction) {
    switch (direction.getAxis()) {
      case X:
        if (direction.getDirection() == Direction.AxisDirection.POSITIVE)
          return boundingBox1.maxX - boundingBox2.minX;
        return boundingBox2.maxX - boundingBox1.minX;
      case Y:
        if (direction.getDirection() == Direction.AxisDirection.POSITIVE)
          return boundingBox1.maxY - boundingBox2.minY;
        return boundingBox2.maxY - boundingBox1.minY;
      default:
        if (direction.getDirection() == Direction.AxisDirection.POSITIVE)
          return boundingBox1.maxZ - boundingBox2.minZ;
        return boundingBox2.maxZ - boundingBox1.minZ;
    }
  }

  private BoundingBox offsetBoundingBox(BoundingBox boundingBox) {
    double progress = this.isExtending() ? this.nextProgress - 1.0F : 1.0F - this.nextProgress;
    return boundingBox.offset(this.pos.getX() + progress * this.facing.getOffsetX(),
        this.pos.getY() + progress * this.facing.getOffsetY(),
        this.pos.getZ() + progress * this.facing.getOffsetZ());
  }

  private BoundingBox getExtensionBoundingBox(BoundingBox boundingBox, Direction direction,
      double progress) {
    double offset = progress * direction.getDirection().offset();
    double minOffset = Math.min(offset, 0.0);
    double maxOffset = Math.max(offset, 0.0);
    switch (direction) {
      case UP:
      case DOWN:
        return new BoundingBox(boundingBox.minX, boundingBox.maxY + minOffset, boundingBox.minZ,
            boundingBox.maxX, boundingBox.maxY + maxOffset, boundingBox.maxZ);
      case NORTH:
      case SOUTH:
        return new BoundingBox(boundingBox.minX, boundingBox.minY, boundingBox.minZ + minOffset,
            boundingBox.maxX, boundingBox.maxY, boundingBox.minZ + maxOffset);
      default:
        return new BoundingBox(boundingBox.minX + minOffset, boundingBox.minY, boundingBox.minZ,
            boundingBox.maxX + maxOffset, boundingBox.maxY, boundingBox.minZ);
    }
  }

  private void pullEntity(Entity entity, Direction direction, double progress) {
    BoundingBox entityBoundingBox = entity.getBoundingBox();
    BoundingBox blockBoundingBox = VoxelShapes.fullCube().getBoundingBox().offset(this.pos);
    if (entityBoundingBox.intersects(blockBoundingBox)) {
      Direction opposite = direction.getOpposite();
      double distance =
          getBoundingBoxesDistance(entityBoundingBox, blockBoundingBox, opposite) + 0.01;
      if (Math.abs(distance - getBoundingBoxesDistance(entityBoundingBox,
          entityBoundingBox.intersection(blockBoundingBox), opposite)) < 0.01) {
        distance = Math.min(distance, progress) + 0.01;
        localDirection.set(direction);
        entity.move(MovementType.PISTON, new Vec3d(distance * opposite.getOffsetX(),
            distance * opposite.getOffsetY(), distance * opposite.getOffsetZ()));
        localDirection.set(null);
      }
    }
  }

  protected MovingStrongPistonBlock getContainerBlock() {
    return TmoBlocks.MOVING_STRONG_PISTON;
  }

  protected StrongPistonHeadBlock getHeadBlock() {
    return TmoBlocks.STRONG_PISTON_HEAD;
  }

  public void finishMovement() {
    if (this.world != null) {
      if (this.progress < 1.0F) {
        this.nextProgress = 1.0F;
        this.progress = this.nextProgress;
        this.world.removeBlockEntity(this.pos);
        this.invalidate();
        if (this.world.getBlockState(this.pos).getBlock() == getContainerBlock()) {
          BlockState state;
          if (this.source) {
            state = Blocks.AIR.getDefaultState();
            BlockPos basePos = this.pos.offset(this.facing.getOpposite());
            BlockState baseState = this.world.getBlockState(basePos);
            if (baseState.getBlock() instanceof StrongPistonBlock)
              this.world.setBlockState(basePos,
                  baseState.with(StrongPistonBlock.EXTENSION_TYPE, ExtensionType.EXTENDED), 16);
            this.updateBase = false;
          } else {
            state = Block.getRenderingState(this.pushedBlock, this.world, this.pos);
          }
          this.world.setBlockState(this.pos, state, 3);
          this.world.updateNeighbor(this.pos, state.getBlock(), this.pos);
        }
      } else {
        updateBase();
      }
    }
  }

  protected float getExtensionSpeed() {
    return 0.5F;
  }

  protected void updateBase() {
    if (this.updateBase && this.progress > 0.5625F) {
      BlockPos basePos = this.pos.offset(this.facing.getOpposite());
      BlockState baseState = this.world.getBlockState(basePos);
      if (baseState.getBlock() instanceof StrongPistonBlock)
        this.world.setBlockState(basePos,
            baseState.with(StrongPistonBlock.EXTENSION_TYPE, ExtensionType.EXTENDED), 16);
      this.updateBase = false;
    }
  }

  @Override
  public void tick() {
    this.progress = this.nextProgress;
    updateBase();
    if (this.progress >= 1.0f) {
      this.world.removeBlockEntity(this.pos);
      invalidate();
      if (this.pushedBlock != null
          && this.world.getBlockState(this.pos).getBlock() == getContainerBlock()) {
        BlockState state = Block.getRenderingState(this.pushedBlock, this.world, this.pos);
        if (state.isAir()) {
          this.world.setBlockState(this.pos, this.pushedBlock, 84);
          Block.replaceBlock(this.pushedBlock, state, this.world, this.pos, 3);
        } else {
          if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED))
            state = state.with(Properties.WATERLOGGED, false);
          this.world.setBlockState(this.pos, state, 67);
          this.world.updateNeighbor(this.pos, state.getBlock(), this.pos);
        }
      }
      return;
    }
    float nextNextProgress = this.nextProgress + getExtensionSpeed();
    pushEntities(nextNextProgress);
    this.nextProgress = nextNextProgress;
    if (this.nextProgress >= 1.0f)
      this.nextProgress = 1.0f;
  }

  @Override
  public void fromTag(CompoundTag compound) {
    super.fromTag(compound);
    this.pushedBlock = TagHelper.deserializeBlockState(compound.getCompound("blockState"));
    this.facing = Direction.byId(compound.getInt("facing"));
    this.nextProgress = compound.getFloat("progress");
    this.progress = this.nextProgress;
    this.extending = compound.getBoolean("extending");
    this.source = compound.getBoolean("source");
  }

  @Override
  public CompoundTag toTag(CompoundTag compound) {
    super.toTag(compound);
    compound.put("blockState", TagHelper.serializeBlockState(this.pushedBlock));
    compound.putInt("facing", this.facing.getId());
    compound.putFloat("progress", this.progress);
    compound.putBoolean("extending", this.extending);
    compound.putBoolean("source", this.source);
    return compound;
  }

  public VoxelShape getCollisionShape(BlockView view, BlockPos pos) {
    VoxelShape shape;
    if (!this.extending && this.source)
      shape = this.pushedBlock.with(StrongPistonBlock.EXTENSION_TYPE, ExtensionType.EXTENDED)
          .getCollisionShape(view, pos);
    else
      shape = VoxelShapes.empty();
    Direction direction5 = localDirection.get();
    if (this.nextProgress < 1.0 && direction5 == getMovingDirection())
      return shape;
    BlockState state;
    if (this.isSource())
      state = getHeadBlock().getDefaultState().with(StrongPistonHeadBlock.FACING, this.facing)
          .with(StrongPistonHeadBlock.SHORT, this.extending != 1.0f - this.nextProgress < 4.0f);
    else
      state = this.pushedBlock;
    float delta = isExtending() ? this.nextProgress - 1.0f : 1.0f - this.nextProgress;
    double xOffset = this.facing.getOffsetX() * delta;
    double yOffset = this.facing.getOffsetY() * delta;
    double zOffset = this.facing.getOffsetZ() * delta;
    return VoxelShapes.union(shape,
        state.getCollisionShape(view, pos).offset(xOffset, yOffset, zOffset));
  }

  static {
    localDirection = new ThreadLocal<Direction>() {
      protected Direction initialValue() {
        return null;
      }
    };
  }
}
