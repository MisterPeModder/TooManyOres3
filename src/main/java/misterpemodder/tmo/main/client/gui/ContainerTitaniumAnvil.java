package misterpemodder.tmo.main.client.gui;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import misterpemodder.tmo.api.item.IItemForgeHammer;
import misterpemodder.tmo.main.client.gui.slot.SlotFiltered;
import misterpemodder.tmo.main.client.gui.slot.SlotHidable;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumAnvil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerTitaniumAnvil extends ContainerBase<TileEntityTitaniumAnvil> {
	
	public InvWrapper input;
    public InvWrapper output;
    
    public int maximumCost;
    public int materialCost;
    private String repairedItemName;
    
    /*
     * SLOTS:
	 * 
	 * Player Inventory 9-35 .. 0 - 26
	 * Player Hotbar	0-8 ... 27 - 35
	 * Player Armor     0-3 ... 36 - 39
	 * Offhand          0-0 ... 40 - 40
	 * Hammer           0-0 ... 41 - 41
	 * Input            0-1 ... 42 - 43
	 * Output          	0-0 ... 44 - 44
	 * Crafing matrix   0-8 ... 45 - 53
	 * Crafing result   0-0 ... 54 - 54
	 * Baubles          0-6 ... 55 - 61
	 */

	public ContainerTitaniumAnvil(TileEntityTitaniumAnvil te, InventoryPlayer playerInv) {
		super(te, playerInv, 100);
	}
	
	@Override
	protected List<Integer> getDefaultSlotIndexes() {
		return Arrays.asList(41,42,43,44);
	}

	@Override
	protected void setTeSlots(TileEntityTitaniumAnvil te) {
		if(input == null)
		input = new InvWrapper(new InventoryBasic("Repair", true, 2) {
            public void markDirty() {
                super.markDirty();
                ContainerTitaniumAnvil.this.onCraftMatrixChanged(this);
            }
        });
		if(output==null)
		output = new InvWrapper(new InventoryCraftResult());
		
		Predicate<ItemStack> HammerFilter = new Predicate<ItemStack>(){
			public boolean test(ItemStack t) {
				return t.getItem() instanceof IItemForgeHammer;
			}
		};
		
		this.addSlotToContainer(new SlotFiltered(this.te.getInventory(), 0, 45, 26, true, HammerFilter){
			@Override
			public void onSlotChanged() {
				super.onSlotChanged();
				ContainerTitaniumAnvil.this.updateRepairOutput();
			}
		});
		
		this.addSlotToContainer(new SlotHidable(input, 0, 45, 53, true));
		this.addSlotToContainer(new SlotHidable(input, 1, 94, 53, true));
		
		Predicate<ItemStack> t = new Predicate<ItemStack>(){
			public boolean test(ItemStack t) {
				return false;
			}
		};
		
		this.addSlotToContainer(new SlotFiltered(output, 0, 152, 53, true, t) {
			
            public boolean canTakeStack(EntityPlayer player) {
                return (player.capabilities.isCreativeMode || player.experienceLevel >= ContainerTitaniumAnvil.this.maximumCost) && ContainerTitaniumAnvil.this.maximumCost > 0 && this.getHasStack();
            }
            
            public ItemStack onTake(EntityPlayer player, ItemStack stack) {
                if (!player.capabilities.isCreativeMode) {
                    player.addExperienceLevel(-ContainerTitaniumAnvil.this.maximumCost);
                }
                ContainerTitaniumAnvil.this.input.setStackInSlot(0, ItemStack.EMPTY);

                if (ContainerTitaniumAnvil.this.materialCost > 0) {
                    ItemStack itemstack1 = ContainerTitaniumAnvil.this.input.getStackInSlot(1);

                    if (!itemstack1.isEmpty() && itemstack1.getCount() > ContainerTitaniumAnvil.this.materialCost) {
                        itemstack1.shrink(ContainerTitaniumAnvil.this.materialCost);
                        ContainerTitaniumAnvil.this.input.setStackInSlot(1, itemstack1);
                    }
                    else {
                        ContainerTitaniumAnvil.this.input.setStackInSlot(1, ItemStack.EMPTY);
                    }
                }
                else {
                    ContainerTitaniumAnvil.this.input.setStackInSlot(1, ItemStack.EMPTY);
                }
                ItemStackHandler h = ContainerTitaniumAnvil.this.getTileEntity().getInventory();
                if(h.getStackInSlot(0).getItem() instanceof IItemForgeHammer) {
                	ItemStack stack1 = h.getStackInSlot(0).copy();
                	((IItemForgeHammer)stack1.getItem()).onHammerUsed(player, stack1, 10);
                	h.setStackInSlot(0, stack1);
                }
                player.playSound(SoundEvents.BLOCK_ANVIL_USE, 1.0F, 1.0F);
                return stack;
            }
        });
	}
	
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            
            //Titanium anvil slots
            if (index >= 41 && index < 45) {
                if(!this.mergeItemStack(itemstack1, 0, 35, true)) {
                    return ItemStack.EMPTY;
                }
            }
            //player inv & crafting grid
            else if(index < 41 || index > 44) {
            	Slot hammerSlot = this.inventorySlots.get(41);
            	if (hammerSlot.isItemValid(itemstack1) && hammerSlot.getStack().isEmpty()) {
            		if (!this.mergeItemStack(itemstack1, 41, 42, false)) {
                    	return ItemStack.EMPTY;
                	}
            	}
            	else if (!this.mergeItemStack(itemstack1, 42, 44, false)) {
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

        for(int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (!itemstack.isEmpty()) {
            	if (!this.mergeItemStack(itemstack, 0, 36, false)) {
            		playerIn.dropItem(itemstack, false);
            	}
            }
        }
        
        for(int i = 0; i < 2; ++i) {
        	ItemStack stack = input.extractItem(i, input.getStackInSlot(i).getCount(), false);
        	if (!stack.isEmpty()) {
            	if (!this.mergeItemStack(stack, 0, 36, false)) {
            		playerIn.dropItem(stack, false);
            	}
            }
        }

        craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
        output.setStackInSlot(0, ItemStack.EMPTY);
    }
	
	public void onCraftMatrixChanged(IInventory inventory) {
        super.onCraftMatrixChanged(inventory);

        if (inventory == input.getInv())  {
            this.updateRepairOutput();
        }
    }
	
	public void updateRepairOutput() {
        ItemStack itemstack = this.input.getStackInSlot(0);
        this.maximumCost = 1;
        int i = 0;
        int j = 0;
        int k = 0;

        if (itemstack.isEmpty() || !(this.getTileEntity().getInventory().getStackInSlot(0).getItem() instanceof IItemForgeHammer)) {
            this.output.setStackInSlot(0, ItemStack.EMPTY);
            this.maximumCost = 0;
        }
        else {
            ItemStack itemstack1 = itemstack.copy();

            ItemStack itemstack2 = this.input.getStackInSlot(1);
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
            j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
            this.materialCost = 0;
            boolean flag = false;

            if (!itemstack2.isEmpty()) {
                if (!ContainerTitaniumAnvil.onAnvilChange(this, itemstack, itemstack2, output.getInv(), repairedItemName, j)) return;
                flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !Items.ENCHANTED_BOOK.getEnchantments(itemstack2).hasNoTags();

                if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
                    int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

                    if (l2 <= 0) {
                    	this.output.setStackInSlot(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }

                    int i3;

                    for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
                        int j3 = itemstack1.getItemDamage() - l2;
                        itemstack1.setItemDamage(j3);
                        ++i;
                        l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
                    }

                    this.materialCost = i3;
                }
                else {
                    if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
                        this.output.setStackInSlot(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }

                    if (itemstack1.isItemStackDamageable() && !flag) {
                        int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
                        int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
                        int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
                        int k1 = l + j1;
                        int l1 = itemstack1.getMaxDamage() - k1;

                        if (l1 < 0)
                        {
                            l1 = 0;
                        }

                        if (l1 < itemstack1.getMetadata())
                        {
                            itemstack1.setItemDamage(l1);
                            i += 2;
                        }
                    }

                    Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
                    boolean flag2 = false;
                    boolean flag3 = false;

                    for (Enchantment enchantment1 : map1.keySet()) {
                        if (enchantment1 != null) {
                            int i2 = map.containsKey(enchantment1) ? ((Integer)map.get(enchantment1)).intValue() : 0;
                            int j2 = ((Integer)map1.get(enchantment1)).intValue();
                            j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
                            boolean flag1 = enchantment1.canApply(itemstack);

                            if (this.player.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
                                flag1 = true;
                            }

                            for (Enchantment enchantment : map.keySet()) {
                                if (enchantment != enchantment1 && !enchantment1.func_191560_c(enchantment)) {
                                    flag1 = false;
                                    ++i;
                                }
                            }

                            if (!flag1) {
                                flag3 = true;
                            }
                            else {
                                flag2 = true;

                                if (j2 > enchantment1.getMaxLevel()) {
                                    j2 = enchantment1.getMaxLevel();
                                }

                                map.put(enchantment1, Integer.valueOf(j2));
                                int k3 = 0;

                                switch (enchantment1.getRarity()) {
                                    case COMMON:
                                        k3 = 1;
                                        break;
                                    case UNCOMMON:
                                        k3 = 2;
                                        break;
                                    case RARE:
                                        k3 = 4;
                                        break;
                                    case VERY_RARE:
                                        k3 = 8;
                                }

                                if (flag) {
                                    k3 = Math.max(1, k3 / 2);
                                }

                                i += k3 * j2;
                            }
                        }
                    }

                    if (flag3 && !flag2) {	
                    	
                        this.output.setStackInSlot(0, ItemStack.EMPTY);
                        this.maximumCost = 0;
                        return;
                    }
                }
            }

            if (StringUtils.isBlank(this.repairedItemName)) {
                if (itemstack.hasDisplayName()) {
                    k = 1;
                    i += k;
                    itemstack1.clearCustomName();
                }
            }
            else if (!this.repairedItemName.equals(itemstack.getDisplayName())) {
                k = 1;
                i += k;
                itemstack1.setStackDisplayName(this.repairedItemName);
            }
            if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) itemstack1 = ItemStack.EMPTY;

            this.maximumCost = j + i;

            if (i <= 0) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (k == i && k > 0 && this.maximumCost >= 40) {
                this.maximumCost = 39;
            }

            if (this.maximumCost >= 40 && !this.player.capabilities.isCreativeMode) {
                itemstack1 = ItemStack.EMPTY;
            }

            if (!itemstack1.isEmpty()) {
                int k2 = itemstack1.getRepairCost();

                if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost())  {
                    k2 = itemstack2.getRepairCost();
                }

                if (k != i || k == 0) {
                    k2 = k2 * 2 + 1;
                }

                itemstack1.setRepairCost(k2);
                EnchantmentHelper.setEnchantments(map, itemstack1);
            }
            this.output.setStackInSlot(0, itemstack1);
            this.detectAndSendChanges();
        }
    }
	
	public void addListener(IContainerListener listener) {
        super.addListener(listener);
        listener.sendProgressBarUpdate(this, 0, this.maximumCost);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        if (id == 0) {
            this.maximumCost = data;
        }
    }
	
	//A version of ForgeHooks.onAnvilChange that works for ContainerTitaniumAnvil
	public static boolean onAnvilChange(ContainerTitaniumAnvil container, @Nonnull ItemStack left, @Nonnull ItemStack right, IInventory outputSlot, String name, int baseCost) {
        AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost);
        if (MinecraftForge.EVENT_BUS.post(e)) return false;
        if (e.getOutput().isEmpty()) return true;

        outputSlot.setInventorySlotContents(0, e.getOutput());
        container.maximumCost = e.getCost();
        container.materialCost = e.getMaterialCost();
        return false;
    }
	
	public void updateItemName(String newName) {
        this.repairedItemName = newName;

        if (!this.output.getStackInSlot(0).isEmpty())  {
            ItemStack stack = this.output.getStackInSlot(0).copy();

            if (StringUtils.isBlank(newName)) {
                stack.clearCustomName();
            }
            else {
            	stack.setStackDisplayName(this.repairedItemName);
            }
            this.output.setStackInSlot(0, stack);
        }

        this.updateRepairOutput();
    }

}
