package misterpemodder.tmo.main.blocks.containers;

import java.util.List;

import misterpemodder.tmo.main.blocks.base.BlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.PropertyIOPortState;
import misterpemodder.tmo.main.client.gui.GuiHandler.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockInjector extends BlockMachine<TileEntityInjector> {
		
	public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1.0D, 0.3125D, 1.0D);
	public static final AxisAlignedBB TANK_NORTH_AABB = new AxisAlignedBB(0.125D, 0.3125D, 0.5D, 0.875D, 1.0D, 0.9375D);
	public static final AxisAlignedBB TANK_SOUTH_AABB = new AxisAlignedBB(0.125D, 0.3125D, 0.0625D, 0.875D, 1.0D, 0.5D);
	public static final AxisAlignedBB TANK_EAST_AABB = new AxisAlignedBB(0.0625D, 0.3125D, 0.125D, 0.5D, 1.0D, 0.875D);
	public static final AxisAlignedBB TANK_WEST_AABB = new AxisAlignedBB(0.5D, 0.3125D, 0.125D, 0.9375D, 1.0D, 0.875D);
	public static final AxisAlignedBB PIPE_X = new AxisAlignedBB(0.375D, 0.3125D, 0, 0.625D, 0.625D, 1.0D);
	public static final AxisAlignedBB PIPE_Z = new AxisAlignedBB(0, 0.3125D, 0.375D, 1.0D, 0.625D, 0.625D);

	public BlockInjector() {
		super(EnumBlocksNames.INJECTOR);
	}
	
	@Override
	protected List<PropertyIOPortState> getIOPortProperties() {
		return PropertyIOPortState.getAllWithout(PropertyIOPortState.FRONT, PropertyIOPortState.UP);
	}

	@Override
	public Class<TileEntityInjector> getTileEntityClass() {
		return TileEntityInjector.class;
	}

	@Override
	public TileEntityInjector createNewTileEntity(World world, IBlockState state) {
		return new TileEntityInjector();
	}
	
	protected AxisAlignedBB getTankAABB(IBlockState state) {
		switch(state.getValue(FACING)) {
			case NORTH:
				return TANK_NORTH_AABB;
			case SOUTH:
				return TANK_SOUTH_AABB;
			case EAST:
				return TANK_EAST_AABB;
			default:
				return TANK_WEST_AABB;
		}
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_) {
		addCollisionBoxToList(pos, entityBox, collidingBoxes, getTankAABB(state));
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getValue(FACING).getAxis() == Axis.X? PIPE_X: PIPE_Z);
	}
	
	@Override
	protected EnumGuiElements getGuiElements() {
		return EnumGuiElements.INJECTOR;
	}

}
