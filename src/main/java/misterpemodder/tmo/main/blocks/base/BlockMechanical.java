package misterpemodder.tmo.main.blocks.base;

import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import net.minecraft.util.BlockRenderLayer;

public class BlockMechanical extends BlockBaseTMO {

	public BlockMechanical(IBlockNames n, IBlockValues v) {
		super(n, v);
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

}
