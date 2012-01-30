package com.webkonsept.minecraft.partyhat;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PartyHat extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");
	protected PartyPlayerListener playerListener = new PartyPlayerListener(this);
	protected PartyBlockListener blockListener = new PartyBlockListener(this);
	
	// Settings
	protected boolean useWhitelistPermissions = false;
	protected String signString = "[PartyHat]";
	protected boolean verbose = false;
	protected int signLine = 1;
	
	@Override
	public void onDisable() {
		this.out("Disabled");
	}

	@Override
	public void onEnable() {
		configure();
		PluginManager pm = getServer().getPluginManager();
	    pm.registerEvents(blockListener,this);
	    pm.registerEvents(playerListener,this);
	    
	    /* OLD!
		pm.registerEvent(Event.Type.PLAYER_INTERACT,playerListener,Priority.Normal,this);
		pm.registerEvent(Event.Type.SIGN_CHANGE,blockListener,Priority.Normal,this);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM,playerListener,Priority.High,this);
		*/
	    
		this.out("Enabled");
		this.babble("Verbose mode ON");
	}
	public boolean permit(Player player,String[] permissions){ 
		
		if (player == null) return false;
		if (permissions == null) return false;
		String playerName = player.getName();
		boolean permit = false;
		for (String permission : permissions){
		    permit = player.hasPermission(permission);
			if (permit){
				babble("Permission granted: "+playerName+"->"+permission);
				break;
			}
			else {
				babble("Permission denied: "+playerName+"->"+permission);
			}
		}
		return permit;
		
	}
	public boolean permit(Player player,String permission){
		return permit(player,new String[]{permission});
	}
	public void out(String message) {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName()+ " " + pdfFile.getVersion() + "] " + message);
	}
	public void babble(String message) {
		if (verbose){
			out("[verbose] "+message);
		}
	}
	public void crap(String message){
		PluginDescriptionFile pdfFile = this.getDescription();
		log.severe("[" + pdfFile.getName()+ " " + pdfFile.getVersion() + " CRAP] " + message);
	}
	public void configure() {
	    getConfig().options().copyDefaults(true);
        {
            File oldConfig = new File(getDataFolder(),"settings.yml");
            if (oldConfig.exists()){
                this.out("Old configuration file found.  Replacing with a new one, but attempting to keep your settings.");
                try {
                    getConfig().load(oldConfig);
                } catch (IOException e) {
                    e.printStackTrace();
                    crap("Serious error trying to load the old configuration!  I'll make up a new one.");
                } catch (InvalidConfigurationException e) {
                    e.printStackTrace();
                    crap("Old configuration was invalid.  Ignoring it.");
                }
                oldConfig.renameTo(new File(getDataFolder(),"old_settings.yml__you_can_delete_me"));
            }
        }
		
		// Loading
		verbose = getConfig().getBoolean("verbose",verbose);
		babble("Verbose mode on!");
		signString = getConfig().getString("signString",signString);
		signLine = getConfig().getInt("signLine",signLine);
		if (signLine < 0 || signLine > 3){
			signLine = 1;
		}
		babble("Triggering on "+signString+" on line"+signLine);
		useWhitelistPermissions = getConfig().getBoolean("useWhitelistPermissions",useWhitelistPermissions);
		if (useWhitelistPermissions){
			babble("Whitelist-by-permissions is ON!");
		}
		else {
			babble("Whitelist-by-permissions is OFF!");
		}
		saveConfig();
	}
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		
		if ( ! this.isEnabled() ) return false;
		boolean success = false;
		if (command.getName().equalsIgnoreCase("partyhat")){
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")){
				success = true;  // Not really a success, but a valid command at least.
				if (
						(sender instanceof Player && this.permit((Player)sender,"partyhat.command.reload"))
						|| !(sender instanceof Player)
						// That is, if it's a player with permission, or not a player at all (console?), then...
				){
					configure();
					sender.sendMessage(ChatColor.GREEN+"PartyHat configuration reloaded");
				}
				else {
					sender.sendMessage(ChatColor.GOLD+"Sorry, permission denied.");
				}
			}
		}		
		return success;
	}
}
