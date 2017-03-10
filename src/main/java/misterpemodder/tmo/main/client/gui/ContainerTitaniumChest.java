package misterpemodder.tmo.main.client.gui;

import java.util.ArrayList;
import java.util.List;

import invtweaks.api.container.ChestContainer;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

@ChestContainer(rowSize = 11)
public class ContainerTitaniumChest extends ContainerBase<TileEntityTitaniumChest> {
	
	private static final int numRows = 6;
	
	/*
	 * NEW SLOTS:
	 * 
	 * Player Inventory 9-35 .. 0 - 26
	 * Player Hotbar	0-8 ... 27 - 35
	 * Player Armor     0-3 ... 36 - 39
	 * Offhand          0-0 ... 40 - 40
	 * Lock Slot        0-0 ... 41 - 41
	 * Titanium Chest 	0-65 .. 42 - 107
	 * Crafing matrix   0-8 ... 108 - 116
	 * Crafing result   0-0 ... 117 - 117
	 * Baubles          0-6 ... 118 - 124 
	 */

	public ContainerTitaniumChest(TileEntityTitaniumChest te, InventoryPlayer playerInv) {
		super(te, playerInv, 132);
	}
	
	@Override
	protected List<Integer> getDefaultSlotIndexes() {
		return new ArrayList<Integer>();
	}
	
	@Override
	protected void setTeSlots(TileEntityTitaniumChest te) {
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
            
            //Titanium chest slots
            if (index >= 42 && index < 107) {
                if(!this.mergeItemStack(itemstack1, 0, 40, false)) {
                    return ItemStack.EMPTY;
                } else {
                	//Workaround for comparator output level not updating when removing an item with shift-click
                	te.getWorld().updateComparatorOutputLevel(te.getPos(), te.getBlockType());
                }
            }
            //player inv
            else if(index < 41 || (index >= 108 && index < 125)) {
            	Slot lockSlot = this.inventorySlots.get(41);
            	if (lockSlot.isItemValid(itemstack1) && lockSlot.getStack().isEmpty()) {
            		if (!this.mergeItemStack(itemstack1, 41, 42, false)) {
                    	return ItemStack.EMPTY;
                	}
            	}
            	else if (!this.mergeItemStack(itemstack1, 42, 107, false)) {
                	return ItemStack.EMPTY;
            	}
            }
            //lock slot
            else if(index == 41) {
            	if (!this.mergeItemStack(itemstack1, 0, 40, false)) {
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
	
	public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);

        for (int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (!itemstack.isEmpty()) {
            	if (!this.mergeItemStack(itemstack, 42, 107, false)) {
            		playerIn.dropItem(itemstack, false);
            	}
            }
        }

        this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
    }

}
