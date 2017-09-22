package misterpemodder.tmo.main.blocks.containers;

import java.util.ArrayList;
import java.util.List;

import misterpemodder.hc.main.utils.GuiHelper.IGuiElement;
import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.client.gui.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockDestabilizer extends BlockMachine<TileEntityDestabilizer> {
	
	public static final PropertyBool EMPTY = PropertyBool.create("empty");

	public BlockDestabilizer() {
		super(EnumBlocksNames.CRYSTAL_DESTABILIZER);
	}
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(EMPTY);
		return list;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		 
		if(tileentity != null && tileentity instanceof TileEntityDestabilizer) {
			return super.getActualState(state, world, pos).withProperty(EMPTY, ((TileEntityDestabilizer)tileentity).getEnderMatterAmount() <= 0);
		}
		else {
			return super.getActualState(state, world, pos);
		}
	}


	@Override
	public Class<TileEntityDestabilizer> getTileEntityClass() {
		return TileEntityDestabilizer.class;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileEntityDestabilizer();
	}
	
	@Override
	protected IGuiElement getGuiElements() {
		return EnumGuiElements.CRYSTAL_DESTABILIZER;
	}
	
}
