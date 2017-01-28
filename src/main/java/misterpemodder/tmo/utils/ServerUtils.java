package misterpemodder.tmo.utils;

import java.util.Map;

import com.google.common.base.Throwables;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.server.FMLServerHandler;

public final class ServerUtils {
	
	public static MinecraftServer getServer() {
		return FMLServerHandler.instance().getServer();
	}
	
	public static boolean isOp(EntityPlayer player) {
		
		EntityPlayerMP sender = (EntityPlayerMP) player;
		MinecraftServer minecraftServer = sender.mcServer;
		ICommandManager commandManager = minecraftServer.getCommandManager();
		Map<String, ICommand> commands = commandManager.getCommands();
		ICommand opCmd = commands.get("op");
		if (opCmd != null && opCmd.checkPermission(minecraftServer, sender)) {
			return true;
		} else {
			return sender.canUseCommand(minecraftServer.getOpPermissionLevel(), "op");
		}
	}
	
}
