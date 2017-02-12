package misterpemodder.tmo.main.commands;

import java.util.Collections;
import java.util.List;

import misterpemodder.tmo.main.Tmo;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

public class SubCommandHelp extends SubCommandBase {
	
	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		super.execute(server, sender, args);
		
		if(args.length == 0) {
			List<SubCommandBase> list = this.getSortedPossibleCommands(sender, server);
        	int i = 7;
        	int j = (list.size() - 1) / 7;
        	int k = 0;

        	try {
        		k = args.length == 0 ? 0 : parseInt(args[0], 1, j + 1) - 1;
        	}
        	catch (NumberInvalidException numberinvalidexception) {
        		throw new WrongUsageException(this.getUsage(sender));
        	}

        	int l = Math.min((k + 1) * 7, list.size());
        	sender.sendMessage(new TextComponentString(TextFormatting.DARK_PURPLE+""+Tmo.proxy.translate("commands.tmo.help.header", new Object[] {Integer.valueOf(k + 1), Integer.valueOf(j + 1)})));

        	for (int i1 = k * 7; i1 < l; ++i1) {
            	TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(list.get(i1).getUsage(sender));
            	Style style = textcomponenttranslation.getStyle();
            	
            	style.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tmo " + list.get(i1).getName() + " "));
            	style.setColor(TextFormatting.DARK_AQUA);
            	style.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(TextFormatting.GRAY+""+TextFormatting.ITALIC+Tmo.proxy.translate("commands.tmo.help.hover"))));
            	
            	sender.sendMessage(textcomponenttranslation);
        	}

        	if (k == 0) {
            	sender.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE+""+Tmo.proxy.translate("commands.tmo.help.hint")));
        	}
		} else {
			if(getSubCommand(args) != null) {
				throw new WrongUsageException(getSubCommand(args).getUsage(sender));
			} else {
				throw new WrongUsageException(this.getUsage(sender));
			}
		}
	}
	
	protected List<SubCommandBase> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
        List<SubCommandBase> list = SubCommandBase.subCommands;
        Collections.sort(list);
        return list;
    }
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
		if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, SubCommandBase.subCommandsNames);
        }
	    return Collections.<String>emptyList();
	}
}
