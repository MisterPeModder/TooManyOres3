package misterpemodder.tmo.main.blocks.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.BlockMachineCasing.EnumMachineCasingVariant;
import misterpemodder.tmo.main.blocks.itemblock.ItemBlockMachine;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.PropertyIOPortState;
import misterpemodder.tmo.main.client.gui.GuiHandler.EnumGuiElements;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;

public abstract class BlockMachine<TE extends TileEntityMachine> extends BlockContainerBase<TE> {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", Arrays.asList(EnumFacing.HORIZONTALS));
	public static final PropertyEnum<EnumMachineCasingVariant> CASING = PropertyEnum.create("casing", EnumMachineCasingVariant.class);
	
	public ImmutableList<PropertyIOPortState> ioPortProperties;
	
	public final boolean canChangeCasings;
	
	public BlockMachine(IBlockNames n) {
		this(n, true);
	}
	
	public BlockMachine(IBlockNames n, boolean canChangeCasings) {
		super(n, EnumBlocksValues.MetalBlocks.MACHINE);
		
		this.canChangeCasings = canChangeCasings;
		if(canChangeCasings) {
			this.itemBlock = new ItemBlockMachine(this);
			itemBlock.setRegistryName(this.getRegistryName());
		}
	}
	
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	protected List<PropertyIOPortState> getIOPortProperties() {
		return PropertyIOPortState.getAllWithout(PropertyIOPortState.FRONT);
	}
	
	@Override
	protected List<IProperty<?>> getProperties() {
		ArrayList<IProperty<?>> list = new ArrayList<>();
		list.addAll(super.getProperties());
		list.add(FACING);
		list.add(CASING);
		this.ioPortProperties = ImmutableList.copyOf(getIOPortProperties());
		list.addAll(ioPortProperties);
		return list;
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity tileentity = world instanceof ChunkCache ? ((ChunkCache)world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		 
		if(!ioPortProperties.isEmpty() && tileentity != null && tileentity instanceof TileEntityMachine) {
			for(PropertyIOPortState property : ioPortProperties) {
				state = state.withProperty(property, property.getValue((TileEntityMachine) tileentity));
			}
			return state;
		}
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		if(canChangeCasings) {
			return state.getValue(FACING).getHorizontalIndex()*4 + state.getValue(CASING).getMeta();			
		}
		return state.getValue(FACING).getHorizontalIndex();	
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		if(canChangeCasings) {
			int cMeta = meta % 4;
			return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal((meta - cMeta)/4)).withProperty(CASING, EnumMachineCasingVariant.fromMeta(cMeta));
		}
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		if(canChangeCasings) {
			return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(CASING, EnumMachineCasingVariant.fromMeta(meta));
		} else {
			return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
		}
    }
	
	@Override
	public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
		return false;
	}
	
	protected abstract EnumGuiElements getGuiElements();
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		} else if(super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)){
			return true;
		}
		else if(this.getTileEntity(world, pos) != null) {
			TE te = getTileEntity(world, pos);
			te.sync();
			te.onInvOpen(player);
			player.openGui(Tmo.instance, getGuiElements().ID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		if(canChangeCasings) {
			for(EnumMachineCasingVariant v : EnumMachineCasingVariant.values()) {
				list.add(new ItemStack(itemIn, 1, v.getMeta()));
			}
		} else {
			super.getSubBlocks(itemIn, tab, list);
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, state.getValue(CASING).getMeta());
	}
	
	@Override
	public int damageDropped(IBlockState state) {
	    return state.getValue(CASING).getMeta();
	}
	
	@Override
	public void registerItemRender() {
		if(canChangeCasings) {
			for(EnumMachineCasingVariant v : EnumMachineCasingVariant.values()) {
				ModelResourceLocation location = new ModelResourceLocation(TMORefs.PREFIX +v.getName()+'_'+this.names.getRegistryName(), v.getName());
				ModelLoader.setCustomModelResourceLocation(this.getItemBlock(),  v.getMeta(), location);
			}
		} else {
			super.registerItemRender();
		}
	}

}
