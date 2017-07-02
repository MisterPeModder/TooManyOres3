package misterpemodder.tmo.main.blocks.slab;

import java.util.Random;

import misterpemodder.tmo.main.blocks.base.BlockMulti;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockAbstractSlab<V extends Enum<V> & IBlockVariant> extends BlockMulti<V> {
	
	protected static final AxisAlignedBB AABB_BOTTOM_HALF = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.5D, 1.0D);
	protected static final AxisAlignedBB AABB_TOP_HALF = new AxisAlignedBB(0.0D, 0.5D, 0.0D, 1.0D, 1.0D, 1.0D);
	
	public static final PropertyEnum<BlockAbstractSlab.EnumBlockHalf> HALF = PropertyEnum.create("half", BlockAbstractSlab.EnumBlockHalf.class);
	
	@Override
	protected BlockStateContainer createBlockState() {
		if(isDouble()) return new BlockStateContainer(this, getPropertyVariant());
		return new BlockStateContainer(this, getPropertyVariant(), HALF);
	}
	
	public static enum EnumBlockHalf implements IStringSerializable {
		TOP("top"),
		BOTTOM("bottom");
		
		private final String name;
		
		public String toString() {
			return this.name;
		}
		
		public String getName(){
			return this.name;
		}
		
		private EnumBlockHalf(String name) {
			this.name = name;
		}
		
	}
	
	protected BlockAbstractSlab(IBlockNames names, IBlockValues values, String suffix) {
		super(names, values, suffix);
		this.setDefaultState(this.blockState.getBaseState());
	}
	
	public abstract boolean isDouble();
	
	public abstract IProperty<V> getPropertyVariant();
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(isDouble()) return getDefaultState().withProperty(getPropertyVariant(), getVariant(meta));
		int isTop = meta%2;
		IBlockState s = getDefaultState().withProperty(HALF, isTop == 1? EnumBlockHalf.TOP:EnumBlockHalf.BOTTOM);
	    return s.withProperty(getPropertyVariant(), getVariant((meta-isTop)/2));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		if(isDouble()) return state.getValue(getPropertyVariant()).getMeta();
	    return 2*state.getValue(getPropertyVariant()).getMeta()+(state.getValue(HALF) == EnumBlockHalf.TOP?1:0);
	}
	
	@Override
	public MapColor getMapColor(IBlockState state) {
		return state.getValue(getPropertyVariant()).getMapColor();
	}
	
	@Override	
    protected boolean canSilkHarvest() {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return this.isDouble() ? FULL_BLOCK_AABB : (state.getValue(HALF) == BlockAbstractSlab.EnumBlockHalf.TOP ? AABB_TOP_HALF : AABB_BOTTOM_HALF);
    }

	@Override
	@SuppressWarnings("unchecked")
    public boolean isFullyOpaque(IBlockState state) {
        return ((BlockAbstractSlab<V>)state.getBlock()).isDouble() || state.getValue(HALF) == BlockAbstractSlab.EnumBlockHalf.TOP;
    }

    public boolean isOpaqueCube(IBlockState state) {
        return this.isDouble();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        if (net.minecraftforge.common.ForgeModContainer.disableStairSlabCulling)
            return super.doesSideBlockRendering(state, world, pos, face);

        if (state.isOpaqueCube()) return true;

        EnumBlockHalf side = state.getValue(HALF);
        return (side == EnumBlockHalf.TOP && face == EnumFacing.UP) || (side == EnumBlockHalf.BOTTOM && face == EnumFacing.DOWN);
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    	if(this.isDouble()) return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer);

        IBlockState iblockstate = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(HALF, BlockAbstractSlab.EnumBlockHalf.BOTTOM);
        return facing != EnumFacing.DOWN && (facing == EnumFacing.UP || hitY <= 0.5D) ? iblockstate : iblockstate.withProperty(HALF, BlockAbstractSlab.EnumBlockHalf.TOP);
    }

    @Override
    public int quantityDropped(Random random) {
        return this.isDouble() ? 2 : 1;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return this.isDouble();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        if (this.isDouble()) {
            return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
        else if (side != EnumFacing.UP && side != EnumFacing.DOWN && !super.shouldSideBeRendered(blockState, blockAccess, pos, side)) {
            return false;
        }
        return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.getItemBlock();
    }
    
    @Override
	public int damageDropped(IBlockState state) {
	    return state.getValue(getPropertyVariant()).getMeta();
	}
    
    @Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World worldIn, BlockPos pos,
			EntityPlayer player) {
		return this.getItem(worldIn, pos, state);
	}
    
    @Override
    public boolean getUseNeighborBrightness(IBlockState state) {
        return this.isDouble()? false:true;
    }
    
    @Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    	return new ItemStack(this.getItemBlock(), 1, state.getValue(getPropertyVariant()).getMeta());
	}
	
}
