package com.misterpemodder.tmo.block.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.misterpemodder.tmo.TmoConstants;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class TmoBlockEntityTypes {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final Map<Identifier, BlockEntityType<?>> BE_TYPES = new HashMap<>();

  public static final BlockEntityType<TitaniumAnvilBlockEntity> TITANIUM_ANVIL =
      create("titanium_anvil", TitaniumAnvilBlockEntity::new);

  private static <BE extends BlockEntity> BlockEntityType<BE> create(String id,
      Supplier<BE> createBe) {
    BlockEntityType<BE> type = BlockEntityType.Builder.create(createBe).build(null);
    BE_TYPES.put(new Identifier(TmoConstants.MOD_ID, id), type);
    return type;
  }

  public static void register(Registry<? super BlockEntityType<?>> registry) {
    LOGGER.info("[TooManyOres] Registering {} block entity types.", BE_TYPES.size());
    for (Map.Entry<Identifier, BlockEntityType<?>> entry : BE_TYPES.entrySet())
      Registry.register(registry, entry.getKey(), entry.getValue());
    BE_TYPES.clear();
  }
}
