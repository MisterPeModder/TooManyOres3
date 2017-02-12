package misterpemodder.tmo.main.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.Sets;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandTMO extends CommandBase {
	
	@Override
	public String getName() {
		return "tmo";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.tmo.generic.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length <= 0) {
            throw new WrongUsageException("commands.tmo.generic.usage");
        }
		SubCommandBase cmd = SubCommandBase.getSubCommand(args);
		if(cmd == null) {
			throw new WrongUsageException("commands.tmo.generic.wrongSubcommand", args[0]);
		} else {
			cmd.execute(server, sender, Arrays.copyOfRange(args, 1, args.length));
		}
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		
		if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SubCommandBase.subCommandsNames);
        }
		else if(SubCommandBase.getSubCommand(args) != null) {
			SubCommandBase cmd = SubCommandBase.getSubCommand(args);
			if(cmd != null) {
				return cmd.getTabCompletions(server, sender, Arrays.copyOfRange(args, 1, args.length), targetPos);
			}
		}
		return Collections.<String>emptyList();
	}

}
