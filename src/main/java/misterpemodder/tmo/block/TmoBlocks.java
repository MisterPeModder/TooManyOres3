package misterpemodder.tmo.block;

import java.util.HashMap;
import java.util.Map;
import misterpemodder.tmo.TmoConstants;
import misterpemodder.tmo.TooManyOres;
import net.fabricmc.fabric.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Settings;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.Item;
import net.minecraft.item.block.BlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class TmoBlocks {
  private static final Map<Identifier, Block> BLOCKS = new HashMap<>();
  private static final Map<Identifier, BlockItem> BLOCKS_ITEMS = new HashMap<>();

  public static final Block ANCIENT_GOLD_BRICKS = add("ancient_gold_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.GOLD).strength(2.0F, 10.0F)));
  public static final Block CHISELED_ANCIENT_GOLD_BRICKS = add("chiseled_ancient_gold_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.GOLD).strength(2.0F, 10.0F)));
  public static final Block DARKANIUM_BRICKS = add("darkanium_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.PURPLE).strength(2.0F, 10.0F)));
  public static final Block ENDSTONE_BRICKS = add("polished_endstone_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.SAND).strength(2.0F, 10.0F)));
  public static final Block FROZIUM_BRICKS = add("frozium_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.ICE).strength(2.0F, 10.0F)));
  public static final Block IGNUM_BRICKS = add("ignum_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.ORANGE).strength(2.0F, 10.0F)));
  public static final Block GOLD_BRICKS = add("gold_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.GOLD).strength(2.0F, 10.0F)));
  public static final Block PLATINUM_BRICKS = add("platinum_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.WHITE).strength(2.0F, 10.0F)));
  public static final Block CHISELED_PLATINUM_BRICKS = add("chiseled_platinum_bricks",
      new Block(Settings.of(Material.STONE, MaterialColor.WHITE).strength(2.0F, 10.0F)));

  public static final Block TITANIUM_ORE = add("titanium_ore", new TmoOreBlock());
  public static final Block ANCIENT_GOLD_ORE = add("ancient_gold_ore", new TmoOreBlock());
  public static final Block DARKANIUM_ORE = add("darkanium_ore", new TmoOreBlock());
  public static final Block NETHER_DARKANIUM_ORE = add("nether_darkanium_ore", new TmoOreBlock());
  public static final Block FROZIUM_ORE = add("frozium_ore", new TmoOreBlock());
  public static final Block IGNUM_ORE = add("ignum_ore", new TmoOreBlock());
  public static final Block NETHER_IGNUM_ORE = add("nether_ignum_ore", new TmoOreBlock());
  public static final Block PLATINUM_ORE = add("platinum_ore", new TmoOreBlock());
  public static final Block PLATINUM_ORE_NETHER = add("nether_platinum_ore", new TmoOreBlock());
  public static final Block ENDER_MATTER_ORE = add("ender_matter_ore", new TmoOreBlock());
  public static final Block COPPER_ORE = add("copper_ore", new TmoOreBlock());
  public static final Block CARBON_ORE = add("carbon_ore", new TmoOreBlock());

  public static final Block TITANIUM_BLOCK =
      add("titanium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE)
          .strength(6.0F, 15.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block POOR_TITANIUM_BLOCK = add("poor_titanium_block",
      new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE).strength(6.0F, 15.0F)
          .sounds(BlockSoundGroup.METAL).build()));
  public static final Block ANCIENT_GOLD_BLOCK =
      add("ancient_gold_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.GOLD)
          .strength(5.0F, 12.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block PLATINUM_BLOCK =
      add("platinum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.WHITE)
          .strength(5.0F, 12.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block DARKANIUM_BLOCK =
      add("darkanium_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN)
          .strength(5.0F, 12.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block FROZIUM_BLOCK =
      add("frozium_block", new TranslucentBlock(FabricBlockSettings.of(Material.ICE)
          .strength(3.0F, 10.0F).sounds(BlockSoundGroup.GLASS).friction(0.98F).build()));
  public static final Block IGNUM_BLOCK =
      add("ignum_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.ORANGE)
          .strength(5.0F, 10.0F).sounds(BlockSoundGroup.METAL).lightLevel(3).build()));
  public static final Block ENDER_MATTER_BLOCK =
      add("ender_matter_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.CYAN)
          .strength(5.0F, 10.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block COPPER_BLOCK =
      add("copper_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN)
          .strength(5.0F, 10.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block TITANITE_BLOCK =
      add("titanite_block", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE)
          .strength(5.0F, 12.0F).sounds(BlockSoundGroup.METAL).build()));

  private static <T extends Block> T add(String name, T block) {
    return add(name, block, true);
  }

  private static <T extends Block> T add(String name, T block, boolean hasBlockItem) {
    Identifier id = new Identifier(TmoConstants.MOD_ID, name);
    BLOCKS.put(id, block);
    if (hasBlockItem)
      BLOCKS_ITEMS.put(id,
          new BlockItem(block, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP)));
    return block;
  }

  public static void register(Registry<? super Block> blockRegistry,
      Registry<? super Item> itemRegistry) {
    TooManyOres.LOGGER.info("[TooManyOres] Registering {} blocks (with {} block items)...",
        BLOCKS.size(), BLOCKS_ITEMS.size());
    for (Map.Entry<Identifier, Block> entry : BLOCKS.entrySet())
      Registry.register(blockRegistry, entry.getKey(), entry.getValue());
    for (Map.Entry<Identifier, BlockItem> entry : BLOCKS_ITEMS.entrySet())
      Registry.register(itemRegistry, entry.getKey(), entry.getValue());
    BLOCKS.clear();
    TooManyOres.LOGGER.info("[TooManyOres] done!");
  }
}
