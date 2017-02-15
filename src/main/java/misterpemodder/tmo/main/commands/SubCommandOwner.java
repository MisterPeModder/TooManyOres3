package misterpemodder.tmo.main.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import com.mojang.authlib.GameProfile;

import misterpemodder.tmo.api.owner.IOwnerHandler;
import misterpemodder.tmo.main.capability.CapabilityOwner;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import misterpemodder.tmo.main.capability.OwnerHandlerUUID;
import misterpemodder.tmo.main.utils.TMORefs;

public class SubCommandOwner extends SubCommandBase{

	@Override
	public String getName() {
		return "owner";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		checkSize(args, 4, getUsage(sender));
		World world = sender.getEntityWorld();
		BlockPos pos = parseBlockPos(sender, args, 1, false);
		
		if(!world.isBlockLoaded(pos)) {
			throw new CommandException("commands.tmo.owner.outOfWorld");
		}
		TileEntity te = world.getTileEntity(pos);
		IOwnerHandler ownerHandler = null;
		if(te == null || (ownerHandler = te.getCapability(CapabilityOwner.OWNER_HANDLER_CAPABILITY, null)) == null) {
			throw new CommandException("commands.tmo.owner.invalid");
		}
		
		sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 0);
		
		switch(args[0]) {
			case "get":
				if(!ownerHandler.hasOwner()) {
					notifyCommandListener(sender, this, "commands.tmo.owner.get.noOwner", new Object[] {pos.getX(), pos.getY(), pos.getZ()});
				} else {
					sender.sendMessage(new TextComponentTranslation("commands.tmo.owner.get.onwerName", new Object[] {pos.getX(), pos.getY(), pos.getZ(), ownerHandler.getOwner(world).getDisplayName()}));
				}
				break;
				
			case "clear":
				if(!ownerHandler.hasOwner()) {
					notifyCommandListener(sender, this, "commands.tmo.owner.get.noOwner", new Object[] {pos.getX(), pos.getY(), pos.getZ()});
				} else {
					ownerHandler.clearOwner();
					notifyCommandListener(sender, this, "commands.tmo.owner.clear.success");
				}
			break;
		
			case "set":
				checkSize(args, 5, "commands.tmo.owner.set.usage");
				Entity entity = getEntity(server, sender, args[4]);
				if(!(entity instanceof EntityPlayer))
					throw new CommandException("commands.tmo.owner.set.notPlayer", entity);
				ownerHandler.setOwner((EntityPlayer)entity);
				sender.sendMessage(new TextComponentTranslation("commands.tmo.owner.force.success", new Object[] {entity.getDisplayName()}));
				break;
		
			case "force":
				checkSize(args, 6, "commands.tmo.owner.force.usage");
				UUID id = null;
				try {
					if(args.length == 6) {
						id = UUID.fromString(args[5]);
					} else if(args.length >= 7) {
						long leastSigBits = Long.valueOf(args[5].replaceFirst("L", ""));
						long mostSigBits = Long.valueOf(args[6].replaceFirst("L", ""));
						id = new UUID(mostSigBits, leastSigBits);
					}
				} catch(IllegalArgumentException e) {
					throw new CommandException("commands.generic.entity.invalidUuid", id);
				}
				FakePlayer player = new FakePlayer(server.worldServerForDimension(sender.getEntityWorld().provider.getDimension()), new GameProfile(id, args[4]));
				ownerHandler.setOwner(player);
				sender.sendMessage(new TextComponentTranslation("commands.tmo.owner.force.success", new Object[] {player.getDisplayName()}));
				break;
		
			default:
				throw new WrongUsageException(getUsage(sender));
		}
		sender.setCommandStat(CommandResultStats.Type.AFFECTED_BLOCKS, 1);
	}
	
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, new String[] {"get", "clear", "set", "force"}) : (args.length > 1 && args.length <= 4? getTabCompletionCoordinate(args, 1, targetPos) : (args[0] != "get"? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.<String>emptyList()));
    }

}
