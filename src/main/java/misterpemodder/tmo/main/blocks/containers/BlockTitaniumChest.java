package misterpemodder.tmo.main.blocks.containers;

import java.util.Random;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoAccessor;
import mcjty.theoneprobe.api.ProbeMode;
import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksNames;
import misterpemodder.tmo.main.blocks.properties.EnumBlocksValues;
import misterpemodder.tmo.main.client.gui.GuiHandler;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.IOwnable;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTitaniumChest extends BlockContainerBase<TileEntityTitaniumChest> implements IProbeInfoAccessor{
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	protected static final AxisAlignedBB CHEST_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D);
	
	public BlockTitaniumChest() {
		super(EnumBlocksNames.TITANIUM_CHEST, EnumBlocksValues.TITANIUM_CHEST);
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
			if(te.hasOwner()) {
				if(!(player.getDisplayNameString().equals(te.getOwner())) && te.isLocked()) {
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
		if (te instanceof IOwnable && ((IOwnable)te).hasCustomName()) {
            player.addExhaustion(0.005F);

            if (worldIn.isRemote) {
                return;
            }
            
            Item item = this.getItemDropped(state, worldIn.rand, 0);
            if (item == Items.AIR) {
                return;
            }

            ItemStack itemstack = new ItemStack(item, 1);
            itemstack.setStackDisplayName(((IOwnable)te).getName());
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
						
						NetworkRegistry.TargetPoint target = new TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
						NBTTagCompound toSend = new NBTTagCompound();
						toSend.setLong("pos", pos.toLong());
						toSend.setTag("lock", chest.getLockItemHandler().serializeNBT());
						TMOPacketHandler.network.sendToAllAround(new PacketServerToClient(PacketDataHandlers.TE_UPDATE_HANDLER, toSend), target);
						
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
		if(te instanceof TileEntityTitaniumChest) {
			TileEntityTitaniumChest chest = (TileEntityTitaniumChest) te;
			
			String title = Tmo.proxy.translate("top.info.titaniumChest.owner.title");
			String empty = Tmo.proxy.translate("top.info.titaniumChest.owner.empty");
			String unlocked = Tmo.proxy.translate("top.info.titaniumChest.lock.unlocked");
			String locked = Tmo.proxy.translate("top.info.titaniumChest.lock.locked");
			
			
			IProbeInfo vertical = null;
			vertical = probeInfo.vertical(probeInfo.defaultLayoutStyle().borderColor(0xff006699).spacing(0));
			vertical.text(TextFormatting.GREEN+title+" "+(chest.hasOwner()?TextFormatting.YELLOW+chest.getOwner():TextFormatting.RED+empty));
			ItemStack lock = chest.getLockItemHandler().getStackInSlot(0);
			String txt = lock.isEmpty() || ((IItemLock)lock.getItem()).isBroken(lock)? TextFormatting.GREEN+unlocked: TextFormatting.RED+locked;
			if(lock.isEmpty()) {
				vertical.horizontal().text(txt);
			}
			else {
				vertical.horizontal().item(lock).text(txt);
			}
		}
		
	}

}
