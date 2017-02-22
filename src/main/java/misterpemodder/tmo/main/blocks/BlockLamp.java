package misterpemodder.tmo.main.blocks;

import java.util.Random;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.config.ConfigValues;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockLamp extends BlockBase implements IProbeInfoAccessor{
	
	//blockstates
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVATED, INVERTED);
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state){
        return true;
    }
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(ACTIVATED) == true? 0:2) + (state.getValue(INVERTED) == true? 0:1);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		boolean inverted = meta % 2 == 1? true:false;
		boolean activated = meta-(meta%2) == 2? true:false;
		return this.getDefaultState().withProperty(INVERTED, inverted).withProperty(ACTIVATED, activated);
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return 0;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState();
    }
    
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if(heldItem == null) return false;
        if(heldItem.getItem() == ItemBlock.getItemFromBlock(Blocks.REDSTONE_TORCH) && player.capabilities.allowEdit) {
        	if(!world.isRemote) {
        		float f = state.getValue(ACTIVATED) == true ? 0.55F : 0.5F;
        		world.setBlockState(pos, state.cycleProperty(INVERTED), 2);
        		world.playSound(player, pos, SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.BLOCKS, 0.3F, f);
        		world.scheduleUpdate(pos, this, 4);
        		world.notifyNeighborsOfStateChange(pos, this, false);
        	}
        	return true;
        }
        return false;
    }
    
    private boolean isValidState(IBlockState state, boolean blockPowered) {
    	boolean valid = false;
    	if(state.getValue(ACTIVATED)) {
    		valid = state.getValue(INVERTED) ^ blockPowered;
    	}
    	else {
    		valid = state.getValue(INVERTED) && blockPowered;
    	}
    	return valid;
    }
    
    private IBlockState getValidState(IBlockState state, boolean blockPowered) {
    	if(state.getValue(INVERTED)) {
    		return state.withProperty(ACTIVATED, !blockPowered);
    	} else {
    		return state.withProperty(ACTIVATED, blockPowered);
    	}
    }
    
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.isRemote) {
            if (!isValidState(state, world.isBlockPowered(pos))) {
                world.scheduleUpdate(pos, this, 4);
            }
        }
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!world.isRemote) {
        	boolean blockPowered = world.isBlockPowered(pos);
        	if(!isValidState(state, blockPowered)) {
        		world.setBlockState(pos, getValidState(state, blockPowered), 2);
        	}
        }
    }
    
	public BlockLamp(IBlockNames names, IBlockValues values) {
		super(names, values);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ACTIVATED, true).withProperty(INVERTED, true));
	}
	
	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		if(ConfigValues.BoolValues.TOP_COMPAT.currentValue) {
			IBlockState state = world.getBlockState(data.getPos());
			String txt = state.getValue(BlockLamp.INVERTED)? TextFormatting.RED+Tmo.proxy.translate("tile.blockLamp.mode.inverted") : TextFormatting.GREEN+Tmo.proxy.translate("tile.blockLamp.mode.normal");
			probeInfo.text(Tmo.proxy.translate("tile.blockLamp.mode.title")+" "+txt);
		}
	}
	
	
}
