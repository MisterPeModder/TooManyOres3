package com.misterpemodder.tmo.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.misterpemodder.tmo.block.SpecialRedstoneWireBlock;
import net.minecraft.client.render.block.BlockColorMap;

@Mixin(BlockColorMap.class)
public final class BlockColorMapMixin {
  @Inject(at = @At("RETURN"), method = "create()Lnet/minecraft/client/render/block/BlockColorMap;")
  private static void onCreate(CallbackInfoReturnable<BlockColorMap> ci) {
    SpecialRedstoneWireBlock.registerBlockColorMapper(ci.getReturnValue());
  }
}
