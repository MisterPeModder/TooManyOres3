package misterpemodder.tmo.main.blocks.containers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.client.gui.GuiHandler.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityThemoelectricGenerator;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class BlockThermoelectricGenerator extends BlockMachine<TileEntityThemoelectricGenerator> {
	
	public static final PropertyBool WORKING = PropertyBool.create("working");

	public BlockThermoelectricGenerator() {
		super(EnumBlocksNames.THERMOELECTRIC_GENERATOR, true);
	}
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(WORKING);
		return list;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		 
		if(tileentity != null && tileentity instanceof TileEntityThemoelectricGenerator) {
			return super.getActualState(state, world, pos).withProperty(WORKING, ((TileEntityThemoelectricGenerator)tileentity).isMachineWorking());
		}
		else {
			return super.getActualState(state, world, pos);
		}
	}

	@Override
	public Class<TileEntityThemoelectricGenerator> getTileEntityClass() {
		return TileEntityThemoelectricGenerator.class;
	}

	@Override
	protected EnumGuiElements getGuiElements() {
		return EnumGuiElements.THERMOELECTRIC_GENERATOR;
	}

	@Override
	public TileEntity createNewTileEntity(World world, IBlockState state) {
		return new TileEntityThemoelectricGenerator();
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(getActualState(state, world, pos).getValue(WORKING)) {
			EnumFacing facing = state.getValue(FACING).getOpposite();

			double dx = facing.getAxis() == Axis.X ? facing.getFrontOffsetX() > 0 ? 1.0D : 0.0D : 0.5D;
			double dz = facing.getAxis() == Axis.Z ? facing.getFrontOffsetZ() > 0 ? 1.0D : 0.0D : 0.5D;

			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX()+dx, pos.getY() + 0.8D, pos.getZ()+dz, dz == 0.5D? facing.getFrontOffsetX()*0.05D : 0.0D, 0.0D, dx == 0.5D? facing.getFrontOffsetZ()*0.05D : 0.0D, new int[0]);
		}
	}

}
