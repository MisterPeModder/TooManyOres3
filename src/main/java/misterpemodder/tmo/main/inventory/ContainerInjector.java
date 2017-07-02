package misterpemodder.tmo.main.inventory;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Predicate;

import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.inventory.slot.SlotFiltered;
import misterpemodder.tmo.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityInjector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerInjector extends ContainerMachine<TileEntityInjector> {
	
	/*
	 * SLOT CONFIG:  
	 * 
	 * Player Inventory 9-35 .. 0 - 26
	 * Player Hotbar	0-8 ... 27 - 35
	 * Player Armor     0-3 ... 36 - 39
	 * Offhand          0-0 ... 40 - 40
	 * Input			0-0 ... 41 - 41
	 * Output           0-0 ... 42 - 42
	 * Crafing result   0-0 ... 43 - 43
	 * Crafing matrix   0-8 ... 44 - 52
	 * Baubles          0-6 ... 53 - 59
	 */
	
	public ContainerElementTank tank;

	public ContainerInjector(TileEntityInjector te, InventoryPlayer playerInv) {
		super(te, playerInv, 100);
	}
	
	@Override
	protected List<Integer> getDefaultSlotIndexes() {
		return Arrays.asList(41,42);
	}
	
	@Override
	protected void setTeSlots(TileEntityInjector te) {
		this.addSlotToContainer(new SlotHidable(te.getInventory(), 0, 107, 42, true));
		
		this.addSlotToContainer(new SlotFiltered(te.output, 0, 155, 42, true, new Predicate<ItemStack>(){
			public boolean apply(ItemStack t) {
				return false;
			}
		}));
	}
	
	@Override
	protected List<ISyncedContainerElement> addContainerElements(List<ISyncedContainerElement> elements) {		
		this.tank = new ContainerElementTank(0, this, 11, 10, te.getTank());
		elements.add(this.tank);
		return super.addContainerElements(elements);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            //Input & Output slots
            if (index >= 41 && index < 60) {
                if(!this.mergeItemStack(itemstack1, 0, 40, false)) {
                    return ItemStack.EMPTY;
                } else {
                	//Workaround for comparator output level not updating when removing an item with shift-click
                	te.getWorld().updateComparatorOutputLevel(te.getPos(), te.getBlockType());
                }
            }
            //player inv
            else if(index < 41) {
            	if (!this.mergeItemStack(itemstack1, 41, 42, false)) {
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
