package misterpemodder.tmo.main.inventory;

import java.util.Arrays;
import java.util.List;

import misterpemodder.hc.main.inventory.elements.ContainerElementVerticalEnergyBar;
import misterpemodder.hc.main.inventory.elements.ISyncedContainerElement;
import misterpemodder.hc.main.inventory.slot.SlotHidable;
import misterpemodder.tmo.main.inventory.elements.ContainerElementTank;
import misterpemodder.tmo.main.tileentity.TileEntityThemoelectricGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class ContainerThermoelectricGenerator extends ContainerBaseTMO<TileEntityThemoelectricGenerator> {
	
	/*
	 * SLOT CONFIG:  
	 * 
	 * Player Inventory 9-35 .. 0 - 26
	 * Player Hotbar	0-8 ... 27 - 35
	 * Player Armor     0-3 ... 36 - 39
	 * Offhand          0-0 ... 40 - 40
	 * Charging slots   0-1 ... 41 - 42
	 * Tank bucket      0-1 ... 43 - 44
	 * Crafing result   0-0 ... 45 - 45
	 * Crafing matrix   0-8 ... 46 - 54
	 * Baubles          0-6 ... 55 - 62
	 */
	
	public ContainerElementTank leftTank;
	public ContainerElementTank rightTank;
	public ContainerElementVerticalEnergyBar energyBar;
	private ContainerElementSmallEnergyBar smallEnergyBar;

	public ContainerThermoelectricGenerator(TileEntityThemoelectricGenerator te, InventoryPlayer playerInv) {
		super(te, playerInv, 100);
	}

	@Override
	protected List<Integer> getDefaultSlotIndexes() {
		return Arrays.asList(41,42,43,44);
	}
	
	@Override
	protected void setTeSlots(TileEntityThemoelectricGenerator te) {
		this.addSlotToContainer(new SlotHidable(te.getInventory(), 0, 159, 74, true));
		this.addSlotToContainer(new SlotHidable(te.getInventory(), 1, 186, 70, true));
		
		this.addSlotToContainer(new SlotHidable(te.getTankBucketSlots(), 0, 50, 14, true));
		this.addSlotToContainer(new SlotHidable(te.getTankBucketSlots(), 1, 88, 14, true));
	}
	
	@Override
	protected List<ISyncedContainerElement> addContainerElements(List<ISyncedContainerElement> elements) {
		
		this.leftTank = new ContainerElementTank(0, this, 8, 14, te.getLeftTank(), false, true);
		this.rightTank = new ContainerElementTank(1, this, 106, 14, te.getRightTank(), true, false);
		this.energyBar = new ContainerElementVerticalEnergyBar(te.getEnergyStorage(), () -> te.getEnergyRate());
		this.smallEnergyBar = new ContainerElementSmallEnergyBar(te);
		
		elements.add(this.leftTank);
		elements.add(this.rightTank);
		elements.add(this.energyBar);
		elements.add(this.smallEnergyBar);
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
                	te.getWorld().updateComparatorOutputLevel(te.getPos(), te.getBlockType());
                }
            }
            //player inv
            else if(index < 41) {
				if (!this.mergeItemStack(itemstack1, 41, 42, false) && !this.mergeItemStack(itemstack1, 43, 45, false)) {
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
	
	public int getSmallEnergyBarFillPercent() {
		return this.smallEnergyBar != null ? this.smallEnergyBar.fillPercent : 0;
	}
	
	private static class ContainerElementSmallEnergyBar implements ISyncedContainerElement {
		
		private final TileEntityThemoelectricGenerator te;
		public int fillPercent = 0;
		
		public ContainerElementSmallEnergyBar(TileEntityThemoelectricGenerator te) {
			this.te = te;
		}

		@Override
		public boolean shouldSendDataToClient() {
			
			ItemStack stack = te.getInventory().getStackInSlot(0);
			
			int p = 0;
			if(stack != ItemStack.EMPTY && stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
				IEnergyStorage h = stack.getCapability(CapabilityEnergy.ENERGY, null);
				p = (h.getEnergyStored() * 100)/h.getMaxEnergyStored();
			}
			
			if(p != this.fillPercent) {
				this.fillPercent = p;
				return true;
			}
			
			return false;
		}

		@Override
		public NBTTagCompound writeData(NBTTagCompound data) {
			data.setInteger("percent", this.fillPercent);
			return data;
		}

		@Override
		public void procData(NBTTagCompound data) {
			this.fillPercent = data.getInteger("percent");
		}
		
	}

}
