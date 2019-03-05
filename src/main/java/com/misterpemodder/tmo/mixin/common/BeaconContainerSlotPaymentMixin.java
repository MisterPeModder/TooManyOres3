package com.misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.misterpemodder.tmo.tag.TmoItemTags;
import net.minecraft.item.ItemStack;

@Mixin(targets = "net.minecraft.container.BeaconContainer$SlotPayment")
public final class BeaconContainerSlotPaymentMixin {
  @Inject(at = @At("HEAD"), method = "canInsert(Lnet/minecraft/item/ItemStack;)Z",
      cancellable = true)
  public void modifyCanInsert(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
    if (TmoItemTags.BEACON_PAYMENT.contains(stack.getItem()))
      ci.setReturnValue(true);
  }
}
