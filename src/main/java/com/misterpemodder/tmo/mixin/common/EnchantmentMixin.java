package com.misterpemodder.tmo.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.misterpemodder.tmo.hook.ItemHook;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;

@Mixin(Enchantment.class)
public final class EnchantmentMixin {
  @Inject(at = @At("HEAD"), method = "isAcceptableItem(Lnet/minecraft/item/ItemStack;)Z",
      cancellable = true)
  private void changeAcceptableItem(ItemStack stack, CallbackInfoReturnable<Boolean> ci) {
    if (((ItemHook) stack.getItem()).tmoCanApplyEnchantment(stack, (Enchantment) (Object) this))
      ci.setReturnValue(true);
  }
}
