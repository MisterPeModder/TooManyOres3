package com.misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import com.misterpemodder.tmo.tag.TmoBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;

@Mixin(BeaconBlockEntity.class)
public final class BeaconBlockEntityMixin {
  @ModifyVariable(at = @At(value = "INVOKE_ASSIGN",
      target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;",
      ordinal = 1), method = "updateBeamColors()V", name = "block_2", ordinal = 0)
  private Block modifyBeaconBaseBlock(Block original) {
    return TmoBlockTags.BEACON_BASE.contains(original) ? Blocks.IRON_BLOCK : original;
  }
}
