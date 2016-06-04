package net.minesofcode.shopstalls;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class ShopStalls extends JavaPlugin {
	
	PluginDescriptionFile pdFile = this.getDescription();
	public final Logger log = Logger.getLogger("Minecraft");
	
	public void logMessage(String msg) {
		this.log.info(pdFile.getName() + " " + pdFile.getVersion() + ": " + msg);
	}
	
	public static ShopStalls plugin;
	private static String name;
	private static String version;
	private static String author;
	
	public static Economy econ = null;
    public static Permission perms = null;
    public static Chat chat = null;
	
	@Override
	public void onEnable() {
		// Vault Setup
		if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        setupChat();
        
        // Configuration
		plugin = this;
		
		//getConfig().options().copyDefaults(true);
		//saveConfig();
		
		// Commands
		CommandManager cm = new CommandManager();
		cm.setup();
		
		CommandManager.populateCommandList(); // Adds all of the command names to a list so they can be registered
		
		getCommand("shopstalls").setExecutor(cm); // Setup Events command
		
		for (String cmdName : CommandManager.commandStrings) {
			getCommand(cmdName).setExecutor(cm);
		}
		
	}
	
	@Override
	public void onDisable() {
		this.getConfig().options().copyDefaults(true);
		this.logMessage("Disabled!");
	}
	
	/**
	* Vault Integration
	*/
	private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public static String getPluginName() {
    	return name;
    }
    
    public static String getVersion() {
    	return version;
    }
    
    public static String getAuthor() {
    	return author;
    }
    
    public static WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
     
        // WorldGuard may not be loaded
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null; // Maybe you want throw an exception instead
        }
     
        return (WorldGuardPlugin) plugin;
    }
    
    /**
	* Checks to see if a String is an Integer value.
	*
	* @param s
	* 			String to check.
	* @return true if the String s is an Integer and false otherwise.
	*/
    public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
    
    /**
	* Checks to see if a String is an Double value.
	*
	* @param s
	* 			String to check.
	* @return true if the String s is an Double and false otherwise.
	*/
    public static boolean isDouble(String s) {
	    try { 
	        Double.parseDouble(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }
	    return true;
	}
	
}
