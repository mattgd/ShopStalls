package net.minesofcode.shopstalls.cmds;

import org.bukkit.entity.Player;

import net.minesofcode.shopstalls.MessageManager;

public abstract class SubCommand {
	
	public abstract void onCommand(Player p, String[] args);
	public abstract String name();
	public abstract String info();
	public abstract String[] aliases();
	
	protected final String PERMISSION_PREFIX = "shopstalls.";
	protected final String NO_PERMISSION = "You do not have permission to use this command.";
	protected final String INVALID_SUBCMD = "Invalid subcommand.";
	protected final String MISSING_PLAYER = "You must specify a player name.";
	
	protected String invalidPlayer(String name) {
		return "Could not find player " + name + ".";
	}
	
	protected static void severe(Player p, String message) {
		MessageManager.getInstance().severe(p, message);
	}
	
	protected static void info(Player p, String message) {
		MessageManager.getInstance().info(p, message);
	}
	
	protected static void good(Player p, String message) {
		MessageManager.getInstance().good(p, message);
	}
	
}
