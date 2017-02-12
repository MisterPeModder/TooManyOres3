package misterpemodder.tmo.main.utils;

import java.util.Map;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.server.FMLServerHandler;

public final class ServerUtils {
	
	public static MinecraftServer getServer() {
		return FMLServerHandler.instance().getServer();
	}
	
	public static boolean isOp(EntityPlayer player) {
		
		EntityPlayerMP sender = (EntityPlayerMP) player;
		MinecraftServer minecraftServer = sender.mcServer;
		
		return minecraftServer.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null;
		
		/*ICommandManager commandManager = minecraftServer.getCommandManager();
		Map<String, ICommand> commands = commandManager.getCommands();
		ICommand opCmd = commands.get("op");
		if (opCmd != null && opCmd.checkPermission(minecraftServer, sender)) {
			return true;
		} else {
			return sender.canUseCommand(minecraftServer.getOpPermissionLevel(), "op");
		}*/
	}
	
}
