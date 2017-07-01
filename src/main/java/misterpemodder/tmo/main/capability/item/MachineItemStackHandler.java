package misterpemodder.tmo.main.capability.item;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.main.capability.IMachineElementHandler;
import misterpemodder.tmo.main.capability.io.IOConfigHandlerMachine;
import misterpemodder.tmo.main.tileentity.TileEntityMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;

public class MachineItemStackHandler extends SyncedItemHandler implements IMachineElementHandler<IItemHandler> {
	
	private final IOConfigHandlerMachine configHandler;
	private final boolean canInsert;
	private final boolean canExtract;
	
	public MachineItemStackHandler(TileEntityMachine<?> te, int size) {
		this(te, size, true, true);
	}

	public MachineItemStackHandler(TileEntityMachine<?> te, int size, boolean canInsert, boolean canExtract) {
		super(te, size);
		this.configHandler = te.getIoConfigHandler();
		this.canExtract = canExtract;
		this.canInsert = canInsert;
	}
	
	public IItemHandler getSideHandler(EnumFacing side) {
		return new SideHandler(side);
	}
	
	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return super.insertItem(slot, stack, simulate);
	}
	
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return super.extractItem(slot, amount, simulate);
	}
	
	public final class SideHandler implements IItemHandler {
		
		private final EnumFacing side;

		private SideHandler(EnumFacing side) {
			this.side = side;
		}
		
		private boolean isInput() {
			return MachineItemStackHandler.this.configHandler.isSideInput(side, TooManyOresAPI.itemIoType);
		}
		
		private boolean isOutput() {
			return MachineItemStackHandler.this.configHandler.isSideOutput(side, TooManyOresAPI.itemIoType);
		}

		@Override
		public int getSlots() {
			if(isInput() || isOutput()) {
				return MachineItemStackHandler.this.getSlots();
			}
			return 0;
		}

		@Override
		public ItemStack getStackInSlot(int slot) {
			if(isInput() || isOutput()) {
				return MachineItemStackHandler.this.getStackInSlot(slot);
			}
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
			if(isInput() && MachineItemStackHandler.this.canInsert) {
				return MachineItemStackHandler.this.insertItem(slot, stack, simulate);
			}
			return stack;
		}

		@Override
		public ItemStack extractItem(int slot, int amount, boolean simulate) {
			if(isOutput() && MachineItemStackHandler.this.canExtract) {
				return MachineItemStackHandler.this.extractItem(slot, amount, simulate);
			}
			return ItemStack.EMPTY;
		}

		@Override
		public int getSlotLimit(int slot) {
			return MachineItemStackHandler.this.getSlotLimit(slot);
		}
		
	}

}
