package misterpemodder.tmo.main.blocks;

import java.util.Random;

import misterpemodder.tmo.main.blocks.base.BlockBase;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.config.ConfigValues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockExploder extends BlockBase {
	
	public static final AxisAlignedBB EXPLODER_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.9375D, 1);
	
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	public BlockExploder() {
		super(EnumBlocksNames.EXPLODER, EnumBlocksValues.MetalBlocks.EXPLODER);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ACTIVATED);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ACTIVATED) == false? 0:1;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ACTIVATED, meta == 1? true:false);
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return EXPLODER_AABB;
    }
	
	public void explode(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if(state.getValue(ACTIVATED) == false) {
			BlockPos p = new BlockPos(pos);
			world.setBlockState(pos, world.getBlockState(pos).withProperty(ACTIVATED, true));
			if(world.isRemote) {
				world.playSound((double)p.getX(), (double)p.getY(), (double)p.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.5F, false);
			} else {
				world.playSound((EntityPlayer)null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.5F);
			}
			world.scheduleUpdate(p, this, 4);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		this.explode(world, pos);
		super.onBlockExploded(world, pos, explosion);
	}
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(state.getValue(ACTIVATED) == true) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.createExplosion(null, pos.getX(), pos.getY()+1, pos.getZ(), ConfigValues.FloatValues.EXPLODER_STRENGTH.currentValue, true);
		}
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		this.explode(world, pos);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(pos.up().equals(fromPos) || world.isBlockPowered(pos)) {
			this.explode(world, pos);
		} else {
			for(EnumFacing side : EnumFacing.HORIZONTALS) {
				IBlockState s = world.getBlockState(pos.offset(side));
		        Block b = s.getBlock();
		        if((b == Blocks.REDSTONE_BLOCK ? 15 : (b == Blocks.REDSTONE_WIRE ? ((Integer)s.getValue(BlockRedstoneWire.POWER)).intValue() : world.getStrongPower(pos, side)))>0) {
		        	this.explode(world, pos);
		        	return;
		        }
			}
		}
		
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		if(heldItem == null) return false;
        if(heldItem.getItem() instanceof ItemFlintAndSteel) {
        	heldItem.damageItem(1, player);
        	this.explode(world, pos);
        	return true;
        }
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(player != null && !player.capabilities.isCreativeMode && player.inventory != null) {
			if(EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, player.inventory.getCurrentItem()) == 0) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), ConfigValues.FloatValues.EXPLODER_STRENGTH.currentValue, true);
			}
		}
	}
	
	public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }

}
