package net.minesofcode.shopstalls.cmds;

import org.bukkit.entity.Player;

import net.minesofcode.shopstalls.MessageManager;
import net.minesofcode.shopstalls.ShopStalls;

public class Reload extends SubCommand {
	
	public void onCommand(Player p, String[] args) {
		ShopStalls.getPlugin(ShopStalls.class).reloadConfig();
		MessageManager msg = MessageManager.getInstance();
		msg.log("[ShopStalls] Configuration data reloaded.");
		msg.good(p, "Reloaded!");
	}
	
	public String name() {
		return "reload";
	}
	
	public String info() {
		return "Reload the plugin.";
	}
	
	public String[] aliases() {
		return new String[] { "rload" };
	}
}