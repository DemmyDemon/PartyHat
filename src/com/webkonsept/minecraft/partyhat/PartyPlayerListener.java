package com.webkonsept.minecraft.partyhat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PartyPlayerListener extends PlayerListener {
	PartyHat plugin;
	
	PartyPlayerListener(PartyHat instance){
		plugin = instance;
	}
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if (event.getPlayer().isSneaking()){
			Item item = event.getItemDrop();
			Vector itemVector = item.getVelocity();
			itemVector.setX(0 - itemVector.getX());
			itemVector.setY(0 - itemVector.getY());
			itemVector.setZ(0 - itemVector.getZ());
			item.setVelocity(itemVector);
		}
	}
	public void onPlayerInteract(PlayerInteractEvent event){
		Block block = event.getClickedBlock();
		if (block != null && plugin.isEnabled() && !event.isCancelled()){
			if (block.getState() instanceof Sign){
				Sign sign = (Sign) block.getState();
				if (sign.getLine(1).equalsIgnoreCase("[partyhat]")){
					Player player = event.getPlayer();
					//if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)){
						event.setCancelled(true);
						if (plugin.permit(player,"partyhat.use")){
							if (event.isBlockInHand()){
								
								ItemStack inHand = player.getItemInHand();
								ItemStack helmet = new ItemStack(inHand.getType(),1);
								helmet.setDurability(inHand.getDurability());
								helmet.setData(inHand.getData());
								
								if (player.getInventory().getHelmet().getType().equals(Material.AIR)){
									player.getInventory().setHelmet(helmet);
									player.sendMessage(ChatColor.GOLD+"You are now wearing a "+helmet.getType().toString().toLowerCase()+" block on your head!");
									if (inHand.getAmount() > 1){
										inHand.setAmount(inHand.getAmount() - 1);
									}
									else {
										player.setItemInHand(null);
									}
								}
								else {
									player.sendMessage(ChatColor.GOLD+"Urr, sorry, you have to take off your helmet first.");
								}
							}
						}
						else {
							player.sendMessage(ChatColor.GOLD+"Sorry, you were not invited to the party :(");
						}
					//}
					//else {
						//player.sendMessage(ChatColor.GOLD+"LEFT click it!");
					//}
				}
			}
		}
	}

}
