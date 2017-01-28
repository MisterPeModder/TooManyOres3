package misterpemodder.tmo.main.blocks.base;

import java.util.List;

import misterpemodder.tmo.main.blocks.BlockBrick.EnumVariant;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.blocks.properties.IBlockVariant;
import misterpemodder.tmo.utils.TMOHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;

public abstract class BlockMulti<V extends Enum<V> & IBlockVariant> extends BlockBase {
	
	protected V[] variants;
	private String suffix;
	

	public BlockMulti(IBlockNames names, IBlockValues values, String suffix) {
		super(names, values);
		this.variants = getVariants();
		this.suffix = suffix;
		
		this.itemBlock = new ItemBlockMulti(this);
		itemBlock.setRegistryName(this.getRegistryName());
	}
	
	protected abstract V[] getVariants();
	
	
	
	public String getSuffix() {
		return suffix;
	}

	public IBlockVariant getVariant(int meta) {
		for(IBlockVariant variant : this.variants) {
			if(meta == variant.getMeta()) {
				return variant;
			}
		}
		return variants[0];
	}
	
	public IBlockVariant getVariant(IBlockState state) {
		return this.getVariant(this.getMetaFromState(state));
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for(IBlockVariant type : variants) {
			 list.add(new ItemStack(itemIn, 1, type.getMeta()));
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
			EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
	}
	
	protected boolean isValidVariant(int meta) {
		boolean valid = false;
		for(IBlockVariant type : variants) {
			if(type.getMeta() == meta) {
				valid = true;
				break;
			}
		}
		return valid;
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return getMetaFromState(state);
	}
	
	@Override
	public void registerItemRender() {
		int vNum = variants.length;
		for(int i=0; i<vNum; i++) {
			String name = variants[i].getName();
			ModelResourceLocation location = new ModelResourceLocation(TMOHelper.PREFIX + name + suffix , name);
			ModelLoader.setCustomModelResourceLocation(this.getItemBlock(),  variants[i].getMeta(), location);
		}
	}
	
	protected static int getMaxMeta(IBlockVariant[] blockVariants) {
		int m = 0;
		for(IBlockVariant variant : blockVariants) {
			m = m < variant.getMeta()? variant.getMeta() : m;
		}
		return m;
	}

}
