package com.misterpemodder.tmo;

import com.misterpemodder.tmo.block.entity.TitaniumAnvilBlockEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public final class TooManyOresNetworking implements ModInitializer {
  public static final Identifier TITANIUM_ANVIL_SYNC =
      new Identifier(TmoConstants.MOD_ID, "titanium_anvil_sync");

  @Override
  public void onInitialize() {
    ClientSidePacketRegistry.INSTANCE.register(TITANIUM_ANVIL_SYNC, (context, buffer) -> {
      if (context.getPlayer() != null && context.getPlayer().getEntityWorld() != null) {
        BlockEntity be = context.getPlayer().getEntityWorld().getBlockEntity(buffer.readBlockPos());
        if (be instanceof TitaniumAnvilBlockEntity)
          ((TitaniumAnvilBlockEntity) be)
              .setHammerStack(ItemStack.fromTag(buffer.readCompoundTag()));
      }
    });
  }
}
