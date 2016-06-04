package net.minesofcode.shopstalls.cmds;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import net.minesofcode.shopstalls.ShopStalls;

public class Remove extends SubCommand {
	
	public void onCommand(Player p, String[] args) {
		WorldGuardPlugin worldGuard = ShopStalls.getWorldGuard();
		Location loc = p.getLocation();
		Vector pt = new Vector(loc.getX(), loc.getY(), loc.getZ());
		RegionManager regionManager = worldGuard.getRegionManager(p.getWorld());
		ApplicableRegionSet set = regionManager.getApplicableRegions(pt);
		
		if (set.size() > 0) {
			boolean stall = false;
			
			for (ProtectedRegion region : set.getRegions()) {
				if (region.getId().startsWith("shopstall")) {
					stall = true;
					region.getMembers().removeAll();
					region.setFlag(DefaultFlag.GREET_MESSAGE, "&aYou could be the lucky owner of this shop stall.");
					info(p, "Shop stall at " + region.getId() + " removed.");
					return;
				}
			}
			
			if (!stall) severe(p, "Could not find a valid shop stall here."); 
		} else {
			severe(p, "Could not find a valid shop stall here.");
		}
	}
	
	public String name() {
		return "remove";
	}
	
	public String info() {
		return "Remove a shop stall.";
	}
	
	public String[] aliases() {
		return new String[] { "r" };
	}
	
}