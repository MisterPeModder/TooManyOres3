package com.misterpemodder.tmo.block;

import java.util.Random;
import javax.annotation.Nullable;
import com.misterpemodder.tmo.hook.BlockHook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.VerticalEntityPosition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class ExploderBlock extends Block implements BlockHook {
  public final ExploderType type;
  public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
  public static final VoxelShape COLLISION_SHAPE =
      Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);

  public ExploderBlock(ExploderType type, Block.Settings settings) {
    super(settings);
    setDefaultState(getStateFactory().getDefaultState().with(ACTIVATED, false));
    this.type = type;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
    return getDefaultState().with(ACTIVATED, false);
  }

  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockView blockView_1, BlockPos pos,
      VerticalEntityPosition verticalEntityPosition_1) {
    return COLLISION_SHAPE;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public boolean hasSolidTopSurface(BlockState state, BlockView blockView_1, BlockPos pos) {
    return true;
  }

  @Override
  public boolean tmoCanConnectToRedstone(BlockState state, @Nullable Direction direction) {
    return true;
  }

  protected void triggerExplosion(World world, BlockPos pos) {
    BlockState state = world.getBlockState(pos);
    if (!state.get(ACTIVATED)) {
      BlockPos p = new BlockPos(pos);
      world.setBlockState(pos, world.getBlockState(pos).with(ACTIVATED, true));
      if (world.isClient) {
        world.playSound(p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_TNT_PRIMED,
            SoundCategory.BLOCK, 1.0F, 1.5F, false);
      } else {
        world.playSound((PlayerEntity) null, p.getX(), p.getY(), p.getZ(),
            SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCK, 1.0F, 1.5F);
      }
      world.getBlockTickScheduler().schedule(p, this, 6);
    }
  }

  @Override
  public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
    if (state.get(ACTIVATED)) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      this.type.explode(null, world, pos);
    }
  }

  @Override
  public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity_1) {
    triggerExplosion(world, pos);
  }

  private boolean isPowered(World world, BlockPos pos) {
    if (world.isReceivingRedstonePower(pos)) {
      return true;
    } else {
      for (Direction side : Direction.Type.HORIZONTAL) {
        BlockState state = world.getBlockState(pos.offset(side));
        if (state.contains(Properties.POWER) && state.get(Properties.POWER) > 0)
          return true;
      }
    }
    return false;
  }

  @Override
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block_1,
      BlockPos fromPos) {
    if (fromPos.equals(pos.up()) || isPowered(world, pos))
      triggerExplosion(world, pos);
  }

  @Override
  public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState blockState_2) {
    if (isPowered(world, pos))
      triggerExplosion(world, pos);
  }

  @Override
  public void onDestroyedByExplosion(World world, BlockPos pos, Explosion explosion) {
    if (!world.isClient) {
      world.setBlockState(pos, getDefaultState(), 2);
      triggerExplosion(world, pos);
    }
  }

  @Override
  public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player,
      Hand hand_1, BlockHitResult blockHitResult_1) {
    ItemStack heldItem = player.getStackInHand(hand_1);
    if (heldItem.getItem() instanceof FlintAndSteelItem
        || heldItem.getItem() instanceof FireChargeItem) {
      heldItem.applyDamage(1, player);
      triggerExplosion(world, pos);
      return true;
    }
    return super.activate(state, world, pos, player, hand_1, blockHitResult_1);
  }

  @Override
  public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
    if (player != null && !player.abilities.creativeMode) {
      if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH,
          player.getStackInHand(player.getActiveHand())) == 0) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        this.type.explode(null, world, pos);
      }
    }
    super.onBreak(world, pos, state, player);
  }

  @Override
  public boolean shouldDropItemsOnExplosion(Explosion explosion) {
    return false;
  }

  @Override
  public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    if (this.type.spawnFire && random.nextInt(5) == 0) {
      double d0 = pos.getX() + 0.55D - random.nextFloat() * 0.1F;
      double d1 = pos.getY() + 1.05D - random.nextFloat() * 0.1F;
      double d2 = pos.getZ() + 0.55D - random.nextFloat() * 0.1F;
      double d3 = 0.4F - (random.nextFloat() + random.nextFloat()) * 0.4F;
      world.addParticle(ParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3,
          random.nextGaussian() * 0.005D, random.nextGaussian() * 0.0005D,
          random.nextGaussian() * 0.005D);
    }
  }

  @Override
  protected void appendProperties(StateFactory.Builder<Block, BlockState> factory) {
    factory.with(ACTIVATED);
  }

  public static interface IExploderType {
    public void explode(@Nullable Entity cause, World world, BlockPos pos);
  }

  public static enum ExploderType implements IExploderType {
    NORMAL(5.5F, false), FIERY(5F, true), SUPERCHARGED(16F, false);

    public final float power;
    public final boolean spawnFire;

    private ExploderType(float power, boolean spawnFire) {
      this.power = power;
      this.spawnFire = spawnFire;
    }

    @Override
    public void explode(@Nullable Entity cause, World world, BlockPos pos) {
      world.createExplosion(cause, pos.getX(), pos.getY() + 1.0, pos.getZ(), this.power,
          this.spawnFire, true);
    }
  }
}
