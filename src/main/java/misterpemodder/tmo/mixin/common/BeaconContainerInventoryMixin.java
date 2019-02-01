package misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import misterpemodder.tmo.tags.TmoItemTags;
import net.minecraft.item.ItemStack;

@Mixin(targets = "net.minecraft.container.BeaconContainer$1")
public final class BeaconContainerInventoryMixin {
  @Inject(at = @At("HEAD"), method = "isValidInvStack(ILnet/minecraft/item/ItemStack;)Z",
      cancellable = true)
  public void modifyValidInvStack(int int_1, ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
    if (TmoItemTags.BEACON_PAYMENT.contains(stack.getItem()))
      ci.setReturnValue(true);
  }
}
