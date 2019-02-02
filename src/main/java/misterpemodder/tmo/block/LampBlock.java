package misterpemodder.tmo.block;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateFactory.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LampBlock extends Block {
  public static final BooleanProperty LIT = Properties.LIT;
  public static final BooleanProperty INVERTED = Properties.INVERTED;

  public LampBlock(Block.Settings settings) {
    super(settings);
    setDefaultState(getDefaultState().with(LIT, false).with(INVERTED, false));
  }

  @Override
  public boolean isSimpleFullBlock(BlockState blockState_1, BlockView blockView_1,
      BlockPos blockPos_1) {
    return super.isSimpleFullBlock(blockState_1, blockView_1, blockPos_1);
  }

  @Override
  public int getLuminance(BlockState state) {
    return state.get(LIT) ? super.getLuminance(state) : 0;
  }

  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext itemPlacementContext_1) {
    return this.getDefaultState();
  }

  @Override
  public boolean activate(BlockState state, World world, BlockPos pos, PlayerEntity player,
      Hand hand, BlockHitResult hit) {
    ItemStack heldStack = player.getStackInHand(hand);
    if (heldStack == null)
      return false;
    Item item = heldStack.getItem();
    // field_8530 -> REDSTONE_TORCH
    if ((item == Items.field_8530
    /* || item == BlockItem.getItemFromBlock(TmoBlocks.WEAK_REDSTONE_TORCH.getBlock()) */)
        && player.abilities.allowModifyWorld) {
      if (!world.isClient) {
        // method_11572 -> cycleProperty
        world.setBlockState(pos, state.method_11572(INVERTED), 2);
        world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCK, 0.3F,
            state.get(LIT) ? 0.55F : 0.5F);
        world.getBlockTickScheduler().schedule(pos, this, 4);
        world.updateNeighbors(pos, this);
      }
      return true;
    }
    return false;
  }

  private boolean isValidState(BlockState state, boolean blockPowered) {
    if (state.get(LIT))
      return state.get(INVERTED) ^ blockPowered;
    else
      return state.get(INVERTED) && blockPowered;
  }

  private BlockState getValidState(BlockState state, boolean blockPowered) {
    if (state.get(INVERTED))
      return state.with(LIT, !blockPowered);
    else
      return state.with(LIT, blockPowered);
  }

  @Override
  public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block_1,
      BlockPos neighbor) {
    if (!world.isClient && !isValidState(state, world.isReceivingRedstonePower(pos)))
      world.getBlockTickScheduler().schedule(pos, this, 4);
  }

  @Override
  public void onScheduledTick(BlockState state, World world, BlockPos pos, Random random) {
    if (!world.isClient) {
      boolean blockPowered = world.isReceivingRedstonePower(pos);
      if (!isValidState(state, blockPowered)) {
        world.setBlockState(pos, getValidState(state, blockPowered), 2);
      }
    }
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> stateFactory) {
    stateFactory.with(LIT).with(INVERTED);
  }
}
