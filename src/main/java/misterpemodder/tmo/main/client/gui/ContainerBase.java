package misterpemodder.tmo.main.client.gui;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public abstract class ContainerBase<TE extends TileEntityContainerBase> extends Container {
	
	protected TE te;
	protected PlayerMainInvWrapper playerInv;
	
	protected MutablePair<TabBase, TabBase> selectedTabs = new MutablePair<>();
	
	public ContainerBase(TE te, PlayerMainInvWrapper playerInv) {
		this.te = te;
		this.playerInv = playerInv;
		
		setTeSlots(te, playerInv);
		setPlayerInvSlots();
		setLockSlot();
		
		hideSlots();
	}

	public void hideSlots() {
		for(Slot sl : this.inventorySlots) {
			if(sl instanceof SlotHidable) {
				SlotHidable slot = (SlotHidable) sl;
				if(selectedTabs.getLeft() != null && selectedTabs.getRight() != null) {
					slot.enabled = selectedTabs.getLeft().shouldDisplaySlot(slot) || selectedTabs.getRight().shouldDisplaySlot(slot);
				} else {
					slot.enabled = slot.getItemHandler() == te.getInventory() || slot.getItemHandler() == playerInv;
				}
			}
		}
	}
	
	public TE getTileEntity() {
		return this.te;
	}
	
	public PlayerMainInvWrapper getPlayerInv() {
		return this.playerInv;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	protected abstract void setTeSlots(TE te, PlayerMainInvWrapper playerInv);
	
	protected void setPlayerInvSlots() {
		//Inventory
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new SlotHidable(playerInv, x + y * 9 + 9, 26 + x * 18, 150 + y * 18, true));
	        }
	    }
	 
	    //Hotbar
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new SlotHidable(playerInv, x, 26 + x * 18, 208, true));
	    }
	}
	
	protected void setLockSlot() {
		if(this.te instanceof ILockable) {
			NonNullList<ItemStack> list = NonNullList.create();
			list.add(new ItemStack(ModItems.TheItems.LOCK.getItem()));
			this.addSlotToContainer(new SlotFiltered(((ILockable)this.te).getLockItemHandler(), 0, 8, 18, list, true, true));
		}
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		te.onInvClose(playerIn);
		super.onContainerClosed(playerIn);
	}
	
}
