package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.blocks.BlockBrick;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.BlockStorage;
import misterpemodder.tmo.main.blocks.BlockTest;
import misterpemodder.tmo.main.blocks.BlockTransparent;
import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.containers.BlockItemKeeper;
import misterpemodder.tmo.main.blocks.containers.BlockTileEntity;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.slab.BlockFullSlab;
import misterpemodder.tmo.main.blocks.slab.BlockHalfSlab;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
	
	public static enum Blocks {
		TEST_BLOCK(new BlockTest()),
		BRICK(new BlockBrick()),
		ORE(new BlockOre()),
		FROZIUM_BLOCK(new BlockTransparent(EnumBlocksNames.FROZIUM_BLOCK, EnumBlocksValues.FROZIUM_BLOCK)),
		STORAGE_BLOCK(new BlockStorage()),
		DECORATION(new BlockDeco(EnumBlocksNames.DECORATION, EnumBlocksValues.DECORATION)),
		HALF_SLAB_BRICK(new BlockHalfSlab()),
		FULL_SLAB_BRICK(new BlockFullSlab((BlockHalfSlab)HALF_SLAB_BRICK.getBlock())),
		ANCIENT_GOLD_LAMP(new BlockLamp(EnumBlocksNames.ANCIENT_GOLD_LAMP, EnumBlocksValues.LAMP)),
		DARKANIUM_LAMP(new BlockLamp(EnumBlocksNames.DARKANIUM_LAMP, EnumBlocksValues.LAMP)),
		KEEPER_BLOCK(new BlockItemKeeper()),
		TITANIUM_CHEST(new BlockTitaniumChest()),
		
		;
		private BlockBase block;
		
		public BlockBase getBlock() {
			return this.block;
		}
		
		Blocks(BlockBase block) {
			this.block = block;
		}
		
	}
	
	public static void register() {
		for(Blocks b : Blocks.values()) {
			BlockBase block = b.getBlock();
			GameRegistry.register(block);
			if(block.hasOwnItemBlock()) GameRegistry.register(block.getItemBlock());
			if(block instanceof BlockTileEntity) {
				GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), block.getRegistryName().toString());
			}
		}
	}
	
	public static void registerRenders() {
		for(Blocks bl : Blocks.values()) {
			BlockBase b = bl.getBlock();
			b.registerItemRender();
		}
	}
	
	public static void registerOreDict() {
		for (Blocks b : Blocks.values()) {
			b.getBlock().registerOreDict();
		}
	}
	
}
