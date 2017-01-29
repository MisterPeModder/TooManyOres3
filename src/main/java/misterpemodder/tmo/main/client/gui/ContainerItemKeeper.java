package misterpemodder.tmo.main.client.gui;



import misterpemodder.tmo.main.tileentity.TileEntityItemKeeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerItemKeeper extends Container {
	
	private TileEntityItemKeeper te;
	
	public ContainerItemKeeper(TileEntityItemKeeper te, IInventory playerInv) {
		this.te = te;
		
		/*
		 * SLOTS:
		 * 
		 * Item Keeper 0 .......... 0
		 * Player Inventory 9-35 .. 1  - 27
		 * Player Inventory 0-8 ... 28 - 36
		 */
		
		// Item Keeper
		ItemStackHandler h = te.getInventory();
		this.addSlotToContainer(new SlotItemHandler(h,0 , 80, 35));

	    // Player Inventory
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
	        }
	    }

	    // Player Hotbar
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 142));
	    }
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0)
            {
                if (!this.mergeItemStack(itemstack1, 1, 36, true))
                {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 0, 1, false))
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
}
