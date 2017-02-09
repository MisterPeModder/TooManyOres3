package misterpemodder.tmo.main.client.gui;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.main.client.gui.GuiTabs.EnumTabs;
import misterpemodder.tmo.main.init.ModItems;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public abstract class ContainerBase<TE extends TileEntityContainerBase> extends Container {
	
	protected TE te;
	protected PlayerMainInvWrapper playerInv;
	
	protected EnumTabs[] selectedTabs = new EnumTabs[2];
	
	public ContainerBase(TE te, PlayerMainInvWrapper playerInv) {
		this.te = te;
		this.playerInv = playerInv;
		this.selectedTabs[0] = EnumTabs.DEFAULT_TOP;
		this.selectedTabs[1] = EnumTabs.DEFAULT_BOTTOM;
		
		setTeSlots(te, playerInv);
		setPlayerInvSlots();
		setLockSlot();
		
		hideSlots();
	}

	protected void hideSlots() {
		for(Slot sl : this.inventorySlots) {
			if(sl instanceof SlotHidable) {
				SlotHidable slot = (SlotHidable) sl;
				IItemHandler h = slot.getItemHandler();
				
				boolean b1 = selectedTabs[1] != EnumTabs.PLAYER_INVENTORY && h == playerInv;
				boolean b2 = selectedTabs[0] != EnumTabs.MAIN && h == te.getInventory();
				boolean b3 = this.te instanceof ILockable
						? selectedTabs[0] != EnumTabs.SECURITY && h == ((ILockable) te).getLockItemHandler()
						: false;

				slot.enabled = !(b1||b2||b3);
				//slot.enabled = !(b1||b2)&&h != te.getInventory();
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
			list.add(new ItemStack(ModItems.Items.LOCK.getItem()));
			this.addSlotToContainer(new SlotFiltered(((ILockable)this.te).getLockItemHandler(), 0, 8, 18, list, true, true));
		}
	}
	
	private class TestSlot extends Slot{

		public TestSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
			super(inventoryIn, index, xPosition, yPosition);
		}
		
		@Override
		public boolean canBeHovered() {
			return false;
		}
		
		@Override
		public boolean canTakeStack(EntityPlayer playerIn) {
			return false;
		}

	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		te.onInvClose(playerIn);
		super.onContainerClosed(playerIn);
	}
	
}
