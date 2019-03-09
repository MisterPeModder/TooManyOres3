package com.misterpemodder.tmo.block.entity;

import java.util.Iterator;
import com.misterpemodder.tmo.TooManyOresNetworking;
import com.misterpemodder.tmo.tag.TmoItemTags;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.server.PlayerStream;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.network.packet.BlockEntityUpdateS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class TitaniumAnvilBlockEntity extends BlockEntity implements Inventory {
  protected Inventory inventory;

  protected <T extends BlockEntity> TitaniumAnvilBlockEntity(BlockEntityType<T> type) {
    super(type);
    this.inventory = new BasicInventory(1) {
      @Override
      public boolean isValidInvStack(int slot, ItemStack stack) {
        return TmoItemTags.HAMMERS.contains(stack.getItem());
      }
    };
  }

  public TitaniumAnvilBlockEntity() {
    this(TmoBlockEntityTypes.TITANIUM_ANVIL);
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
    ItemStack stack = this.inventory.takeInvStack(slot, count);
    /*
     * if (stack != ItemStack.EMPTY) markDirty();
     */
    return stack;
  }

  @Override
  public ItemStack removeInvStack(int slot) {
    ItemStack stack = this.inventory.removeInvStack(slot);
    /*
     * if (stack != ItemStack.EMPTY) markDirty();
     */
    return stack;
  }

  @Override
  public void setInvStack(int slot, ItemStack stack) {
    // ItemStack hammer = this.inventory.getInvStack(0);
    this.inventory.setInvStack(slot, stack);
    /*
     * if (!ItemStack.areEqual(hammer, stack)) markDirty();
     */
  }

  @Override
  public void clear() {
    /*
     * if (!this.inventory.isInvEmpty()) markDirty();
     */
    this.inventory.clear();
  }

  public void setHammerStack(ItemStack stack) {
    setInvStack(0, stack);
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
  public BlockEntityUpdateS2CPacket toUpdatePacket() {
    return new BlockEntityUpdateS2CPacket(this.pos, 13, this.toInitialChunkDataTag());
  }

  protected void sync(CompoundTag hammerTag, PlayerEntity player) {
    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
    buf.writeBlockPos(getPos());
    buf.writeCompoundTag(hammerTag);
    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player,
        TooManyOresNetworking.TITANIUM_ANVIL_SYNC, buf);
  }

  @Override
  public void markDirty() {
    super.markDirty();
    if (!hasWorld() || getWorld().isClient())
      return;
    CompoundTag hammerTag = new CompoundTag();
    ItemStack hammerStack = this.inventory.getInvStack(0);
    if (!hammerStack.isEmpty())
      hammerStack.toTag(hammerTag);
    for (Iterator<PlayerEntity> iter = PlayerStream.watching(this).iterator(); iter.hasNext();)
      sync(hammerTag, iter.next());
  }

  @Override
  public CompoundTag toInitialChunkDataTag() {
    return toTag(new CompoundTag());
  }
}
