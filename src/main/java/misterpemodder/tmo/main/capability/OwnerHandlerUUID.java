package misterpemodder.tmo.main.capability;

import java.util.UUID;
import javax.annotation.Nullable;
import misterpemodder.tmo.api.owner.IOwnerHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

public class OwnerHandlerUUID implements IOwnerHandler, INBTSerializable<NBTTagCompound> {
	
	protected String playerName;
	protected UUID playerId;

	@Override
	public boolean hasOwner() {
		return this.playerName != null && !this.playerName.isEmpty() && this.playerId != null;
	}

	@Override
	public String getOwnerName() {
		return playerName;
	}
	
	public UUID getOwnerUUID() {
		return playerId;
	}
	@Override
	public EntityPlayer getOwner(World world) {
		EntityPlayer owner = world.getPlayerEntityByUUID(playerId);
		if(owner != null) updateName(owner);
		return owner;
	}

	@Override
	public void setOwner(@Nullable EntityPlayer player) {
		if(player != null) {
			playerId = EntityPlayer.getUUID(player.getGameProfile());
			playerName = player.getDisplayNameString();
		} else {
			playerId = null;
			playerName = "";
		}
	}
	
	protected void updateName(EntityPlayer player) {
		if(!playerName.equals(player.getDisplayNameString())) {
			playerName = player.getDisplayNameString();
		}
	}
	
	public boolean isOwner(EntityPlayer player) {
		EntityPlayer owner = this.getOwner(player.getEntityWorld());
		if(owner != null) return player.isEntityEqual(owner);
		return playerId.equals(EntityPlayer.getUUID(player.getGameProfile()));
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound compound = new NBTTagCompound();
		if(playerName != null) compound.setString("name", playerName);
		if(playerId != null) compound.setTag("UUID", NBTUtil.createUUIDTag(playerId));
		return compound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if(nbt.hasKey("name")) playerName = nbt.getString("name");
		if(nbt.hasKey("UUID")) playerId = NBTUtil.getUUIDFromTag(nbt.getCompoundTag("UUID"));
	}
	
	@Override
	public void clearOwner() {
		this.playerId = null;
		this.playerName = null;
	}

}
