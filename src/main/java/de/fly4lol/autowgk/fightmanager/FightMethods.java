package de.fly4lol.autowgk.fightmanager;


import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;

public class FightMethods {
	
	public Main plugin;
	public Util util;
	
	public FightMethods(Main plugin) {
		this.plugin = plugin;
		plugin.getUtil();
	}
	
	public void joinArena(Player player, Location location){
		AutoArena autoArena = util.getArenaAt( location );
		if( autoArena != null){
			
			if(autoArena.getMode() != AutoArenaMode.DISABLED){
				
				
			} else {
				player.sendMessage(plugin.prefix + "Diese Arena ist immoment nicht für AutoWGK verfügbar!");
			}
			
		} else {
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena!");
		}
	}
	
	

}
