package misterpemodder.tmo.main.client.gui;

import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.MutablePair;

import misterpemodder.tmo.api.block.ILockable;
import misterpemodder.tmo.api.item.IItemLock;
import misterpemodder.tmo.main.client.gui.slot.IHidable;
import misterpemodder.tmo.main.client.gui.slot.SlotFiltered;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.client.gui.tabs.TabBase;
import misterpemodder.tmo.main.tileentity.TileEntityContainerBase;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerArmorInvWrapper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;
import net.minecraftforge.items.wrapper.PlayerOffhandInvWrapper;

public abstract class ContainerBase<TE extends TileEntityContainerBase> extends Container {
	
	protected static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[] {EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD};
	public static final int DEFAULT_OFFSET = 132;
	
	protected TE te;
	protected PlayerMainInvWrapper playerMainInv;
	protected PlayerOffhandInvWrapper playerOffhandInv;
	protected PlayerArmorInvWrapper playerArmorInv;
	
	protected MutablePair<TabBase, TabBase> selectedTabs = new MutablePair<>();
	
	public ContainerBase(TE te, InventoryPlayer playerInv) {
		this.te = te;
		
		this.playerMainInv = new PlayerMainInvWrapper(playerInv);
		this.playerOffhandInv = new PlayerOffhandInvWrapper(playerInv);
		this.playerArmorInv = new PlayerArmorInvWrapper(playerInv);
		
		setPlayerInvSlots();
		setLockSlot();
		setTeSlots(te);
		setExtraInvSlots();
		
		hideSlots();
	}

	public void hideSlots() {
		for(Slot sl : this.inventorySlots) {
			if(sl instanceof IHidable) {
				IHidable slot = (IHidable) sl;
				if(selectedTabs.getLeft() != null && selectedTabs.getRight() != null) {
					slot.setEnabled(selectedTabs.getLeft().shouldDisplaySlot(slot) || selectedTabs.getRight().shouldDisplaySlot(slot));
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
	
	protected void setPlayerInvSlots() {
		//Main Inventory
	    for (int y = 0; y < 3; ++y) {
	        for (int x = 0; x < 9; ++x) {
	            this.addSlotToContainer(new SlotHidable(playerMainInv, x + y * 9 + 9, 26 + x * 18, DEFAULT_OFFSET+18 + y * 18, true));
	        }
	    }
	 
	    //Hotbar
	    for (int x = 0; x < 9; ++x) {
	        this.addSlotToContainer(new SlotHidable(playerMainInv, x, 26 + x * 18, DEFAULT_OFFSET+76, true));
	    }
	    
	    //Armor
	    for (int y = 0; y < 4; ++y) {
	    	final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[y];
	    	Predicate<ItemStack> armorTest = new Predicate<ItemStack>() {
				public boolean test(ItemStack stack) {
					return stack.getItem().isValidArmor(stack, entityequipmentslot, playerArmorInv.getInventoryPlayer().player);
				}
			};
			SlotFiltered slot = new SlotFiltered(playerArmorInv, y, 61, DEFAULT_OFFSET+69 - y * 18, true, armorTest);
	    	slot.setBackgroundName(ItemArmor.EMPTY_SLOT_NAMES[y]);
			this.addSlotToContainer(slot);
	    }
	    
	    //Offhand
	    int o = TMORefs.baublesEnabled? 19 : 0;
	    Slot slot = new SlotHidable(playerOffhandInv, 0, 83+o, DEFAULT_OFFSET+69, true);
	    slot.setBackgroundName("minecraft:items/empty_armor_slot_shield");
	    this.addSlotToContainer(slot);
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
	
}
