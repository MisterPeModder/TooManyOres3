package misterpemodder.tmo.main.blocks.base;

import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import net.minecraft.util.BlockRenderLayer;

public class BlockMechanical extends BlockBase {

	public BlockMechanical(IBlockNames n, IBlockValues v) {
		super(n, v);
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
