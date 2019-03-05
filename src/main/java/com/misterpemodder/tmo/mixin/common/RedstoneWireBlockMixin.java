package com.misterpemodder.tmo.mixin.common;

import javax.annotation.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.misterpemodder.tmo.block.SpecialRedstoneWireBlock;
import com.misterpemodder.tmo.hook.BlockHook;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

@Mixin(RedstoneWireBlock.class)
public final class RedstoneWireBlockMixin {
  @Shadow
  private boolean wiresGivePower;
  private static boolean tmoWiresGivePower = true;

  @Inject(at = @At("HEAD"),
      method = "method_10482(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z",
      cancellable = true)
  private static void onCanWireConnect(BlockState state, Direction direction,
      CallbackInfoReturnable<Boolean> ci) {
    if (((BlockHook) state.getBlock()).tmoCanConnectToRedstone(state, direction))
      ci.setReturnValue(true);
  }

  @Inject(at = @At("HEAD"),
      method = "method_10478(Lnet/minecraft/world/BlockView;"
          + "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z",
      cancellable = true)
  private void onShouldConnectToSide(BlockView view, BlockPos pos, Direction direction,
      CallbackInfoReturnable<Boolean> ci) {
    if ((Object) this instanceof SpecialRedstoneWireBlock
        && ((SpecialRedstoneWireBlock) (Object) this).shouldConnectToSide(view, pos, direction))
      ci.setReturnValue(true);
  }

  @Redirect(
      at = @At(value = "INVOKE",
          target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"),
      method = "method_10479")
  private Block isBlockRedstoneWire(BlockState state) {
    Block block = state.getBlock();
    return block instanceof RedstoneWireBlock ? Blocks.REDSTONE_WIRE : block;
  }

  @Inject(at = @At(value = "RETURN", ordinal = 0),
      method = "method_10486(ILnet/minecraft/block/BlockState;)I", cancellable = true)
  private void on10486(int int_1, BlockState state, CallbackInfoReturnable<Integer> ci) {
    if (state.getBlock() instanceof RedstoneWireBlock) {
      int power = state.get(RedstoneWireBlock.POWER);
      ci.setReturnValue(power > int_1 ? power : int_1);
    }
  }

  @Redirect(
      at = @At(value = "FIELD", target = "Lnet/minecraft/block/RedstoneWireBlock;wiresGivePower:Z",
          opcode = Opcodes.PUTFIELD),
      method = "method_10481(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;"
          + "Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/BlockState;")
  private void setWiresGivePower(RedstoneWireBlock owner, boolean value) {
    ((RedstoneWireBlockMixin) (Object) owner).wiresGivePower = value;
    tmoWiresGivePower = value;
  }

  @Redirect(
      at = @At(value = "FIELD", target = "Lnet/minecraft/block/RedstoneWireBlock;wiresGivePower:Z",
          opcode = Opcodes.GETFIELD),
      method = "getStrongRedstonePower(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;"
          + "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I")
  private boolean getWireGivePower(RedstoneWireBlock owner) {
    return tmoWiresGivePower || ((RedstoneWireBlockMixin) (Object) owner).wiresGivePower;
  }

  @Redirect(
      at = @At(value = "FIELD", target = "Lnet/minecraft/block/RedstoneWireBlock;wiresGivePower:Z",
          opcode = Opcodes.GETFIELD),
      method = "getWeakRedstonePower(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/BlockView;"
          + "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)I")
  private boolean getWireGivePower2(RedstoneWireBlock owner) {
    return tmoWiresGivePower || ((RedstoneWireBlockMixin) (Object) owner).wiresGivePower;
  }

  @Shadow
  protected static boolean method_10482(BlockState blockState_1, @Nullable Direction direction_1) {
    return true;
  }

  @Redirect(at = @At(value = "INVOKE",
      target = "Lnet/minecraft/block/RedstoneWireBlock;method_10482(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;)Z"),
      method = "getRenderConnectionType(Lnet/minecraft/world/BlockView;"
          + "Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Lnet/minecraft/block/enums/WireConnection;")
  private boolean changeWireSideConnection(BlockState state, Direction direction, BlockView view,
      BlockPos pos, Direction direction_1) {
    return method_10482(state, direction) || (Object) this instanceof SpecialRedstoneWireBlock
        && ((SpecialRedstoneWireBlock) (Object) this).shouldConnectToSide(view, pos, direction);
  }
}
