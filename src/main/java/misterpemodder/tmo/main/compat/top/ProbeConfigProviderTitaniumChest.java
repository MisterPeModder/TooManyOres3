package misterpemodder.tmo.main.compat.top;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfig.ConfigMode;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.hc.main.apiimpl.capability.owner.CapabilityOwner;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class ProbeConfigProviderTitaniumChest implements IProbeConfigProvider {
	
	private ConfigMode showChestContentsOld;
	private ConfigMode showChestContentsDetailedOld;
	private boolean configModified = false;
	
	public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, Entity entity, IProbeHitEntityData data) {}

    public void getProbeConfig(IProbeConfig config, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data) {
    	TileEntity te = world.getTileEntity(data.getPos());
    	
    	if(configModified) {
    		configModified = false;
    		config.showChestContents(showChestContentsOld);
    		config.showChestContentsDetailed(showChestContentsDetailedOld);
    	} else {
    		showChestContentsOld = config.getShowChestContents();
			showChestContentsDetailedOld = config.getShowChestContentsDetailed();
    	}
    	
    	if(te != null && te instanceof TileEntityTitaniumChest && ConfigValues.BoolValues.TOP_COMPAT_HIDE_CONTENT.currentValue && ConfigValues.BoolValues.TOP_COMPAT.currentValue) {
    		TileEntityTitaniumChest chest = (TileEntityTitaniumChest) te;
			IOwnerHandler ownerHandler = chest.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null);
			
			if(chest.isLocked() && ownerHandler!=null && ownerHandler.hasOwner() && ownerHandler.getOwner(world) != player) {
				if(!configModified) {
					configModified = true;
					config.showChestContents(ConfigMode.NOT);
					config.showChestContentsDetailed(ConfigMode.NOT);
					return;
				}
			}
    	}
    }
    


}
