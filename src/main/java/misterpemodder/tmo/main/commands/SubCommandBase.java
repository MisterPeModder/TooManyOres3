package misterpemodder.tmo.main.commands;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public abstract class SubCommandBase extends CommandBase {
	
    public static List<SubCommandBase> subCommands = new ArrayList<>();
    public static List<String> subCommandsNames = new ArrayList<>();
	
	static {
		subCommands.add(new SubCommandHelp());
		subCommands.add(new SubCommandOwner());
		for(SubCommandBase cmd : subCommands) {
			subCommandsNames.add(cmd.getName());
		}
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return String.format("commands.tmo.%s.usage", getName());
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		checkSize(args, this.getUsage(sender));
	}
	
	protected void checkSize(Object[] args, String message, Object... replacements) throws WrongUsageException {
		this.checkSize(args, 0, message, replacements);
	}
	
	protected void checkSize(Object[] args, int minSize, String message, Object... replacements) throws WrongUsageException {
		if (args.length < minSize) {
            throw new WrongUsageException(message, replacements);
        }
	}
	
	protected static SubCommandBase getSubCommand(String[] args) {
		if (args.length > 0) {
			for(SubCommandBase cmd : subCommands) {
				if(cmd.getName().equals(args[0])) {
					return cmd;
				}
			}
		}
		return null;
	}
	
}
