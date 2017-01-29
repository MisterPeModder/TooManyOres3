package misterpemodder.tmo.main.client.gui;

import invtweaks.api.container.ChestContainer;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

@ChestContainer(rowSize = 11)
public class ContainerTitaniumChest extends ContainerBase<TileEntityTitaniumChest> {
	
	private static final int numRows = 6;
	private static final int size = 66;
	
	/*
	 * SLOTS:
	 * 
	 * Titanium Chest 	0-65 ...0 - 65
	 * Player Inventory 9-35 .. 66 - 92
	 * Player Hotbar	0-8 ... 93 - 101
	 * Lock Slot        0-0 ... 102 - 102
	 */
	
	public ContainerTitaniumChest(TileEntityTitaniumChest te, PlayerMainInvWrapper playerInv) {
		super(te, playerInv);
	}
	
	@Override
	protected void setTeSlots(TileEntityTitaniumChest te, PlayerMainInvWrapper playerInv) {
		ItemStackHandler h = te.getInventory();
		for (int y = 0; y < ContainerTitaniumChest.numRows; ++y) {
            for (int x = 0; x < 11; ++x) {
                this.addSlotToContainer(new SlotHidable(h, x + y * 11, 8 + x * 18, 18 + y * 18, false));
            }
        }
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            int mIndex = playerIn.inventory.mainInventory.size() + ContainerTitaniumChest.size;
       
            if (index < ContainerTitaniumChest.numRows * 11) {
                if(!this.mergeItemStack(itemstack1, ContainerTitaniumChest.numRows * 11, mIndex, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if(index < mIndex) {
            	Slot lockSlot = this.inventorySlots.get(mIndex);
            	if (lockSlot.isItemValid(itemstack1) && lockSlot.getStack().isEmpty()) {
            		if (!this.mergeItemStack(itemstack1, mIndex, mIndex+1, false)) {
                    	return ItemStack.EMPTY;
                	}
            	}
            	else if (!this.mergeItemStack(itemstack1, 0, ContainerTitaniumChest.numRows * 11, false)) {
                	return ItemStack.EMPTY;
            	}
            }
            else if(index == mIndex) {
            	if (!this.mergeItemStack(itemstack1, ContainerTitaniumChest.numRows * 11, mIndex, false)) {
                	return ItemStack.EMPTY;
            	}
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            }
            else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

}
