package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.blocks.BlockBrick;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.blocks.BlockExploder;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.BlockStorage;
import misterpemodder.tmo.main.blocks.BlockTransparent;
import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.containers.BlockTileEntity;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumAnvil;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.slab.BlockFullSlab;
import misterpemodder.tmo.main.blocks.slab.BlockHalfSlab;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public class ModBlocks {
	
	public static enum TheBlocks {
		BRICK(new BlockBrick()),
		ORE(new BlockOre()),
		FROZIUM_BLOCK(new BlockTransparent(EnumBlocksNames.FROZIUM_BLOCK, EnumBlocksValues.FROZIUM_BLOCK)),
		STORAGE_BLOCK(new BlockStorage()),
		DECORATION(new BlockDeco(EnumBlocksNames.DECORATION, EnumBlocksValues.DECORATION)),
		HALF_SLAB_BRICK(new BlockHalfSlab()),
		FULL_SLAB_BRICK(new BlockFullSlab((BlockHalfSlab)HALF_SLAB_BRICK.getBlock())),
		ANCIENT_GOLD_LAMP(new BlockLamp(EnumBlocksNames.ANCIENT_GOLD_LAMP, EnumBlocksValues.LAMP)),
		DARKANIUM_LAMP(new BlockLamp(EnumBlocksNames.DARKANIUM_LAMP, EnumBlocksValues.LAMP)),
		TITANIUM_CHEST(new BlockTitaniumChest()),
		TITANIUM_ANVIL(new BlockTitaniumAnvil()),
		EXPLODER(new BlockExploder()),
		;
		private BlockBase block;
		
		public BlockBase getBlock() {
			return this.block;
		}
		
		TheBlocks(BlockBase block) {
			this.block = block;
		}
		
	}
	
	@SubscribeEvent
	public static void registerBlocksEvent(RegistryEvent.Register<Block> event) {
		register(event.getRegistry());
	}
	
	private static void register(IForgeRegistry<Block> registry) {
		TMORefs.LOGGER.info("Registering Blocks...");
		for(TheBlocks b : TheBlocks.values()) {
			BlockBase block = b.getBlock();
			registry.register(block);
			if(block.hasOwnItemBlock()) ModItems.ITEM_BLOCKS.add(block.getItemBlock());
			if(block instanceof BlockTileEntity) {
				GameRegistry.registerTileEntity(((BlockTileEntity<?>)block).getTileEntityClass(), block.getRegistryName().toString());
			}
		}
	}
	
	public static void registerRenders() {
		for(TheBlocks bl : TheBlocks.values()) {
			BlockBase b = bl.getBlock();
			b.registerItemRender();
		}
	}
	
	public static void registerOreDict() {
		for (TheBlocks b : TheBlocks.values()) {
			b.getBlock().registerOreDict();
		}
	}
	
}
