package misterpemodder.tmo.main.blocks.containers;

import java.util.Random;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.block.IWorldNameableModifiable;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.properties.IBlockNames;
import misterpemodder.tmo.main.blocks.properties.IBlockValues;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.items.tools.ItemWrench;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.ServerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.IItemHandler;

public abstract class BlockContainerBase<TE extends TileEntityContainerBase> extends BlockTileEntity<TE> {

	public BlockContainerBase(IBlockNames n, IBlockValues v) {
		super(n, v);
	}
	
	public ItemStack toItem(TE tileEntity, IBlockState state) {
		NBTTagCompound c = new NBTTagCompound();
		NBTTagCompound teNBT = tileEntity.serializeNBT();
		teNBT.removeTag("x");
		teNBT.removeTag("y");
		teNBT.removeTag("z");
		c.setTag("BlockEntityTag", teNBT);
		NBTTagList l = new NBTTagList();
		l.appendTag(new NBTTagString(Tmo.proxy.translate("item.hasNBT.desc")));
		NBTTagCompound d = new NBTTagCompound();
		d.setTag("Lore", l);
		c.setTag("display", d);
		
		ItemStack stack = new ItemStack(this.getItemDropped(state, new Random(), 0));
		stack.setTagCompound(c);
		
		return stack;
	}
	
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TE te = this.getTileEntity(world, pos);
		
		GameRules rules = world.getGameRules();
		
		if(rules.getBoolean("doTileDrops")) {
			if(!rules.getBoolean("dropInvContents")) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.toItem(te, state)));
			} else {
				if(te instanceof ILockable) {
					IItemHandler lockItemHandler = ((ILockable) te).getLockItemHandler();
					if(!lockItemHandler.getStackInSlot(0).isEmpty()) {
						world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), lockItemHandler.getStackInSlot(0)));
					}
				}
				IItemHandler itemHandler = te.getInventory();
				int s = itemHandler.getSlots();
				for(int i=0; i<s; i++) {
					if(!itemHandler.getStackInSlot(i).isEmpty()) {
						world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemHandler.getStackInSlot(i)));
					}
				}
			}
		}
		super.breakBlock(world, pos, state);
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tileentity = worldIn.getTileEntity(pos);
            if (stack.hasDisplayName() && tileentity instanceof IWorldNameableModifiable) {
            	((IWorldNameableModifiable)tileentity).setCustomName(stack.getDisplayName());
            }
            if(placer instanceof EntityPlayer) {
            	/*String owner;
            	if(stack.serializeNBT().hasKey("tag")) {
            		NBTTagCompound tag = stack.serializeNBT().getCompoundTag("tag");
            		if(tag.hasKey("BlockEntityTag")) {
            			NBTTagCompound blockEntityTag = tag.getCompoundTag("BlockEntityTag");
            			if(blockEntityTag.hasKey("owner")) {
            				owner = blockEntityTag.getString("owner");
            			}
            		}
            	}*/
            	IOwnerHandler ownerHandler = tileentity.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
            	if(ownerHandler != null)
            		ownerHandler.setOwner((EntityPlayer)placer);
            }
            if(!worldIn.isRemote) {
            	TargetPoint target = new TargetPoint(worldIn.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 64);
            	NBTTagCompound toSend = new NBTTagCompound();
            	
            	toSend.setLong("pos", pos.toLong());
            	toSend.setTag("tileEntity", tileentity.serializeNBT());
            	
            	TMOPacketHandler.network.sendToAllAround(new PacketServerToClient(PacketDataHandlers.TE_UPDATE_HANDLER, toSend), target); 
            }
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		TE tileEntity = this.getTileEntity(world, pos);
		
		if(tileEntity == null) {
			return false;
		}
		else if(world.isRemote) {
			return true;
		} 
		else if (stack.getItem() instanceof ItemWrench && player.isSneaking()) {

			if (((ItemWrench) stack.getItem()).canWrench(player, pos)) {
				((ItemWrench) stack.getItem()).wrenchUsed(player, pos);
				if (stack.getItem() instanceof ItemWrench) {
					if (((ItemWrench) stack.getItem()).isAdminWrench && ServerUtils.isOp(player)) {
						IOwnerHandler ownerHandler = tileEntity.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY,
								facing);

						if (ownerHandler != null && ownerHandler.hasOwner()) {
							ownerHandler.setOwner(null);
							player.sendMessage(new TextComponentString(Tmo.proxy.translate("item.wrench.OwnerRemoved")));
							return true;
						}
					} else {
						player.sendMessage(new TextComponentString(TextFormatting.RED + Tmo.proxy.translate("item.wrench.NoPermissions")));
						return false;
					}
				}
			}
			world.setBlockToAir(pos);
			world.spawnEntity(
					new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), this.toItem(tileEntity, state)));
			return true;
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
}
