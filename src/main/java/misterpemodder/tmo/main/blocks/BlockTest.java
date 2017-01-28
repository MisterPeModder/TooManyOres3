package misterpemodder.tmo.main.blocks;

import javax.annotation.Nullable;

import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class BlockTest extends BlockBase {
	
	//blockstates
	private static final PropertyBool ACTIVATED = PropertyBool.create("activated");
	private static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVATED, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		
		int meta = 0;
		int index = state.getValue(FACING).getIndex() -2;
		meta = index*2 + ((boolean) state.getValue(ACTIVATED)?1:0);
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int activated = meta % 2;
		int facing = (meta-activated)/2;
		
		return this.getDefaultState().withProperty(ACTIVATED, activated==1?true:false).withProperty(FACING, EnumFacing.getFront(facing+2));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return 0;
	}
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this.getWeakPower(blockState, blockAccess, pos, side);
    }
	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		boolean t1 = blockState.getValue(FACING) == side.getOpposite();
		boolean t2 = blockState.getValue(ACTIVATED) == true;
    	return t1 && t2? 15 : 0; 
    }
    
	@Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
        return true;
    }
    
    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }
    
    @Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if (!player.capabilities.allowEdit)
        {
            return false;
        }
        else if(player.isSneaking()) {
        	if(facing != state.getValue(FACING)){
        		if(facing == EnumFacing.UP || facing == EnumFacing.DOWN) {
            		world.setBlockState(pos, state.withRotation(Rotation.CLOCKWISE_90), 2);
            		world.notifyNeighborsOfStateChange(pos.offset(state.getValue(FACING),2), this, false);
            	} else {
            		world.setBlockState(pos, state.withProperty(FACING, facing), 2);
            	}
        		updateBlock(world, pos);
        	}
        	return true;
        }
        else
        {	
            state = state.cycleProperty(ACTIVATED);
            float f = state.getValue(ACTIVATED) == true ? 0.55F : 0.5F;
            world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
            world.setBlockState(pos, state, 2);
            updateBlock(world, pos);
           // worldIn.notifyNeighborsOfStateChange(pos, this);
            //worldIn.notifyBlockOfStateChange(pos.offset(state.getValue(FACING),2), this);
            return true;
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
    	if(world.getStrongPower(pos, state.getValue(FACING)) > 0 && state.getValue(ACTIVATED)) {
    		world.setBlockState(pos, state.withProperty(ACTIVATED, false),2);
    		updateBlock(world, pos);
    	}
    	else if(world.getStrongPower(pos, state.getValue(FACING)) == 0 && !state.getValue(ACTIVATED)) {
    		world.setBlockState(pos, state.withProperty(ACTIVATED, true),2);
    		updateBlock(world, pos);
    	}
    }
    
    private void updateBlock(World world, BlockPos pos) {
    	world.notifyNeighborsOfStateChange(pos, this, false);
    	world.notifyNeighborsOfStateChange(pos.offset(world.getBlockState(pos).getValue(FACING),2), this, false);
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

	public BlockTest() {
		super(EnumBlocksNames.TEST_BLOCK, EnumBlocksValues.TEST_BLOCK);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVATED, false));
	}
	
	
}
