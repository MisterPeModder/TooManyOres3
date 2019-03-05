package com.misterpemodder.tmo.client;

import com.misterpemodder.tmo.block.entity.TitaniumAnvilBlockEntity;
import com.misterpemodder.tmo.client.render.TitaniumAnvilBlockEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.render.BlockEntityRendererRegistry;

public final class TooManyOresClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    BlockEntityRendererRegistry.INSTANCE.register(TitaniumAnvilBlockEntity.class,
        new TitaniumAnvilBlockEntityRenderer());
  }
}
