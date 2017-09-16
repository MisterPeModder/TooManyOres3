package misterpemodder.tmo.main.capability;

import misterpemodder.hc.api.capability.owner.IOwnerHandler;
import misterpemodder.tmo.main.TooManyOres;
import misterpemodder.tmo.main.network.PacketDataHandlers;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class CapabilityFreezing {
	
	@CapabilityInject(IOwnerHandler.class)
	 public static Capability<IFreezing> FREEZING_CAPABILITY = null;
	 
	 public static void register() {
		 CapabilityManager.INSTANCE.register(IFreezing.class, new Capability.IStorage<IFreezing>() {

			@Override
			public NBTBase writeNBT(Capability<IFreezing> capability, IFreezing instance, EnumFacing side) {
				return null;
			}
			@Override
			public void readNBT(Capability<IFreezing> capability, IFreezing instance, EnumFacing side, NBTBase nbt) {}
		}, Freezing.class);
	 }
	 
	 public interface IFreezing extends INBTSerializable<NBTTagCompound> {
		boolean isFreezing();
		void updateEntity(EntityLivingBase entity);
		void update(int duration, EntityLivingBase entity);
		void initPotion(EntityLiving entity);
	 }

	 
	 public static class Freezing implements IFreezing {
		 
		private boolean freezing = false;
		private boolean wasFreezing = false;
		private boolean wasSilent = false;
		
		@Override
		public boolean isFreezing() {
			return this.freezing;
		}
		
		@Override
		public void updateEntity(EntityLivingBase entity) {
			if(entity instanceof EntityLiving) {
				((EntityLiving)entity).setSilent(this.freezing? true : wasSilent);
			}
		}
		
		@Override
		public void initPotion(EntityLiving entity) {
			this.wasSilent = entity.isSilent();
		}
		
		@Override
		public void update(int duration, EntityLivingBase entity) {
			this.wasFreezing = freezing;
			this.freezing = duration>0;
			
			if(freezing || wasFreezing) {
				if(!wasFreezing && entity instanceof EntityLiving) {
					initPotion((EntityLiving)entity);
				}
				updateEntity(entity);
			}
			if(freezing != wasFreezing) {
				if(entity.getEntityWorld() != null && !entity.getEntityWorld().isRemote) {
					NBTTagCompound toSend = new NBTTagCompound();
					NetworkRegistry.TargetPoint target = new TargetPoint(entity.getEntityWorld().provider.getDimension(), entity.posX, entity.posY, entity.posZ, 128);
					toSend.setTag("entity_uuid", NBTUtil.createUUIDTag(entity.getUniqueID()));
					toSend.setTag("freezing_cap", this.serializeNBT());
					TooManyOres.PACKET_HANDLER.sendToAllAround(PacketDataHandlers.FREEZING_CAPABILITY_UPDATE_HANDLER, toSend, target);
				}
			}
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound compound = new NBTTagCompound();
			compound.setBoolean("freezing", this.freezing);
			compound.setBoolean("freezingOld", this.wasFreezing);
			compound.setBoolean("wasSilent", this.wasSilent);
			return compound;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			this.freezing = nbt.getBoolean("freezing");
			this.wasFreezing = nbt.getBoolean("freezingOld");
			this.wasSilent = nbt.getBoolean("wasSilent");
		}
		 
	 }
	
}
