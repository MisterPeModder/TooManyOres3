package misterpemodder.tmo.main.inventory;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.inventory.slot.SlotFiltered;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityDestabilizer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerDestabilizer extends ContainerMachine<TileEntityDestabilizer>{
	
	/*
	 * SLOT CONFIG:  
	 * 
	 * Player Inventory 9-35 .. 0 - 26
	 * Player Hotbar	0-8 ... 27 - 35
	 * Player Armor     0-3 ... 36 - 39
	 * Offhand          0-0 ... 40 - 40
	 * Input			0-0 ... 41 - 41
	 * Ender            0-0 ... 42 - 42
	 * Crafing result   0-0 ... 43 - 43
	 * Crafing matrix   0-8 ... 44 - 52
	 * Baubles          0-6 ... 53 - 59
	 */
	
	public ContainerElementTank tank;
	
	public ContainerDestabilizer(TileEntityDestabilizer te, InventoryPlayer playerInv) {
		super(te, playerInv, 100);
	}
	
	@Override
	protected List<Integer> getDefaultSlotIndexes() {
		return Arrays.asList(41,42);
	}
	
	@Override
	protected void setTeSlots(TileEntityDestabilizer te) {
		this.addSlotToContainer(new SlotHidable(te.getInventory(), 0, 78, 42, true));
		
		this.addSlotToContainer(new SlotFiltered(te.getEnder(), 0, 16, 42, true, new Predicate<ItemStack>(){
			public boolean apply(ItemStack t) {
				return TooManyOresAPI.methodHandler.isEnderMatterItem(t);
			}
		}));
	}
	
	@Override
	protected Iterable<ISyncedContainerElement> getContainerElements() {
		this.tank = new ContainerElementTank(0, this, 161, 9, ((TileEntityDestabilizer)te).getTank());
		return Arrays.asList(this.tank);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            //Input & Output slots
            if (index >= 41 && index < 60) {
                if(!this.mergeItemStack(itemstack1, 0, 40, false)) {
                    return ItemStack.EMPTY;
                } else {
                	te.getWorld().updateComparatorOutputLevel(te.getPos(), te.getBlockType());
                }
            }
            //player inv
            else if(index < 41) {
				if ((TooManyOresAPI.methodHandler.isEnderMatterItem(itemstack)
						&& !this.mergeItemStack(itemstack1, 42, 43, false))
						|| !this.mergeItemStack(itemstack1, 41, 42, false)) {
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
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		
		for (int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (!itemstack.isEmpty()) {
            	if (!this.mergeItemStack(itemstack, 0, 42, false)) {
            		playerIn.dropItem(itemstack, false);
            	}
            }
        }

        this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
	}
	
}
