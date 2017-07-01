package misterpemodder.tmo.main.apiimpl;

import java.util.HashMap;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import misterpemodder.tmo.api.TooManyOresAPI;
import misterpemodder.tmo.api.block.ISlimeBlock;
import misterpemodder.tmo.api.capability.io.IIOType;
import misterpemodder.tmo.api.handler.ITMOMethodHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;

public class MethodHandler implements ITMOMethodHandler {
	
	private static HashMap<Capability<?>, IIOType<?>> ioTypeForCap = new HashMap<>();

	@Override
	public boolean isSlimeBlock(IBlockState state) {
		return this.isSlimeBlock(state.getBlock());
	}

	@Override
	public boolean isSlimeBlock(Block block) {
		return TooManyOresAPI.SLIME_BLOCKS.containsKey(block);
	}
	
	@Override
	@Nullable
	public ISlimeBlock getSlimeBlock(IBlockState state) {
		return this.getSlimeBlock(state.getBlock());
	}
	
	@Override
	@Nullable
	public ISlimeBlock getSlimeBlock(Block block) {
		if(block instanceof ISlimeBlock) {
			return (ISlimeBlock) block;
		} 
		else if(this.isSlimeBlock(block)) {
			return TooManyOresAPI.SLIME_BLOCKS.get(block);
		} else {
			return null;
		}
	}
	
	@Override
	public boolean isEnderMatterItem(ItemStack stack) {
		for(ItemStack s : TooManyOresAPI.ENDER_MATTER_ITEMS.keySet()) {
			if(ItemStack.areItemsEqual(s, stack)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public Pair<Integer, Integer> getEnderMatterValue(ItemStack stack) {
		for(ItemStack s : TooManyOresAPI.ENDER_MATTER_ITEMS.keySet()) {
			if(ItemStack.areItemsEqual(s, stack) && stack.getCount() >= s.getCount()) {
				return Pair.of(TooManyOresAPI.ENDER_MATTER_ITEMS.get(s), s.getCount());
			}
		}
		return Pair.of(0, 0);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> IIOType<T> getIOTypeForCapability(Capability<T> cap) {
		if(ioTypeForCap.containsKey(cap)) {
			return (IIOType<T>) ioTypeForCap.get(cap);
		}
		else {
			for(IIOType<?> type : TooManyOresAPI.IO_TYPES) {
				if(type.getCapabilityInstance().equals(cap)) {
					ioTypeForCap.put(cap, type);
					return (IIOType<T>) type;
				}
			}
			ioTypeForCap.put(cap, null);
		}
		return null;
	}

}
