package misterpemodder.tmo.main.tileentity;

import misterpemodder.tmo.main.Tmo;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.items.ItemLock;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketClientToServer;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTitaniumChest extends TileEntityContainerBase implements TileEntityLockable, ITickable, IOwnable  {
	
	private ItemStackHandler inventory = new ItemStackHandler(66);
	private ItemStackHandler lock = new ItemStackHandler(1);
	private String customName;
	private String owner;
	
    public float lidAngle;
    public float prevLidAngle;
    public int numPlayersUsing;
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound.setTag("inventory", inventory.serializeNBT());
		compound.setTag("lock", lock.serializeNBT());
		if (this.hasCustomName()) {
			compound.setString("customName", customName);
        }
		if(this.hasOwner()) {
			compound.setString("owner", owner); 
		}
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
		this.lock.deserializeNBT(compound.getCompoundTag("lock"));
		
		if(compound.hasKey("customName")) {
			this.customName = compound.getString("customName");
		}
		if(compound.hasKey("owner")) {
			this.owner  = compound.getString("owner");
		}
		super.readFromNBT(compound);
	}
	
	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.handleUpdateTag(tag);
		if(world.isRemote) {
			NBTTagCompound toSend = new NBTTagCompound();
			toSend.setBoolean("client_flag", true);
			toSend.setInteger("world_dim_id", world.provider.getDimension());
			toSend.setTag("player_id", NBTUtil.createUUIDTag(Minecraft.getMinecraft().player.getUniqueID()));
			toSend.setLong("pos", this.pos.toLong());
			TMOPacketHandler.network.sendToServer(new PacketClientToServer(PacketDataHandlers.TE_UPDATE_HANDLER, toSend));
		}
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
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return this.hasCapability(capability, facing)? (T)inventory : super.getCapability(capability, facing);
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
        
        if(this.world.isRemote) {

        this.prevLidAngle = this.lidAngle;
        float f1 = 0.1F;

        if (this.numPlayersUsing > 0 && this.lidAngle == 0.0F) {
            double d1 = (double)i + 0.5D;
            double d2 = (double)k + 0.5D;

            Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_OPEN, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numPlayersUsing == 0 && this.lidAngle > 0.0F || this.numPlayersUsing > 0 && this.lidAngle < 1.0F) {
            float f2 = this.lidAngle;

            if (this.numPlayersUsing > 0) {
                this.lidAngle += 0.1F;
            }
            else {
                this.lidAngle -= 0.1F;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float f3 = 0.5F;

            if (this.lidAngle < 0.5F && f2 >= 0.5F) {
                double d3 = (double)i + 0.5D;
                double d0 = (double)k + 0.5D;
                
                Minecraft.getMinecraft().player.playSound(SoundEvents.BLOCK_CHEST_CLOSE, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F)
            {
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
    public boolean hasOwner() {
    	return this.owner != null && !this.owner.isEmpty();
    }
    
    @Override
    public String getOwner() {
    	return this.owner;
    }
    
    @Override
    public void setOwner(String owner) {
    	this.owner = owner;
    	markDirty();
    }
    
    @Override
    public boolean isLocked() {
    	ItemStack lock = this.getLockItemHandler().getStackInSlot(0).copy();
    	boolean z = !ItemLock.isBroken(lock);
    	return lock.isEmpty() || lock.getItem() != ModItems.Items.LOCK.getItem()? false : !ItemLock.isBroken(lock);
    }
    
}
