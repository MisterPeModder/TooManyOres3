package misterpemodder.tmo.main.client.gui;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import invtweaks.api.container.ChestContainer;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidableInventory;
import misterpemodder.tmo.main.client.gui.slot.SlotHidableInventory.SlotHidableCrafting;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.items.ItemStackHandler;

@ChestContainer(rowSize = 11)
public class ContainerTitaniumChest extends ContainerBase<TileEntityTitaniumChest> {
	
	private static final int numRows = 6;
	//private static final int size = 66;
	
	public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public IBaublesItemHandler baublesInv;
	
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
		super(te, playerInv);
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
	
	@Override
	protected void setExtraInvSlots() {
		craftMatrix = new InventoryCrafting(this, 3, 3);
		craftResult = new InventoryCraftResult();
		
		this.addSlotToContainer(new SlotHidableCrafting(playerMainInv.getInventoryPlayer().player, this.craftMatrix, this.craftResult, 0, 181, DEFAULT_OFFSET+75));

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                this.addSlotToContainer(new SlotHidableInventory(this.craftMatrix, x + y * 3, 149 + x * 18, DEFAULT_OFFSET+15 + y * 18));
            }
        }
        
        if(TMORefs.baublesEnabled) {
			baublesInv = BaublesApi.getBaublesHandler(playerMainInv.getInventoryPlayer().player);
			for (int x = 0; x < 2; ++x) {
				for (int y = 0; y < 4; ++y) {
	            	if(y == 3 && x == 1) break;
	                this.addSlotToContainer(new SlotHidable(this.baublesInv, y + x * 4, 83 + x * 19, DEFAULT_OFFSET+15 + y * 18, true));
	            }
	        }
		}
        
        this.onCraftMatrixChanged(this.craftMatrix);
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            //Titanium chest slots
            if (index >= 42 && index < 107) {
                if(!this.mergeItemStack(itemstack1, 0, 40, true)) {
                    return ItemStack.EMPTY;
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
	
	public void onCraftMatrixChanged(IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, playerMainInv.getInventoryPlayer().player.world));
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
	
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

}
