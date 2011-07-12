package com.webkonsept.minecraft.partyhat;

import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class PartyBlockListener extends BlockListener {
	PartyHat plugin;
	
	PartyBlockListener(PartyHat instance){
		plugin = instance;
	}
	public void onSignChange(SignChangeEvent event){
		if (!plugin.isEnabled()) return;
		if (event.isCancelled()) return;
		if (event.getLine(1).equalsIgnoreCase("[partyhat]")){
			if (! plugin.permit(event.getPlayer(),"partyhat.sign")){
				event.setLine(1,"DENIED!");
			}
		}
	}
}
