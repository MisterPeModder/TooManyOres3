package misterpemodder.tmo.main.blocks.containers;

import java.util.Arrays;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockInjector extends BlockContainerBase<TileEntityInjector> {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
	
	public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1.0D, 0.3125D, 1.0D);
	public static final AxisAlignedBB TANK_NORTH_AABB = new AxisAlignedBB(0.125D, 0.3125D, 0.5D, 0.875D, 1.0D, 0.9375D);
	public static final AxisAlignedBB TANK_SOUTH_AABB = new AxisAlignedBB(0.125D, 0.3125D, 0.0625D, 0.875D, 1.0D, 0.5D);
	public static final AxisAlignedBB TANK_EAST_AABB = new AxisAlignedBB(0.0625D, 0.3125D, 0.125D, 0.5D, 1.0D, 0.875D);
	public static final AxisAlignedBB TANK_WEST_AABB = new AxisAlignedBB(0.5D, 0.3125D, 0.125D, 0.9375D, 1.0D, 0.875D);
	public static final AxisAlignedBB PIPE_X = new AxisAlignedBB(0.375D, 0.3125D, 0, 0.625D, 0.625D, 1.0D);
	public static final AxisAlignedBB PIPE_Z = new AxisAlignedBB(0, 0.3125D, 0.375D, 1.0D, 0.625D, 0.625D);

	public BlockInjector() {
		super(EnumBlocksNames.INJECTOR, EnumBlocksValues.MetalBlocks.MACHINE);
	}
	
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
		return true;
	}
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}

	@Override
	public Class<TileEntityInjector> getTileEntityClass() {
		return TileEntityInjector.class;
	}

	@Override
	public TileEntity createNewTileEntity(World world, IBlockState state) {
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		} else if(super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)){
			return true;
		}
		else if(this.getTileEntity(world, pos) != null) {
			TileEntityInjector tei = getTileEntity(world, pos);
			tei.sync();
			tei.onInvOpen(player);
			player.openGui(Tmo.instance, GuiHandler.EnumGuiElements.INJECTOR.ID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}

}
