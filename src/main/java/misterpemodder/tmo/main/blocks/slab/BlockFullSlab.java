package misterpemodder.tmo.main.blocks.slab;

import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.item.EnumRarity;

public class BlockFullSlab extends BlockAbstractSlab<BlockHalfSlab.EnumVariant> {
	
	private BlockHalfSlab halfSlab;
	
	@Override
	public PropertyEnum<BlockHalfSlab.EnumVariant> getPropertyVariant() {
		return BlockHalfSlab.VARIANT;
	}
	
	public BlockHalfSlab.EnumVariant[] getVariants() {
		return BlockHalfSlab.EnumVariant.values();
	}
	
	protected static IBlockNames toFullSlabName(BlockHalfSlab slab) {
		return new IBlockNames() {
			public String getUnlocalizedName() {
				return slab.getNames().getUnlocalizedName();
			}
			public String getRegistryName() {
				return slab.getNames().getRegistryName()+"_full";
			}
			public String[] getOreDictNames() {
				return slab.getNames().getOreDictNames();
			}
			public EnumRarity getRarity() {
				return slab.getNames().getRarity();
			}
		};
	}

	public BlockFullSlab(BlockHalfSlab halfSlab) {
		super(toFullSlabName(halfSlab), halfSlab.getValues(), halfSlab.getSuffix());
		
		this.halfSlab = halfSlab;
		halfSlab.setFullSlab(this);
		
		this.itemBlock = halfSlab.getItemBlock();
	}
	
	public BlockHalfSlab getHalfSlab() {
		return this.halfSlab;
	}

	@Override
	public boolean isDouble() {
		return true;
	}
	
	@Override
	public boolean hasOwnItemBlock() {
		return false;
	}
	
}
