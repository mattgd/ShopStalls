package net.minesofcode.shopstalls.cmds;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.minesofcode.shopstalls.ShopStalls;

public class Create extends SubCommand {
	
	public void onCommand(Player p, String[] args) {
		if (args.length < 3) {
			severe(p, "Command usage: /shopstalls create <player> <price> <shop name>");
		} else {
			@SuppressWarnings("deprecation")
			OfflinePlayer member = Bukkit.getOfflinePlayer(args[0]);
			
			if (!member.hasPlayedBefore()) {
				severe(p, args[0] + " has never played before on this server.");
				return;
			}
			
			if (!ShopStalls.isDouble(args[1])) {
				severe(p, args[1] + " is not a valid shop stall price.");
				return;
			}
			
			EconomyResponse response = ShopStalls.econ.withdrawPlayer(member, Double.parseDouble(args[1]));
			
			if (response.equals(ResponseType.FAILURE)) {
				severe(p, "The selected player does not have enough money for this shop stall.");
				return;
			}
			
			WorldGuardPlugin worldGuard = ShopStalls.getWorldGuard();
			Location loc = p.getLocation();
			Vector pt = new Vector(loc.getX(), loc.getY(), loc.getZ());
			RegionManager regionManager = worldGuard.getRegionManager(p.getWorld());
			ApplicableRegionSet set = regionManager.getApplicableRegions(pt);
			
			String shopName = "";
			for (int i = 2; i < args.length; i++) {
				shopName += args[i] + " ";
			}
			shopName = shopName.substring(0, shopName.length() - 1); // Remove trailing space
			
			for (ProtectedRegion region : set.getRegions()) {
				
				if (region.getId().startsWith("shopstall")) {
					region.getMembers().addPlayer(member.getName());
					region.setFlag(DefaultFlag.GREET_MESSAGE, "&aWelcome to &b" + shopName + "&a!");
					good(p, "Shop stall &b" + shopName + " &acreated at &e" + region.getId() + "&a.");
					return;
				}
			}
			
			
		}
	}
	
	public String name() {
		return "create";
	}
	
	public String info() {
		return "Create a shop stall.";
	}
	
	public String[] aliases() {
		return new String[] { "c" };
	}
	
}