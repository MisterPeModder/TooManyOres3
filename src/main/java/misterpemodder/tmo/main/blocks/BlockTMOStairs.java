package misterpemodder.tmo.main.blocks;

import misterpemodder.hc.main.blocks.BlockMulti;
import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;

public class BlockTMOStairs extends BlockStairs implements IHexianBlock {
	
	protected ItemBlock itemBlock;
	protected BlockMulti<?> baseBlock;
	protected IBlockVariant variant;

	public BlockTMOStairs(BlockMulti<?> baseBlock, IBlockVariant variant) {
		super(baseBlock.getStateFromVariant(variant));
		this.variant = variant;
		this.baseBlock = baseBlock;
		this.setUnlocalizedName(baseBlock.getUnlocalizedName().substring(5)+".stairs."+variant.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + variant.getName()+"_"+baseBlock.getRegistryName().getResourcePath()+"_stairs");
		this.setLightOpacity(0);
		
		this.itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(this.getRegistryName());
		this.setCreativeTab(TMORefs.TMO_TAB);
	}

	@Override
	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}
	
	@Override
	public MapColor getMapColor(IBlockState state) {
		return variant.getMapColor();
	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		return super.getHarvestLevel(state);
	}
	
	@Override
	public String getHarvestTool(IBlockState state) {
		return super.getHarvestTool(state);
	}
	
}
