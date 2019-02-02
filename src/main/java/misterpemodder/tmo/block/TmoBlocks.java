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
import net.minecraft.block.OreBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
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

  public static final SlabBlock ANCIENT_GOLD_BRICK_SLAB =
      add("ancient_gold_brick_slab", new SlabBlock(Settings.copy(ANCIENT_GOLD_BRICKS)));
  public static final SlabBlock DARKANIUM_BRICK_SLAB =
      add("darkanium_brick_slab", new SlabBlock(Settings.copy(DARKANIUM_BRICKS)));
  public static final SlabBlock ENDSTONE_BRICK_SLAB =
      add("polished_endstone_brick_slab", new SlabBlock(Settings.copy(ENDSTONE_BRICKS)));
  public static final SlabBlock FROZIUM_BRICK_SLAB =
      add("frozium_brick_slab", new SlabBlock(Settings.copy(FROZIUM_BRICKS)));
  public static final SlabBlock IGNUM_BRICK_SLAB =
      add("ignum_brick_slab", new SlabBlock(Settings.copy(IGNUM_BRICKS)));
  public static final SlabBlock GOLD_BRICK_SLAB =
      add("gold_brick_slab", new SlabBlock(Settings.copy(GOLD_BRICKS)));
  public static final SlabBlock PLATINUM_BRICK_SLAB =
      add("platinum_brick_slab", new SlabBlock(Settings.copy(PLATINUM_BRICKS)));

  public static final StairsBlock ANCIENT_GOLD_BRICK_STAIRS =
      add("ancient_gold_brick_stairs", new TmoStairsBlock(ANCIENT_GOLD_BRICKS));
  public static final StairsBlock DARKANIUM_BRICK_STAIRS =
      add("darkanium_brick_stairs", new TmoStairsBlock(DARKANIUM_BRICKS));
  public static final StairsBlock ENDSTONE_BRICK_STAIRS =
      add("polished_endstone_brick_stairs", new TmoStairsBlock(ENDSTONE_BRICKS));
  public static final StairsBlock FROZIUM_BRICK_STAIRS =
      add("frozium_brick_stairs", new TmoStairsBlock(FROZIUM_BRICKS));
  public static final StairsBlock IGNUM_BRICK_STAIRS =
      add("ignum_brick_stairs", new TmoStairsBlock(IGNUM_BRICKS));
  public static final StairsBlock GOLD_BRICK_STAIRS =
      add("gold_brick_stairs", new TmoStairsBlock(GOLD_BRICKS));
  public static final StairsBlock PLATINUM_BRICK_STAIRS =
      add("platinum_brick_stairs", new TmoStairsBlock(PLATINUM_BRICKS));

  public static final OreBlock TITANIUM_ORE = add("titanium_ore", new TmoOreBlock());
  public static final OreBlock ANCIENT_GOLD_ORE = add("ancient_gold_ore", new TmoOreBlock());
  public static final OreBlock DARKANIUM_ORE = add("darkanium_ore", new TmoOreBlock());
  public static final OreBlock NETHER_DARKANIUM_ORE =
      add("nether_darkanium_ore", new TmoOreBlock());
  public static final OreBlock FROZIUM_ORE = add("frozium_ore", new TmoOreBlock());
  public static final OreBlock IGNUM_ORE = add("ignum_ore", new TmoOreBlock());
  public static final OreBlock NETHER_IGNUM_ORE = add("nether_ignum_ore", new TmoOreBlock());
  public static final OreBlock PLATINUM_ORE = add("platinum_ore", new TmoOreBlock());
  public static final OreBlock PLATINUM_ORE_NETHER = add("nether_platinum_ore", new TmoOreBlock());
  public static final OreBlock ENDER_MATTER_ORE = add("ender_matter_ore", new TmoOreBlock());
  public static final OreBlock COPPER_ORE = add("copper_ore", new TmoOreBlock());
  public static final OreBlock CARBON_ORE = add("carbon_ore", new TmoOreBlock());

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

  public static final Block TITANIUM_PLATING =
      add("titanium_plating", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BLUE)
          .strength(2.0F, 1.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block COPPER_DECORATION =
      add("copper_decoration", new Block(FabricBlockSettings.of(Material.METAL, MaterialColor.BROWN)
          .strength(2.0F, 1.0F).sounds(BlockSoundGroup.METAL).build()));

  public static final LampBlock ANCIENT_GOLD_LAMP =
      add("ancient_gold_lamp", new LampBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP)
          .lightLevel(15).strength(0.3F, 0.3F).sounds(BlockSoundGroup.GLASS).build()));
  public static final LampBlock DARKANIUM_LAMP =
      add("darkanium_lamp", new LampBlock(FabricBlockSettings.of(Material.REDSTONE_LAMP)
          .lightLevel(15).strength(0.3F, 0.3F).sounds(BlockSoundGroup.GLASS).build()));

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
