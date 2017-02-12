package misterpemodder.tmo.main.tileentity;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.block.IWorldNameableModifiable;
import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.capability.OwnerHandlerUUID;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTitaniumChest extends TileEntityContainerBase implements ILockable, ITickable, IWorldNameableModifiable  {
	
	private ItemStackHandler inventory = new ItemStackHandler(66);
	private ItemStackHandler lock = new ItemStackHandler(1);
	private OwnerHandlerUUID ownerHandler = new OwnerHandlerUUID();
	
	private String customName;
	private String owner;
	
    public static final int MAX_UPDATE_TIME = 200;
    public int numPlayersUsing;
    
    public int ticksSinceUpdate;
    public float prevLidAngle;
    public float lidAngle;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setTag("lock", lock.serializeNBT());
		compound.setTag("owner", ownerHandler.serializeNBT());
		if (this.hasCustomName()) {
			compound.setString("customName", customName);
        }
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.lock.deserializeNBT(compound.getCompoundTag("lock"));
		this.ownerHandler.deserializeNBT(compound.getCompoundTag("owner"));
		
		if(compound.hasKey("customName")) {
			this.customName = compound.getString("customName");
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		this.sync();
	}
	
	@Override
	public ItemStackHandler getInventory() {
		return this.inventory;
	}
	
	@Override
	public ItemStackHandler getLockItemHandler() {
		return this.lock;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityOwner.OWNER_HANDLER_CAPABILITY) {
			return true;
		}
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if(this.hasCapability(capability, facing)) {
			return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY? (T)inventory : (T)ownerHandler;
		}
		return super.getCapability(capability, facing);
	}
	
	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentString(TextFormatting.getTextWithoutFormattingCodes(Tmo.proxy.translate("tile.blockTitaniumChest.name")));
	}
	
	@Override
	public boolean canRenderBreaking() {
	     return true;
	}
	
	public void syncPlayerUsingNum(int num) {
		if(!this.world.isRemote) {
			this.numPlayersUsing += num;
			NetworkRegistry.TargetPoint target = new TargetPoint(this.world.provider.getDimension(), this.pos.getX(), this.pos.getY(), this.pos.getZ(), 64);
			NBTTagCompound toSend = new NBTTagCompound();
			toSend.setLong("pos", this.pos.toLong());
			toSend.setInteger("numPlayersUsing", this.numPlayersUsing);
			TMOPacketHandler.network.sendToAllAround(new PacketServerToClient(PacketDataHandlers.TCHEST_UPDATE_HANDLER, toSend), target);
		}
	}
	
	@Override
	public void update() {
        int i = this.pos.getX();
        int j = this.pos.getY();
        int k = this.pos.getZ();
        
        Block b = this.world.getBlockState(pos).getBlock();
        
		if (this.world.isRemote) {
			
			if(ticksSinceUpdate >= MAX_UPDATE_TIME) {
				ticksSinceUpdate = 0;
				this.sync();
			} else {
				ticksSinceUpdate++;
			}
			
			//Chest lid animation
			this.prevLidAngle = this.lidAngle;
			float f1 = 0.1F;

			if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
				double d1 = (double) i + 0.5D;
				double d2 = (double) k + 0.5D;

				Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F,
						this.world.rand.nextFloat() * 0.1F + 0.9F);
			}

			if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
				float f2 = this.lidAngle;

				if (this.numPlayersUsing > 0) {
					this.lidAngle += 0.1F;
				} else {
					this.lidAngle -= 0.1F;
				}

				if (this.lidAngle > 1.0F) {
					this.lidAngle = 1.0F;
				}

				float f3 = 0.5F;

				if (this.lidAngle < 0.5F && f2 >= 0.5F) {
					double d3 = (double) i + 0.5D;
					double d0 = (double) k + 0.5D;

					Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5F,
							this.world.rand.nextFloat() * 0.1F + 0.9F);
				}

				if (this.lidAngle < 0.0F) {
					this.lidAngle = 0.0F;
				}
			}
		}
    }

	@Override
    public void onInvOpen(EntityPlayer player) {
        if (!player.isSpectator()) {
            if (this.numPlayersUsing < 0) {
                this.numPlayersUsing = 0;
            }
            if(!player.getEntityWorld().isRemote)
            	this.syncPlayerUsingNum(1);
        }
    }
	
	@Override
    public void onInvClose(EntityPlayer player) {
        if (!player.isSpectator() && this.getBlockType() instanceof BlockTitaniumChest) {
        	if(!player.getEntityWorld().isRemote)
        		this.syncPlayerUsingNum(-1);
        }
    }
	
	@Override
	public boolean hasCustomName() {
        return this.customName != null && !this.customName.isEmpty();
    }
	
	@Override
    public void setCustomName(String name) {
        this.customName = name;
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : this.getDisplayName().getUnformattedText();
    }
    
    @Override
    public boolean isLocked() {
    	ItemStack lock = this.getLockItemHandler().getStackInSlot(0).copy();
    	return lock.isEmpty() || !(lock.getItem() instanceof IItemLock)? false : !((IItemLock)lock.getItem()).isBroken(lock);
    }
    
}
