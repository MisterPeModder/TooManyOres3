package misterpemodder.tmo.main.blocks.containers;

import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.client.gui.GuiHandler.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDestabilizer extends BlockMachine<TileEntityDestabilizer> {
	
	public static final PropertyBool EMPTY = PropertyBool.create("empty");

	public BlockDestabilizer() {
		super(EnumBlocksNames.CRYSTAL_DESTABILIZER);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, EMPTY);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntityDestabilizer te = this.getTileEntity(world, pos);
		
		if(te == null) {
			return super.getActualState(state, world, pos);
		}
		else {
			return state.withProperty(EMPTY, te.getEnderMatterAmount() <= 0);
		}
	}


	@Override
	public Class<TileEntityDestabilizer> getTileEntityClass() {
		return TileEntityDestabilizer.class;
	}

	@Override
	public TileEntityDestabilizer createNewTileEntity(World world, IBlockState state) {
		return new TileEntityDestabilizer();
	}
	
	@Override
	protected EnumGuiElements getGuiElements() {
		return EnumGuiElements.CRYSTAL_DESTABILIZER;
	}
	
}
