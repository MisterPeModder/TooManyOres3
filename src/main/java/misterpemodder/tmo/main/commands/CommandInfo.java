package misterpemodder.tmo.main.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.oredict.OreDictionary;

public class CommandInfo extends CommandBase {

	@Override
	public String getName() {
		return "item_info";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.info.usage";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		
		try {
			EntityPlayerMP player = CommandBase.getCommandSenderAsPlayer(sender);
			
			ItemStack stack  = player.getHeldItem(EnumHand.MAIN_HAND);
			if(stack != null) {
				Item item = stack.getItem();
				
				int[] ids = OreDictionary.getOreIDs(stack);
				String oreDict = "";
				
				for(int i : ids) {
					oreDict += OreDictionary.getOreName(i)+", ";
				}

				if(!oreDict.isEmpty()) {
					oreDict.substring(oreDict.length()-2, oreDict.length()-1);
				}
				
				notifyCommandListener(sender, this, "commands.info.success.start", new Object[] {});
				notifyCommandListener(sender, this, "commands.info.success.reg", new Object[] {item.getRegistryName()});
				notifyCommandListener(sender, this, "commands.info.success.unl", new Object[] {item.getUnlocalizedName()});
				notifyCommandListener(sender, this, "commands.info.success.meta", new Object[] {stack.getMetadata()});
				notifyCommandListener(sender, this, "commands.info.success.metaunl", new Object[] {item.getUnlocalizedName(stack)});
				notifyCommandListener(sender, this, "commands.info.success.ore", new Object[] {oreDict});
				notifyCommandListener(sender, this, "commands.info.success.end", new Object[] {});
			}

		} catch(PlayerNotFoundException e) {
			throw new WrongUsageException("commands.info.usage", new Object[0]);
		}
		
	}
	
}
