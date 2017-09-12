package misterpemodder.tmo.main.tileentity;

import misterpemodder.hc.api.block.ILockable;
import misterpemodder.hc.main.tileentity.TileEntityCustomChest;
import misterpemodder.tmo.main.blocks.containers.BlockTitaniumChest;
import misterpemodder.tmo.main.capability.item.ItemStackHandlerLockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityTitaniumChest extends TileEntityCustomChest implements ILockable {
	
	private ItemStackHandler lock;

	private boolean locked = false;
	public static final int MAX_UPDATE_TIME = 200;
    
    public int ticksSinceUpdate;
    
    public TileEntityTitaniumChest() {
    	super();
    	this.lock = new ItemStackHandlerLockable(this, 1);
	}
    
    @Override
    public int getInventorySize() {
    	return 66;
    }
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {;
		compound.setBoolean("locked", locked);
		return super.writeToNBT(compound);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		this.locked = compound.getBoolean("locked");
		super.readFromNBT(compound);
	}
	
	@Override
	public ItemStackHandler getLockItemHandler() {
		return this.lock;
	}
	
	@SideOnly(Side.CLIENT)
	protected void additionalClientUpdate() {
		if(ticksSinceUpdate >= MAX_UPDATE_TIME) {
			ticksSinceUpdate = 0;
			this.sync();
		} else {
			ticksSinceUpdate++;
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
	public void setLocked(boolean locked) {
		this.locked = locked;
		this.markDirty();
		this.sync();
	}
    
    @Override
    public boolean isLocked() {
    	return this.locked;
    }
    
}
