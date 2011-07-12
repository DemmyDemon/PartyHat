package com.webkonsept.minecraft.partyhat;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class PartyHat extends JavaPlugin {
	private Logger log = Logger.getLogger("Minecraft");
	protected PermissionHandler Permissions;
	private boolean usePermissions = false;
	protected HashMap<String,Boolean> fallbackPermissions = new HashMap<String,Boolean>();
	protected PartyPlayerListener playerListener = new PartyPlayerListener(this);
	protected PartyBlockListener blockListener = new PartyBlockListener(this);
	
	@Override
	public void onDisable() {
		fallbackPermissions.clear();
		this.out("Disabled");
	}

	@Override
	public void onEnable() {
		usePermissions = setupPermissions();
		if(!usePermissions){
			fallbackPermissions.put("partyhat.use",true);
		}
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_INTERACT,playerListener,Priority.Normal,this);
		pm.registerEvent(Event.Type.SIGN_CHANGE,blockListener,Priority.Normal,this);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM,playerListener,Priority.High,this);
		this.out("Enabled");
	}
	public boolean permit(Player player,String permission){ 
		boolean allow = false; // Default to GTFO
		
		if ( usePermissions ){
			if (Permissions.has(player,permission)){
				allow = true;
			}
		}
		else if (player.isOp() || fallbackPermissions.get(permission)){
			allow = true;
		}
		
		return allow;
	}
	private boolean setupPermissions() {
		Plugin test = this.getServer().getPluginManager().getPlugin("Permissions");
		if (this.Permissions == null){
			if (test != null){
				this.Permissions = ((Permissions)test).getHandler();
				return true;
			}
			else {
				this.out("Permissions plugin not found, defaulting to OPS CHECK mode");
				return false;
			}
		}
		else {
			this.crap("Urr, this is odd...  Permissions are already set up!");
			return true;
		}
	}
	public void out(String message) {
		PluginDescriptionFile pdfFile = this.getDescription();
		log.info("[" + pdfFile.getName()+ " " + pdfFile.getVersion() + "] " + message);
	}
	public void crap(String message){
		PluginDescriptionFile pdfFile = this.getDescription();
		log.severe("[" + pdfFile.getName()+ " " + pdfFile.getVersion() + " CRAP] " + message);
	}
}
