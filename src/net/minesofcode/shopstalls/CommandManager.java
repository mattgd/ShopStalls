package net.minesofcode.shopstalls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.minesofcode.shopstalls.cmds.Create;
import net.minesofcode.shopstalls.cmds.Reload;
import net.minesofcode.shopstalls.cmds.SubCommand;

public class CommandManager implements CommandExecutor {
	
	public static ArrayList<SubCommand> commands = new ArrayList<SubCommand>();
	public static List<String> commandStrings = new ArrayList<String>();
	
	public void setup() {
		commands.add(new Create());
		commands.add(new Reload());
	}
	
	// Gets a list of all of the command Strings from the CommandManager
	public static void populateCommandList() {
		for (SubCommand subCmd : commands) {
			commandStrings.add(subCmd.name());
		}
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		if (cmd.getName().equalsIgnoreCase("shopstalls") || cmd.getName().equalsIgnoreCase("shopstall")) {
			
			if (sender.hasPermission("shopstalls.admin")) {
				
				if (args.length == 0) {
					String info = String.format("&6ShopStalls &2version &6%s &2by &6%s&2.", ShopStalls.getVersion(), ShopStalls.getAuthor());
					MessageManager.getInstance().info(sender, info);
					return true;
				}
				
				if (args.length >= 1) {
					SubCommand target = get(args[0]);
					
					if (sender.hasPermission("shopstalls." + target.name().toLowerCase())) {
						Player p = (Player) sender;
						
						ArrayList<String> a = new ArrayList<String>();
						a.addAll(Arrays.asList(args));
						a.remove(0);
						args = a.toArray(new String[a.size()]);
						
						try {
							target.onCommand(p, args);
						} catch (Exception e) {
							MessageManager.getInstance().severe(p, "An error has occured: " + e.getCause());
							e.printStackTrace();
						}
					} else {
						MessageManager.getInstance().severe(sender, "You do not have permission to use that command.");
					}
				}
				
			} else {
				MessageManager.getInstance().severe(sender, "You do not have permission to use this command.");
			}
			return true;
		}
		
		Player p = (Player) sender;
		
		String cmdName = cmd.getName().toLowerCase();
		SubCommand target = get(cmd.getName().toLowerCase());
		
		if (target == null) {
			MessageManager.getInstance().severe(p, "/" + cmdName + " is not a valid command.");
			return true;
		}
		
		try {
			target.onCommand(p, args);
		} catch (Exception e) {
			MessageManager.getInstance().severe(p, "An error has occured: " + e.getCause());
			e.printStackTrace();
		}
		
		return true;
	}
	
	private SubCommand get(String name) {
		for (SubCommand cmd : commands) {
			if (cmd.name().equalsIgnoreCase(name)) return cmd;
			for (String alias : cmd.aliases()) if (name.equalsIgnoreCase(alias)) return cmd;
		}
		return null;
	}
}