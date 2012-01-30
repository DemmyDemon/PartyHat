package com.webkonsept.minecraft.partyhat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class PartyBlockListener implements Listener {
	PartyHat plugin;
	
	PartyBlockListener(PartyHat instance){
		plugin = instance;
	}
	@EventHandler
	public void onSignChange(final SignChangeEvent event){
		if (!plugin.isEnabled()) return;
		if (event.isCancelled()) return;
		if (event.getLine(plugin.signLine).equalsIgnoreCase(plugin.signString)){
			if (! plugin.permit(event.getPlayer(),"partyhat.sign")){
				event.setLine(1,"DENIED!");
			}
		}
	}
}
