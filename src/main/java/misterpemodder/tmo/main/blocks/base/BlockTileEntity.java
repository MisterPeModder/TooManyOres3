package misterpemodder.tmo.main.blocks.base;

import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockTileEntity<TE extends TileEntity> extends BlockBase implements IBlockTileEntity<TE>{

	public BlockTileEntity(IBlockNames n, IBlockValues v) {
		super(n, v);
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}
	
	public TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return this.createNewTileEntity(world, this.getStateFromMeta(meta));
	}
	
	public abstract TileEntity createNewTileEntity(World world, IBlockState state);
	
}
