package misterpemodder.tmo.main.client.gui;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.MutablePair;

import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotFiltered;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotHidableInventory;
import misterpemodder.tmo.main.client.gui.slot.SlotHidableInventory.SlotHidableCrafting;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public abstract class ContainerBase<TE extends TileEntityContainerBase> extends Container {
	
	protected static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
	public final int BPART_OFFSET;
	
	protected TE te;
	protected PlayerMainInvWrapper playerMainInv;
	protected PlayerOffhandInvWrapper playerOffhandInv;
	protected PlayerArmorInvWrapper playerArmorInv;
	
	public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public IBaublesItemHandler baublesInv;
    
	protected MutablePair<TabBase, TabBase> selectedTabs = new MutablePair<>();
	public EntityPlayer player;
	
	public ContainerBase(TE te, InventoryPlayer playerInv, int bPartOffset) {
		this(te, playerInv, bPartOffset, true);
	}
	
	public ContainerBase(TE te, InventoryPlayer playerInv, int bPartOffset, boolean hasArmorTab) {
		this.te = te;
		
		BPART_OFFSET = bPartOffset;
		this.player = playerInv.player;
		this.playerMainInv = new PlayerMainInvWrapper(playerInv);
		
		if(hasArmorTab) {
			this.playerOffhandInv = new PlayerOffhandInvWrapper(playerInv);
			this.playerArmorInv = new PlayerArmorInvWrapper(playerInv);
		}
		
		setPlayerInvSlots(hasArmorTab);
		setLockSlot();
		setTeSlots(te);
		setCraftingSlots(hasArmorTab);
		setExtraInvSlots();
		
		hideSlots();
	}

	public void hideSlots() {
		for(Slot sl : this.inventorySlots) {
			if(sl instanceof IHidable) {
				IHidable slot = (IHidable) sl;
				if(selectedTabs.getLeft() != null && selectedTabs.getRight() != null) {
					slot.setEnabled(selectedTabs.getLeft().shouldDisplaySlot(slot) || selectedTabs.getRight().shouldDisplaySlot(slot));
				} else if(getDefaultSlotIndexes().contains(sl.slotNumber)){
					slot.setEnabled(true);
				} else if(slot instanceof SlotHidable) {
					IItemHandler h = ((SlotHidable)slot).getItemHandler();
					slot.setEnabled(h == te.getInventory() || h == playerMainInv);
				} else {
					slot.setEnabled(false);
				}
			}
		}
	}
	
	public TE getTileEntity() {
		return this.te;
	}
	
	public PlayerMainInvWrapper getPlayerInv() {
		return this.playerMainInv;
	}
	
	public PlayerOffhandInvWrapper getPlayerOffandInv() {
		return this.playerOffhandInv;
	}
	
	public PlayerArmorInvWrapper getPlayerArmorInv() {
		return this.playerArmorInv;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}
	
	protected abstract void setTeSlots(TE te);
	
	protected abstract List<Integer> getDefaultSlotIndexes();
	
	protected void setPlayerInvSlots(boolean hasArmorTab) {
		//Main Inventory
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new SlotHidable(playerMainInv, x + y * 9 + 9, 26 + x * 18, BPART_OFFSET+18 + y * 18, true));
	        }
	    }
	 
	    //Hotbar
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new SlotHidable(playerMainInv, x, 26 + x * 18, BPART_OFFSET+76, true));
	    }
	    
	    if(hasArmorTab) {
			//Armor
	    	for (int y = 0; y < 4; ++y) {
	    		final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[y];
	    		Predicate<ItemStack> armorTest = new Predicate<ItemStack>() {
					public boolean test(ItemStack stack) {
						return stack.getItem().isValidArmor(stack, entityequipmentslot, player);
					}
				};
				SlotFiltered slot = new SlotFiltered(playerArmorInv, y, 61, BPART_OFFSET+69 - y * 18, true, armorTest);
	    		slot.setBackgroundName(ItemArmor.EMPTY_SLOT_NAMES[y]);
				this.addSlotToContainer(slot);
	    	}
	    
	    	//Offhand
	    	int o = TMORefs.baublesEnabled? 19 : 0;
	    	Slot slot = new SlotHidable(playerOffhandInv, 0, 83+o, BPART_OFFSET+69, true);
	    	slot.setBackgroundName("minecraft:items/empty_armor_slot_shield");
	    	this.addSlotToContainer(slot);
		}
	}
	
	protected void setCraftingSlots(boolean hasArmorTab) {
		if(hasArmorTab) {

			craftMatrix = new InventoryCrafting(this, 3, 3);
			craftResult = new InventoryCraftResult();
			
			this.addSlotToContainer(new SlotHidableCrafting(playerMainInv.getInventoryPlayer().player, this.craftMatrix, this.craftResult, 0, 181, BPART_OFFSET+75));

	        for (int y = 0; y < 3; ++y) {
	            for (int x = 0; x < 3; ++x) {
	                this.addSlotToContainer(new SlotHidableInventory(this.craftMatrix, x + y * 3, 149 + x * 18, BPART_OFFSET+15 + y * 18));
	            }
	        }
	        
	        if(TMORefs.baublesEnabled) {
				baublesInv = BaublesApi.getBaublesHandler(player);
				for (int x = 0; x < 2; ++x) {
					for (int y = 0; y < 4; ++y) {
		            	if(y == 3 && x == 1) break;
		                this.addSlotToContainer(new SlotHidable(this.baublesInv, y + x * 4, 83 + x * 19, BPART_OFFSET+15 + y * 18, true));
		            }
		        }
			}
	        
	        this.onCraftMatrixChanged(this.craftMatrix);
		}
	}
	
	protected void setLockSlot() {
		if(this.te instanceof ILockable) {
			Predicate<ItemStack> lockTest = new Predicate<ItemStack>() {
				public boolean test(ItemStack stack) {
					return stack.getItem() instanceof IItemLock;
				}
			};
			SlotFiltered ls = new SlotFiltered(((ILockable)this.te).getLockItemHandler(), 0, 8, 18, true, lockTest);
			//ls.setBackgroundName(TMORefs.PREFIX+"empty_lock_slot.png");
			//ls.setBackgroundLocation(new ResourceLocationTmo("textures/items/empty_lock_slot.png"));
			this.addSlotToContainer(ls);
		}
	}
	
	//override this to add extra slots
	protected void setExtraInvSlots() {
	}
	
	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		te.onInvClose(playerIn);
		super.onContainerClosed(playerIn);
	}
	
	public void onCraftMatrixChanged(IInventory inventoryIn) {
		if(craftResult != null)
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, player.world));
    }
	
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return craftResult != null && slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }
	
}
