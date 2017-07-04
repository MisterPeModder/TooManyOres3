package misterpemodder.tmo.main.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import misterpemodder.tmo.main.blocks.BlockExploder.EnumExploderVariant;
import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.main.config.ConfigValues;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockExploder extends BlockMulti<EnumExploderVariant> {
	
	public static final AxisAlignedBB EXPLODER_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.9375D, 1);
	public static final PropertyEnum<EnumExploderVariant> VARIANT = PropertyEnum.create("variant", EnumExploderVariant.class);
	public static EnumExploderVariant[] exploderVariants = EnumExploderVariant.values();
	
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");
	
	public enum EnumExploderVariant implements IBlockVariant {
		
		NORMAL(0, "normal", "normal", EnumRarity.COMMON),
		FIERY(1, "fiery", "fiery", EnumRarity.UNCOMMON),
		SUPERCHARGED(2, "supercharged", "supercharged", EnumRarity.RARE),
		;
		
		private final int meta;
		private final String name;
		private final String unlocalizedName;
		private final EnumRarity rarity;

		@Override
		public String getName() {
			return this.name;
		}
		@Override
		public String toString() {
			return this.name;
		}
		@Override
		public int getMeta() {
			return this.meta;
		}
		@Override
		public String getUnlocalizedName() {
			return unlocalizedName;
		}
		@Override
		public MapColor getMapColor() {
			return EnumBlocksValues.MetalBlocks.EXPLODER.getMapColor(null);
		}
		@Override
		public EnumRarity getRarity() {
			return this.rarity;
		}

		EnumExploderVariant(int id, String name, String unlocalizedName, EnumRarity rarity) {
			this.meta = id;
			this.name = name;
			this.unlocalizedName = unlocalizedName;
			this.rarity = rarity;
		}
		
	}

	public BlockExploder() {
		super(EnumBlocksNames.EXPLODER, EnumBlocksValues.MetalBlocks.EXPLODER, "_exploder");
	}
	
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public IProperty<EnumExploderVariant> getPropertyVariant() {
		return VARIANT;
	}
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(ACTIVATED);
		return list;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(VARIANT).getMeta();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ACTIVATED, meta % 2 == 1? true:false).withProperty(VARIANT, getVariant(meta));
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
				world.playSound(p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.5F, false);
			} else {
				world.playSound((EntityPlayer)null, p.getX(), p.getY(), p.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0F, 1.5F);
			}
			world.scheduleUpdate(p, this, 4);
		}
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		this.explode(world, pos);
		this.updateTick(world, pos, world.getBlockState(pos), new Random());
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(VARIANT, getVariant(meta)).withProperty(ACTIVATED, false);
    }
	
	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return true;
	}
	
	@Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if(state.getValue(ACTIVATED) == true) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
			world.newExplosion(null, pos.getX(), pos.getY()+1, pos.getZ(), ((state.getValue(VARIANT)==EnumExploderVariant.SUPERCHARGED? 3 : 1)*ConfigValues.FloatValues.EXPLODER_STRENGTH.currentValue), state.getValue(VARIANT)==EnumExploderVariant.FIERY, true);
		}
    }
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
		this.explode(world, pos);
	}
	
	private boolean isPowered(World world, BlockPos pos) {
		if(world.isBlockPowered(pos)) {
			return true;
		} else {
			for(EnumFacing side : EnumFacing.HORIZONTALS) {
				IBlockState s = world.getBlockState(pos.offset(side));
		        Block b = s.getBlock();
		        if((b == Blocks.REDSTONE_BLOCK ? 15 : (b == Blocks.REDSTONE_WIRE ? s.getValue(BlockRedstoneWire.POWER).intValue() : world.getStrongPower(pos, side)))>0) {
		        	return true;
		        }
			}
		}
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(pos.up().equals(fromPos)) {
			this.explode(world, pos);
		} else if (isPowered(world, pos)){
			this.explode(world, pos);
		}
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (isPowered(world, pos)){
			this.explode(world, pos);
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
	
	@Override
	public boolean canDropFromExplosion(Explosion explosionIn) {
        return false;
    }
	
	@Override
	public EnumExploderVariant[] getVariants() {
		return exploderVariants;
	}
	
	@SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(state.getValue(VARIANT)==EnumExploderVariant.FIERY && rand.nextInt(5) == 0) {
            double d0 = pos.getX() + 0.55D - rand.nextFloat() * 0.1F;
            double d1 = pos.getY() + 1.05D - rand.nextFloat() * 0.1F;
            double d2 = pos.getZ() + 0.55D - rand.nextFloat() * 0.1F;
            double d3 = 0.4F - (rand.nextFloat() + rand.nextFloat()) * 0.4F;
            world.spawnParticle(EnumParticleTypes.LAVA, d0 + d3, d1 + d3, d2 + d3, rand.nextGaussian() * 0.005D, rand.nextGaussian() * 0.0005D, rand.nextGaussian() * 0.005D, new int[0]);
		}
    }
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return stack.getMetadata() == EnumExploderVariant.SUPERCHARGED.getMeta();
	}

}
