package misterpemodder.tmo.main.blocks.redstone;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import misterpemodder.tmo.main.blocks.base.IBlockTMO;
import misterpemodder.tmo.main.blocks.itemblock.ItemBlockSpecialRedstone;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSpecialRedstoneWire extends BlockRedstoneWire implements IBlockTMO {
	
	private final ItemBlockSpecialRedstone itemBlock;
	private final EnumBlocksNames names;
	
	public static boolean canWireProvidePower = true;
	
	private static Method isPowerSourceAt;
	
	static {
		try {
			isPowerSourceAt = ReflectionHelper.findMethod(BlockRedstoneWire.class, "isPowerSourceAt", "func_176339_d", IBlockAccess.class, BlockPos.class, EnumFacing.class);
			isPowerSourceAt.setAccessible(true);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public BlockSpecialRedstoneWire(EnumBlocksNames names) {
		super();
		this.names = names;
		
		this.setUnlocalizedName(names.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + names.getRegistryName());
		this.setLightOpacity(0);
		
		this.itemBlock = new ItemBlockSpecialRedstone(this);
		itemBlock.setRegistryName(this.getRegistryName());
		this.setCreativeTab(TMORefs.TMO_TAB);
	}
	
	@Override
	public ItemBlock getItemBlock() {
		return this.itemBlock;
	}
	
	public IBlockNames getNames() {
		return this.names;
	}
	
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this.getItemBlock());
    }
	
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return this.getItemBlock();
    }
	
	public boolean alwaysConnect() {
		return this == TheBlocks.COPPER_REDSTONE.getBlock();
    }
	
	public static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos) {
		if(blockState.getBlock() instanceof BlockRedstoneWire) {
			return true;
		}
		else if(side != null) {
			IBlockState state = world.getBlockState(pos.offset(side.getOpposite()));
			if(state.getBlock() instanceof BlockSpecialRedstoneWire) {
				return ((BlockSpecialRedstoneWire)state.getBlock()).canWireConnectTo(blockState, side, world, pos);
			}
		}
		return false;
	}
	
	protected boolean canWireConnectTo(IBlockState blockState, @Nonnull EnumFacing side, IBlockAccess world, BlockPos pos) {
		if(this == TheBlocks.COPPER_REDSTONE.getBlock()) {
			return blockState.isNormalCube() || (blockState.getBlock() instanceof BlockPistonBase && blockState.getValue(BlockPistonBase.FACING) != side.getOpposite());
		}
		return false;
	}
	
	@Override
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return this == TheBlocks.TITANIUM_REDSTONE.getBlock()? 0 : super.getStrongPower(blockState, blockAccess, pos, side);
    }
	
	protected boolean isPowerSourceAt(IBlockAccess world, BlockPos pos, EnumFacing side) {
		try {
			Object b = isPowerSourceAt.invoke(this, world, pos, side);
			if(b instanceof Boolean) {
				return ((Boolean) b).booleanValue();
			}
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {}
		return false;
	}
	
	@Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (!BlockSpecialRedstoneWire.canWireProvidePower) {
			return 0;
		} else {
			int i = blockState.getValue(POWER).intValue();

			if (i == 0) {
				return 0;
			} else if (side == EnumFacing.UP) {
				return i;
			} else {

				EnumSet<EnumFacing> enumset = EnumSet.<EnumFacing> noneOf(EnumFacing.class);
				for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
					if (isPowerSourceAt(blockAccess, pos, side)) {
						enumset.add(enumfacing);
					}
				}

				if (side.getAxis().isHorizontal() && enumset.isEmpty()) {
					return i;
				} else if (this == TheBlocks.COPPER_REDSTONE.getBlock() || (enumset.contains(side) && !enumset.contains(side.rotateYCCW()) && !enumset.contains(side.rotateY()))) {
					return i;
				} else {
					return 0;
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerColorHandler() {
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(new IBlockColor() {
            public int colorMultiplier(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex) {
                return BlockSpecialRedstoneWire.getWireColorMultiplier(state.getValue(BlockRedstoneWire.POWER).intValue(), (BlockSpecialRedstoneWire)state.getBlock());
            }
        }, TheBlocks.TITANIUM_REDSTONE.getBlock(), TheBlocks.COPPER_REDSTONE.getBlock());
	}
	
	@SideOnly(Side.CLIENT)
	public static int getWireColorMultiplier(int power, BlockSpecialRedstoneWire block) {
		
		float f = power / 15.0F;
		float r = 0.0F;
		float g = 0.0F;
		float b = 0.0F;
		
		if(block == TheBlocks.TITANIUM_REDSTONE.getBlock()) {
			 r = f * 0.2F + 0.15F;
			 b = f * 0.6F + 0.2F;
			 
		} 
		else if(block == TheBlocks.COPPER_REDSTONE.getBlock()) {
			 r = f * 0.55F + 0.45F;
			 g = f * 0.3F + 0.1F;
			 
		}
		
        int i = MathHelper.clamp((int)(r * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(g * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(b * 255.0F), 0, 255);
        
        return -16777216 | i << 16 | j << 8 | k;
	}

}
