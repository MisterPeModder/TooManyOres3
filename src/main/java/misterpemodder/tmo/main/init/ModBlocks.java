package misterpemodder.tmo.main.init;

import misterpemodder.hc.main.blocks.BlockMulti;
import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.hc.main.blocks.properties.IHexianBlockList;
import misterpemodder.hc.main.utils.RegistryHelper;
import misterpemodder.tmo.main.blocks.BlockBrick;
import misterpemodder.tmo.main.blocks.BlockDeco;
import misterpemodder.tmo.main.blocks.BlockExploder;
import misterpemodder.tmo.main.blocks.BlockLamp;
import misterpemodder.tmo.main.blocks.BlockMachineCasing;
import misterpemodder.tmo.main.blocks.BlockOre;
import misterpemodder.tmo.main.blocks.BlockStorage;
import misterpemodder.tmo.main.blocks.BlockTMOStairs;
import misterpemodder.tmo.main.blocks.BlockTransparent;
import misterpemodder.tmo.main.blocks.containers.BlockDestabilizer;
import misterpemodder.tmo.main.blocks.containers.BlockInjector;
import misterpemodder.tmo.main.blocks.containers.BlockThermoelectricGenerator;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumAnvil;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.redstone.BlockPistonStrongBase;
import misterpemodder.tmo.main.blocks.redstone.BlockRedstoneStrong;
import misterpemodder.tmo.main.blocks.redstone.BlockSpecialRedstoneTorch;
import misterpemodder.tmo.main.blocks.redstone.BlockSpecialRedstoneWire;
import misterpemodder.tmo.main.blocks.redstone.BlockStrongPistonExtension;
import misterpemodder.tmo.main.blocks.redstone.BlockStrongPistonMoving;
import misterpemodder.tmo.main.blocks.slab.BlockFullSlab;
import misterpemodder.tmo.main.blocks.slab.BlockHalfSlab;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TMORefs.MOD_ID)
public class ModBlocks {
	
	public static enum TheBlocks implements IHexianBlockList {
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
		MACHINE_CASING(new BlockMachineCasing()),
		STRONG_REDSTONE_BLOCK(new BlockRedstoneStrong()),
		EXPLODER(new BlockExploder()),
		ANCIENT_GOLD_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.ANCIENT_GOLD)),
		DARKANIUM_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.DARKANIUM)),
		ENDSTONE_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.ENDSTONE)),
		FROZIUM_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.FROZIUM)),
		IGNUM_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.IGNUM)),
		GOLD_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.GOLD)),
		PLATINUM_STAIRS(new BlockTMOStairs((BlockMulti<?>) BRICK.getBlock(), BlockBrick.EnumBrickVariant.PLATINUM)),
		STRONG_PISTON(new BlockPistonStrongBase(EnumBlocksNames.STRONG_PISTON_BASE, false)),
		STRONG_PISTON_STICKY(new BlockPistonStrongBase(EnumBlocksNames.STRONG_PISTON_BASE_STICKY, true)),
		STRONG_PISTON_EXTENSION(new BlockStrongPistonExtension()),
		STRONG_PISTON_MOVING(new BlockStrongPistonMoving()),
		INJECTOR(new BlockInjector()),
		CRYSTAL_DESTABILIZER(new BlockDestabilizer()),
		THERMOELECTRIC_GENERATOR(new BlockThermoelectricGenerator()),
		TITANIUM_REDSTONE(new BlockSpecialRedstoneWire(EnumBlocksNames.TITANIUM_REDSTONE)),
		COPPER_REDSTONE(new BlockSpecialRedstoneWire(EnumBlocksNames.COPPER_REDSTONE)),
		WEAK_REDSTONE_TORCH(new BlockSpecialRedstoneTorch(true, EnumBlocksNames.WEAK_REDSTONE_TORCH)),
		WEAK_REDSTONE_TORCH_UNLIT(new BlockSpecialRedstoneTorch(false, EnumBlocksNames.WEAK_REDSTONE_TORCH_UNLIT)),
		;
		private IHexianBlock block;
		
		@Override
		public Block getBlock() {
			return (Block)this.block;
		}
		
		@Override
		public IHexianBlock getHexianBlock() {
			return this.block;
		}
		
		TheBlocks(IHexianBlock block) {
			this.block = block;
		}
		
	}
	
	@SubscribeEvent
	public static void registerBlocksEvent(RegistryEvent.Register<Block> event) {
		RegistryHelper.registerBlocks(TheBlocks.values(), event.getRegistry());
	}
	
}
