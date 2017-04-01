package misterpemodder.tmo.main.init;

import misterpemodder.tmo.main.blocks.BlockBrick;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.blocks.BlockExploder;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.BlockPistonStrongBase;
import misterpemodder.tmo.main.blocks.BlockRedstoneStrong;
import misterpemodder.tmo.main.blocks.BlockStorage;
import misterpemodder.tmo.main.blocks.BlockStrongPistonExtension;
import misterpemodder.tmo.main.blocks.BlockStrongPistonMoving;
import misterpemodder.tmo.main.blocks.BlockTMOStairs;
import misterpemodder.tmo.main.blocks.BlockTransparent;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumAnvil;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.blocks.containers.IBlockTileEntity;
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
		MACHINE_CASING(new BlockMachine(EnumBlocksNames.MACHINE_CASING, EnumBlocksValues.MetalBlocks.MACHINE)),
		STRONG_REDSTONE_BLOCK(new BlockRedstoneStrong()),
		EXPLODER(new BlockExploder()),
		ANCIENT_GOLD_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.ANCIENT_GOLD)),
		DARKANIUM_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.DARKANIUM)),
		ENDSTONE_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.ENDSTONE)),
		FROZIUM_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.FROZIUM)),
		IGNUM_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.IGNUM)),
		GOLD_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.GOLD)),
		PLATINUM_STAIRS(new BlockTMOStairs((BlockMulti) BRICK.getBlock(), BlockBrick.EnumVariant.PLATINUM)),
		STRONG_PISTON(new BlockPistonStrongBase(EnumBlocksNames.STRONG_PISTON_BASE, false)),
		STRONG_PISTON_STICKY(new BlockPistonStrongBase(EnumBlocksNames.STRONG_PISTON_BASE_STICKY, true)),
		STRONG_PISTON_EXTENSION(new BlockStrongPistonExtension()),
		STRONG_PISTON_MOVING(new BlockStrongPistonMoving()),
		INJECTOR(new BlockInjector()),
		;
		private IBlockTMO block;
		
		public Block getBlock() {
			return (Block)this.block;
		}
		
		public IBlockTMO getBlockTMO() {
			return this.block;
		}
		
		TheBlocks(IBlockTMO block) {
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
			IBlockTMO block = b.getBlockTMO();
			registry.register(b.getBlock());
			if(block.hasOwnItemBlock())
				ModItems.ITEM_BLOCKS.add(block.getItemBlock());
			if(block instanceof IBlockTileEntity) {
				GameRegistry.registerTileEntity(((IBlockTileEntity<?>)block).getTileEntityClass(), b.getBlock().getRegistryName().toString());
			}
		}
	}
	
	public static void registerRenders() {
		for(TheBlocks bl : TheBlocks.values()) {
			IBlockTMO b = bl.getBlockTMO();
			if(b.hasOwnItemBlock())
				b.registerItemRender();
		}
	}
	
	public static void registerOreDict() {
		for (TheBlocks b : TheBlocks.values()) {
			b.getBlockTMO().registerOreDict();
		}
	}
	
}
