package com.misterpemodder.tmo.block;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.misterpemodder.tmo.TmoConstants;
import com.misterpemodder.tmo.block.ExploderBlock.ExploderType;
import com.misterpemodder.tmo.block.SpecialRedstoneWireBlock.WireType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Settings;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.Item;
import net.minecraft.item.StringItem;
import net.minecraft.item.block.BlockItem;
import net.minecraft.item.block.WallStandingBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public final class TmoBlocks {
  private static final Logger LOGGER = LogManager.getLogger();
  private static final Map<Identifier, Block> BLOCKS = new HashMap<>();
  private static final Map<Identifier, BlockItem> BLOCK_ITEMS = new HashMap<>();

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

  public static final TitaniumAnvilBlock TITANIUM_ANVIL = add("titanium_anvil",
      new TitaniumAnvilBlock(FabricBlockSettings.of(Material.ANVIL, MaterialColor.BLUE)
          .strength(10.0F, 1200.0F).sounds(BlockSoundGroup.ANVIL).build()));

  public static final StrongRedstoneBlock STRONG_REDSTONE_BLOCK = add("strong_redstone_block",
      new StrongRedstoneBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON)
          .strength(5.0F, 7.0F).sounds(BlockSoundGroup.METAL).build()));

  public static final Block MACHINE_CASING = add("machine_casing",
      new CutoutBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON)
          .strength(5.0F, 7.0F).sounds(BlockSoundGroup.METAL).build()),
      Rarity.RARE);
  public static final Block BASIC_MACHINE_CASING = add("basic_machine_casing",
      new CutoutBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON)
          .strength(5.0F, 7.0F).sounds(BlockSoundGroup.METAL).build()));
  public static final Block IMPROVED_MACHINE_CASING = add("improved_machine_casing",
      new CutoutBlock(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON)
          .strength(5.0F, 7.0F).sounds(BlockSoundGroup.METAL).build()),
      Rarity.EPIC);

  public static final ExploderBlock EXPLODER = add("exploder",
      new ExploderBlock(ExploderType.NORMAL,
          FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).strength(5.0F, 0.0F)
              .sounds(BlockSoundGroup.METAL).build()));
  public static final ExploderBlock FIERY_EXPLODER = add("fiery_exploder",
      new ExploderBlock(ExploderType.FIERY,
          FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).strength(5.0F, 0.0F)
              .sounds(BlockSoundGroup.METAL).build()),
      Rarity.UNCOMMON);
  public static final ExploderBlock SUPERCHARGED_EXPLODER = add("supercharged_exploder",
      new ExploderBlock(ExploderType.SUPERCHARGED,
          FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).strength(5.0F, 0.0F)
              .sounds(BlockSoundGroup.METAL).build()),
      Rarity.RARE);

  public static final SpecialRedstoneWireBlock TITANIUM_REDSTONE_WIRE =
      add("titanium_redstone_wire",
          new SpecialRedstoneWireBlock(WireType.WEAK, Settings.copy(Blocks.REDSTONE_WIRE)),
          (BlockItem) null);
  public static final SpecialRedstoneWireBlock COPPER_REDSTONE_WIRE = add("copper_redstone_wire",
      new SpecialRedstoneWireBlock(WireType.STRONG, Settings.copy(Blocks.REDSTONE_WIRE)),
      (BlockItem) null);
  public static final WeakRedstoneTorchBlock WEAK_REDSTONE_TORCH = add("weak_redstone_torch",
      new WeakRedstoneTorchBlock(Settings.copy(Blocks.REDSTONE_TORCH)), (BlockItem) null);
  public static final WeakRedstoneTorchWallBlock WEAK_REDSTONE_WALL_TORCH = add(
      "weak_redstone_wall_torch",
      new WeakRedstoneTorchWallBlock(Settings.copy(Blocks.REDSTONE_WALL_TORCH)), (BlockItem) null);

  static {
    addBlockItem("titanium_redstone", new StringItem(TITANIUM_REDSTONE_WIRE,
        new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP)));
    addBlockItem("copper_redstone", new StringItem(COPPER_REDSTONE_WIRE,
        new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP)));
    addBlockItem("weak_redstone_torch", new WallStandingBlockItem(WEAK_REDSTONE_TORCH,
        WEAK_REDSTONE_WALL_TORCH, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP)));
  }

  private static <T extends BlockItem> T addBlockItem(String name, T blockItem) {
    if (blockItem != null)
      BLOCK_ITEMS.put(new Identifier(TmoConstants.MOD_ID, name), blockItem);
    return blockItem;
  }

  private static <T extends Block> T add(String name, T block) {
    return add(name, block,
        new BlockItem(block, new Item.Settings().itemGroup(TmoConstants.ITEM_GROUP)));
  }

  private static <T extends Block> T add(String name, T block, Rarity rarity) {
    return add(name, block, new BlockItem(block,
        new Item.Settings().rarity(rarity).itemGroup(TmoConstants.ITEM_GROUP)));
  }

  private static <T extends Block> T add(String name, T block, @Nullable BlockItem blockItem) {
    Identifier id = new Identifier(TmoConstants.MOD_ID, name);
    BLOCKS.put(id, block);
    if (blockItem != null)
      BLOCK_ITEMS.put(id, blockItem);
    return block;
  }

  public static void register(Registry<? super Block> blockRegistry,
      Registry<? super Item> itemRegistry) {
    LOGGER.info("[TooManyOres] Registering {} blocks (with {} block items).", BLOCKS.size(),
        BLOCK_ITEMS.size());
    for (Map.Entry<Identifier, Block> entry : BLOCKS.entrySet())
      Registry.register(blockRegistry, entry.getKey(), entry.getValue());
    for (Map.Entry<Identifier, BlockItem> entry : BLOCK_ITEMS.entrySet())
      Registry.register(itemRegistry, entry.getKey(), entry.getValue());
    BLOCKS.clear();
    BLOCK_ITEMS.clear();
  }
}
