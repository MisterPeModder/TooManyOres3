package misterpemodder.tmo.main.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.hc.main.blocks.properties.IBlockValues;
import misterpemodder.hc.main.utils.StringUtils;
import misterpemodder.tmo.main.blocks.base.BlockBaseTMO;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "mcjty.theoneprobe.api.IProbeInfoAccessor", modid = "theoneprobe")
public class BlockLamp extends BlockBaseTMO implements IProbeInfoAccessor {
	
	//blockstates
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");
	public static final PropertyBool INVERTED = PropertyBool.create("inverted");
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(ACTIVATED);
		list.add(INVERTED);
		return list;
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
		ItemStack heldStack = player.getHeldItem(hand);
		if(heldStack == null) return false;
		Item item = heldStack.getItem();
        if((item == ItemBlock.getItemFromBlock(Blocks.REDSTONE_TORCH) || item == ItemBlock.getItemFromBlock(TheBlocks.WEAK_REDSTONE_TORCH.getBlock())) && player.capabilities.allowEdit) {
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
	@Optional.Method(modid = "theoneprobe")
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		if(ConfigValues.BoolValues.TOP_COMPAT.currentValue) {
			IBlockState state = world.getBlockState(data.getPos());
			String txt = state.getValue(BlockLamp.INVERTED)? TextFormatting.RED+StringUtils.translate("tile.blockLamp.mode.inverted") : TextFormatting.GREEN+StringUtils.translate("tile.blockLamp.mode.normal");
			probeInfo.text(StringUtils.translate("tile.blockLamp.mode.title")+" "+txt);
		}
	}
	
	
}
