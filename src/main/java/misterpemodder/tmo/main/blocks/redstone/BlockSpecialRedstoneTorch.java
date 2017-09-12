package misterpemodder.tmo.main.blocks.redstone;

import java.util.Random;

import misterpemodder.hc.main.blocks.IHexianBlock;
import misterpemodder.hc.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.init.ModBlocks.TheBlocks;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpecialRedstoneTorch extends BlockRedstoneTorch implements IHexianBlock {
	
	private final ItemBlock itemBlock;
	private final EnumBlocksNames names;
	
	protected boolean isOn;

	public BlockSpecialRedstoneTorch(boolean isOn, EnumBlocksNames names) {
		super(isOn);
		this.names = names;
		this.isOn = isOn;
		
		this.setUnlocalizedName(names.getUnlocalizedName());
		this.setRegistryName(TMORefs.PREFIX + names.getRegistryName());
		
		this.setHardness(0.0F);
		this.setSoundType(SoundType.WOOD);
		
		this.itemBlock = new ItemBlock(this);
		itemBlock.setRegistryName(this.getRegistryName());
		
		if(isOn) {
			this.setLightLevel(0.5F);
			this.disableStats();
			this.setCreativeTab(TMORefs.TMO_TAB);
		}
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
        return Item.getItemFromBlock(TheBlocks.WEAK_REDSTONE_TORCH.getBlock());
    }
	
	public boolean isAssociatedBlock(Block other) {
        return other == TheBlocks.WEAK_REDSTONE_TORCH.getBlock() || other == TheBlocks.WEAK_REDSTONE_TORCH_UNLIT.getBlock();
    }
	
	protected boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing enumfacing = state.getValue(FACING).getOpposite();
        return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
    }
	
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        boolean flag = this.shouldBeOff(worldIn, pos, state);
        
        if (this.isOn) {
            if (flag) {
                worldIn.setBlockState(pos, TheBlocks.WEAK_REDSTONE_TORCH_UNLIT.getBlock().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);                
            }
        }
        else if (!flag) {
            worldIn.setBlockState(pos, TheBlocks.WEAK_REDSTONE_TORCH.getBlock().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
        }
    }
	
	public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
        return 0;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(TheBlocks.WEAK_REDSTONE_TORCH.getBlock()));
	}

}
