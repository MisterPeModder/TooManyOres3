package com.misterpemodder.tmo.block.entity;

import com.misterpemodder.tmo.tag.TmoItemTags;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class TitaniumAnvilBlockEntity extends BlockEntity implements Inventory {
  protected Inventory inventory;

  public TitaniumAnvilBlockEntity() {
    super(TmoBlockEntityTypes.TITANIUM_ANVIL);
    this.inventory = new BasicInventory(1) {
      @Override
      public boolean isValidInvStack(int slot, ItemStack stack) {
        return TmoItemTags.HAMMERS.contains(stack.getItem());
      }
    };
  }

  @Override
  public void fromTag(CompoundTag tag) {
    if (tag.containsKey("hammer", NbtType.COMPOUND))
      this.inventory.setInvStack(0, ItemStack.fromTag(tag.getCompound("hammer")));
    super.fromTag(tag);
  }

  @Override
  public CompoundTag toTag(CompoundTag tag) {
    ItemStack hammerStack = this.inventory.getInvStack(0);
    if (!hammerStack.isEmpty())
      tag.put("hammer", hammerStack.toTag(new CompoundTag()));
    return super.toTag(tag);
  }

  @Override
  public int getInvSize() {
    return this.inventory.getInvSize();
  }

  @Override
  public boolean isInvEmpty() {
    return this.inventory.isInvEmpty();
  }

  @Override
  public ItemStack getInvStack(int slot) {
    return this.inventory.getInvStack(slot);
  }

  @Override
  public ItemStack takeInvStack(int slot, int count) {
    return this.inventory.takeInvStack(slot, count);
  }

  @Override
  public ItemStack removeInvStack(int slot) {
    return this.inventory.removeInvStack(slot);
  }

  @Override
  public void setInvStack(int slot, ItemStack stack) {
    this.inventory.setInvStack(slot, stack);
  }

  @Override
  public int getInvMaxStackAmount() {
    return 1;
  }

  @Override
  public boolean canPlayerUseInv(PlayerEntity player) {
    return this.inventory.canPlayerUseInv(player);
  }

  @Override
  public boolean isValidInvStack(int slot, ItemStack stack) {
    return this.inventory.isValidInvStack(slot, stack);
  }

  @Override
  public void clear() {
    this.inventory.clear();
  }
}
