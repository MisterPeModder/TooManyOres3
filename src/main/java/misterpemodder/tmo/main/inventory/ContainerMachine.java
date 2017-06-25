package misterpemodder.tmo.main.inventory;

import misterpemodder.tmo.main.network.PacketDataHandlers;
import misterpemodder.tmo.main.network.TMOPacketHandler;
import misterpemodder.tmo.main.network.packet.PacketServerToClient;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ContainerMachine<TE extends TileEntityMachine> extends ContainerBase<TE> {

	public int progress;
	
	public ContainerMachine(TE te, InventoryPlayer playerInv, int bPartOffset) {
		super(te, playerInv, bPartOffset);
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		TE te = this.getTileEntity();
		if(te != null && te.getCurrentRecipe() != null)
		listener.sendProgressBarUpdate(this, 0, this.getTileEntity().getProgress());
	}
	
	@Override
	public void detectAndSendChanges() {
        
		super.detectAndSendChanges();
		TE te = this.getTileEntity();
        if(te == null) return;
        int teProgress = 0;
        if(te.getCurrentRecipe() != null) {
        	teProgress = (te.getProgress()*28)/te.getCurrentRecipe().getTotalTime();
        }
        for (int i = 0; i < this.listeners.size(); ++i) {
            IContainerListener listener = (IContainerListener)this.listeners.get(i);

            if(this.progress != teProgress && listener instanceof EntityPlayerMP) {
            	NBTTagCompound toSend = new NBTTagCompound();
            	toSend.setInteger("progress", teProgress);
            	TMOPacketHandler.network.sendTo(new PacketServerToClient(PacketDataHandlers.PROGRESS_ARROW_UPDATE_HANDLER, toSend), (EntityPlayerMP)listener);
            }
        }

        this.progress = teProgress;
	}
	
}
