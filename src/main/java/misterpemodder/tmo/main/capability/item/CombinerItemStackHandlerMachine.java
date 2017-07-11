package misterpemodder.tmo.main.capability.item;

import com.google.common.collect.ImmutableMap;

import misterpemodder.tmo.main.capability.IMachineElementHandler;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CombinerItemStackHandlerMachine extends CombinedItemStackHandler implements IMachineElementHandler<CombinedItemStackHandler> {

	private ImmutableMap<EnumFacing, CombinedItemStackHandler> combinedHandlers;
	
	public CombinerItemStackHandlerMachine(MachineItemStackHandler first, MachineItemStackHandler... otherHandlers) {
		super(first, otherHandlers);
		
		ImmutableMap.Builder<EnumFacing, CombinedItemStackHandler> builder = new ImmutableMap.Builder<>();
		for(EnumFacing side : EnumFacing.values()) {
			IItemHandlerModifiable fh = first.getSideHandler(side);
			if(otherHandlers != null && otherHandlers.length > 0) {
				int l = otherHandlers.length;
				IItemHandlerModifiable[] sideHandlers = new IItemHandlerModifiable[l];
				for(int i=0; i<l; i++) {
					sideHandlers[i] = otherHandlers[i].getSideHandler(side);
				}
				builder.put(side, new CombinedItemStackHandler(fh, sideHandlers));
			} else {
				builder.put(side, new CombinedItemStackHandler(fh));
			}
		}
		this.combinedHandlers = builder.build();
	}

	@Override
	public CombinedItemStackHandler getSideHandler(EnumFacing side) {
		return combinedHandlers.get(side);
	}
	
}
