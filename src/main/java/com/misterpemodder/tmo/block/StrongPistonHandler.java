package com.misterpemodder.tmo.block;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class StrongPistonHandler {
  private final World world;
  private final BlockPos posFrom;
  private final boolean flag;
  private final BlockPos posTo;
  private final Direction direction;
  private final List<BlockPos> movedBlocks;
  private final List<BlockPos> brokenBlocks;
  private final Direction direction2;

  public static final int PUSH_LIMIT = 24;

  public StrongPistonHandler(World world, BlockPos pos, Direction direction, boolean flag) {
    this.movedBlocks = new ArrayList<>();
    this.brokenBlocks = new ArrayList<>();
    this.world = world;
    this.posFrom = pos;
    this.direction2 = direction;
    this.flag = flag;
    if (flag) {
      this.direction = direction;
      this.posTo = pos.offset(direction);
    } else {
      this.direction = direction.getOpposite();
      this.posTo = pos.offset(direction, 2);
    }
  }

  public boolean calculatePush() {
    this.movedBlocks.clear();
    this.brokenBlocks.clear();
    BlockState targetState = this.world.getBlockState(this.posTo);
    if (!StrongPistonBlock.isMovable(targetState, this.world, this.posTo, this.direction, false,
        this.direction2)) {
      if (this.flag && targetState.getPistonBehavior() == PistonBehavior.DESTROY) {
        this.brokenBlocks.add(this.posTo);
        return true;
      }
      return false;
    } else {
      if (!tryMove(this.posTo, this.direction))
        return false;
      for (int i = 0; i < this.movedBlocks.size(); ++i) {
        // TODO: replace slime block by some kind of an interface.
        BlockPos pos = this.movedBlocks.get(i);
        if (this.world.getBlockState(pos).getBlock() == Blocks.SLIME_BLOCK && !canMove(pos))
          return false;
      }
      return true;
    }
  }

  private boolean tryMove(BlockPos targetPos, Direction direction) {
    BlockState targetState = this.world.getBlockState(targetPos);
    Block targetBlock = targetState.getBlock();
    if (targetState.isAir())
      return true;
    if (!StrongPistonBlock.isMovable(targetState, this.world, targetPos, this.direction, false,
        direction))
      return true;
    if (targetPos.equals(this.posFrom))
      return true;
    if (this.movedBlocks.contains(targetPos))
      return true;
    int blocksCount = 1;
    if (blocksCount + this.movedBlocks.size() > PUSH_LIMIT)
      return false;
    // TODO: replace slime block by some kind of an interface.
    while (targetBlock == Blocks.SLIME_BLOCK) {
      BlockPos pos = targetPos.offset(this.direction.getOpposite(), blocksCount);
      targetState = this.world.getBlockState(pos);
      targetBlock = targetState.getBlock();
      if (targetState.isAir() || !StrongPistonBlock.isMovable(targetState, this.world, pos,
          this.direction, false, this.direction.getOpposite()))
        break;
      if (pos.equals(this.posFrom))
        break;
      if (++blocksCount + this.movedBlocks.size() > PUSH_LIMIT)
        return false;
    }
    int integer7 = 0;
    for (int i = blocksCount - 1; i >= 0; --i) {
      this.movedBlocks.add(targetPos.offset(this.direction.getOpposite(), i));
      ++integer7;
    }
    int integer8 = 1;
    while (true) {
      BlockPos blockPos9 = targetPos.offset(this.direction, integer8);
      int integer10 = this.movedBlocks.indexOf(blockPos9);
      if (integer10 > -1) {
        this.shuffleMovedBlocksList(integer7, integer10);
        for (int i = 0; i <= integer10 + integer7; ++i) {
          BlockPos blockPos12 = this.movedBlocks.get(i);
          if (this.world.getBlockState(blockPos12).getBlock() == Blocks.SLIME_BLOCK
              && !this.canMove(blockPos12)) {
            return false;
          }
        }
        return true;
      }
      targetState = this.world.getBlockState(blockPos9);
      if (targetState.isAir()) {
        return true;
      }
      if (!StrongPistonBlock.isMovable(targetState, this.world, blockPos9, this.direction, true,
          this.direction) || blockPos9.equals(this.posFrom)) {
        return false;
      }
      if (targetState.getPistonBehavior() == PistonBehavior.DESTROY) {
        this.brokenBlocks.add(blockPos9);
        return true;
      }
      if (this.movedBlocks.size() >= PUSH_LIMIT) {
        return false;
      }
      this.movedBlocks.add(blockPos9);
      ++integer7;
      ++integer8;
    }
  }

  private void shuffleMovedBlocksList(int first, int second) {
    List<BlockPos> list1 = new ArrayList<>();
    List<BlockPos> list2 = new ArrayList<>();
    List<BlockPos> list3 = new ArrayList<>();
    list1.addAll(this.movedBlocks.subList(0, second));
    list2
        .addAll(this.movedBlocks.subList(this.movedBlocks.size() - first, this.movedBlocks.size()));
    list3.addAll(this.movedBlocks.subList(second, this.movedBlocks.size() - first));
    this.movedBlocks.clear();
    this.movedBlocks.addAll(list1);
    this.movedBlocks.addAll(list2);
    this.movedBlocks.addAll(list3);
  }

  private boolean canMove(BlockPos blockPos) {
    for (final Direction direction : Direction.values()) {
      if (direction.getAxis() != this.direction.getAxis()
          && !tryMove(blockPos.offset(direction), direction))
        return false;
    }
    return true;
  }

  public List<BlockPos> getMovedBlocks() {
    return this.movedBlocks;
  }

  public List<BlockPos> getBrokenBlocks() {
    return this.brokenBlocks;
  }
}
