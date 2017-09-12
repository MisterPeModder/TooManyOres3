package misterpemodder.tmo.main.blocks.base;

import misterpemodder.hc.main.blocks.BlockMulti;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.utils.TMORefs;

public abstract class BlockMultiTMO<V extends Enum<V> & IBlockVariant> extends BlockMulti<V>{

	public BlockMultiTMO(IBlockNames names, IBlockValues values, String suffix) {
		super(names, values, suffix, TMORefs.TMO_TAB);
	}

}
