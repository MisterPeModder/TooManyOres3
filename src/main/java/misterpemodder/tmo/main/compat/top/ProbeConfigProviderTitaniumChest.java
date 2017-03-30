package misterpemodder.tmo.main.compat.top;

import com.google.common.base.Function;

import mcjty.theoneprobe.api.IProbeConfig;
import mcjty.theoneprobe.api.IProbeConfig.ConfigMode;
import mcjty.theoneprobe.api.IProbeConfigProvider;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeHitEntityData;
import mcjty.theoneprobe.api.ITheOneProbe;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import misterpemodder.tmo.main.config.ConfigValues;
import misterpemodder.tmo.main.tileentity.TileEntityTitaniumChest;
import misterpemodder.tmo.main.utils.TMORefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class ProbeConfigProviderTitaniumChest implements IProbeConfigProvider {
	
	private ConfigMode showChestContentsOld;
	private ConfigMode showChestContentsDetailedOld;
	private boolean configModified = false;
	
	public static void init() {
		if(TMORefs.topLoaded) {
			TMORefs.LOGGER.info("Found The One Probe: Sending request for the ITheOneProbe instance...");
			FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", GetTheOneProbe.class.getName());
		} else {
			TMORefs.LOGGER.info("The One Probe not found: integration not loading");
		}
	}
	
	public static void getTheOneProbe(ITheOneProbe theOneProbe) {
		theOneProbe.registerProbeConfigProvider(new ProbeConfigProviderTitaniumChest());
	}
	
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
    
    public static class GetTheOneProbe implements Function<ITheOneProbe, Void> {

		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			TMORefs.LOGGER.info("=> Registering: Titanium Chest probe config provider"); 
			theOneProbe.registerProbeConfigProvider(new ProbeConfigProviderTitaniumChest());
			return null;
		}
    	
    }

}
