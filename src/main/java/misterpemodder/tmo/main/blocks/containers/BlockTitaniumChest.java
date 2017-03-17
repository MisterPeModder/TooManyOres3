package misterpemodder.tmo.main.blocks.containers;

import java.util.Random;

import mcjty.theoneprobe.api.ElementAlignment;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.apiimpl.elements.ElementVertical;
import mcjty.theoneprobe.config.Config;
import misterpemodder.tmo.api.IStrongPistonBehavior;
import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTitaniumChest extends BlockContainerBase<TileEntityTitaniumChest> implements IProbeInfoAccessor{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final AxisAlignedBB CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	
	
	public BlockTitaniumChest() {
		super(EnumBlocksNames.TITANIUM_CHEST, EnumBlocksValues.TITANIUM_CHEST);
	}
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.NORMAL;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex() -2;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta+2));
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
	
	@Override
	public int damageDropped(IBlockState state) {
	    return 0;
	}
	
	@Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    	return CHEST_AABB;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
    	return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player,EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		} else if(super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ)){
			return true;
		}
		else if(this.getTileEntity(world, pos) != null) {
			TileEntityTitaniumChest te = this.getTileEntity(world, pos);
			te.sync();
			IOwnerHandler ownerHandler = te.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			
			if(ownerHandler != null && ownerHandler.hasOwner()) {
				if(!ownerHandler.isOwner(player) && te.isLocked()) {
					player.sendStatusMessage(new TextComponentString(TextFormatting.RED+Tmo.proxy.translate("tile.blockTitaniumChest.locked")), true);
					return true;
				}
			}
				
			te.onInvOpen(player);
			player.openGui(Tmo.instance, GuiHandler.EnumGuiElements.TITANIUM_CHEST.ID, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		if (te instanceof IWorldNameable && ((IWorldNameable)te).hasCustomName()) {
            player.addExhaustion(0.005F);
            
            if (worldIn.isRemote) {
                return;
            }
            
            Item item = this.getItemDropped(state, worldIn.rand, 0);
            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack = new ItemStack(item, 1);
            itemstack.setStackDisplayName(((IWorldNameable)te).getName());
            spawnAsEntity(worldIn, pos, itemstack);
        }
        else {
            super.harvestBlock(worldIn, player, pos, state, (TileEntity)null, stack);
        }
	}
	
	
	@Override
	public boolean canDropFromExplosion(Explosion explosionIn) {
		return false;
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		TileEntityTitaniumChest chest = (TileEntityTitaniumChest)world.getTileEntity(pos);
		if(chest != null && !world.isRemote) {
			ItemStack stack = chest.getLockItemHandler().getStackInSlot(0);
			if(!stack.isEmpty() && stack.getItem() instanceof IItemLock) {
				IItemLock lock = (IItemLock)stack.getItem();
				if(!lock.isBroken(stack)) {
					
					if(lock.attemptBreak(stack, new Random()) == EnumActionResult.SUCCESS) {
						world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
						chest.setLocked(false);
						lock.onLockBroken(world, pos, explosion.getExplosivePlacedBy());
					}
				}
			}
		}
	}
	
	@Override
	public Class<TileEntityTitaniumChest> getTileEntityClass() {
		return TileEntityTitaniumChest.class;
	}

	@Override
	public TileEntity createNewTileEntity(World world, IBlockState state) {
		return new TileEntityTitaniumChest();
	}

	@Override
	public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
		TileEntity te = world.getTileEntity(data.getPos());
		if(te != null && te instanceof TileEntityTitaniumChest && ConfigValues.BoolValues.TOP_COMPAT.currentValue) { 
			TileEntityTitaniumChest chest = (TileEntityTitaniumChest) te;
			IOwnerHandler ownerHandler = chest.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			
			String title = Tmo.proxy.translate("top.info.titaniumChest.owner.title");
			String empty = Tmo.proxy.translate("top.info.titaniumChest.owner.empty");
			
			String str = TextFormatting.GREEN + Tmo.proxy.translate("top.info.titaniumChest.access.granted");
			String str2 = TextFormatting.GREEN+title+" "+TextFormatting.RED+empty;
			
			if(ownerHandler!=null && ownerHandler.hasOwner()) {
				str2 = TextFormatting.GREEN+title+" "+TextFormatting.YELLOW+ownerHandler.getOwnerName();
				if(ownerHandler.getOwner(world) != player) {
					if(chest.isLocked()) {
						str = TextFormatting.RED+Tmo.proxy.translate("top.info.titaniumChest.access.denied");
						if(player.isSneaking())
						probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(Config.chestContentsBorderColor).spacing(0)).text(Tmo.proxy.translateFormatted(TextFormatting.GRAY+""+TextFormatting.ITALIC+"<%s>", "top.info.titaniumChest.hidden"));
					} else {
						str = TextFormatting.GOLD+Tmo.proxy.translate("top.info.titaniumChest.access.forced");
					}
				}
			}
			IProbeInfo main = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff040448).spacing(0));
			
			ItemStack lock = chest.getLockItemHandler().getStackInSlot(0);
			String lockedText = TextFormatting.YELLOW+""+TextFormatting.UNDERLINE+Tmo.proxy.translate("top.info.titaniumChest.locked")+":"+TextFormatting.RESET+" "+(chest.isLocked()? TextFormatting.GREEN+Tmo.proxy.translate("top.info.titaniumChest.locked.yes"):TextFormatting.RED+Tmo.proxy.translate("top.info.titaniumChest.locked.no"));
			ElementVertical textInfo = new ElementVertical(0, 2, ElementAlignment.ALIGN_TOPLEFT);
			
			main.text(str2);
			textInfo.text(lockedText).text(TextFormatting.YELLOW+""+TextFormatting.UNDERLINE+Tmo.proxy.translate("top.info.titaniumChest.access")+":"+TextFormatting.RESET+" "+str);
			
			if(lock.isEmpty()) {
				main.horizontal().element(textInfo);
			}
			else {
				main.horizontal().item(lock, main.defaultItemStyle().height(27)).element(textInfo);
			}
		}
		
	}
	
	public static class TChestPistonBehavior implements IStrongPistonBehavior {

		@Override
		public boolean canPushBlock(IBlockState state, World world, BlockPos pos, EnumFacing facing, boolean destroyBlocks) {
			if(state.getBlock() instanceof BlockTitaniumChest) {
				TileEntity te = world.getTileEntity(pos);
				if(te != null && te instanceof TileEntityTitaniumChest) {
					return !((TileEntityTitaniumChest)te).isLocked();
				}
			}
			return true;
		}
		
	}

}
