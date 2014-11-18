package de.fly4lol.autowgk.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.MySQLMethods;


public class PlayerInteractListener implements Listener{
	private Main plugin;
	private MySQLMethods sql;
	
	public PlayerInteractListener(Main plugin, MySQLMethods sql) {
		this.plugin = plugin;
		this.sql = sql;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerInteractHandler(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Material type = event.getClickedBlock().getType();
			if(type.equals(Material.SIGN ) || type.equals(Material.SIGN_POST)){
				Location signLoc = event.getClickedBlock().getLocation();
				if(plugin.addSign.contains(player)){
					if(!sql.existSignAtLocation(signLoc)){
						BlockState state = signLoc.getBlock().getState();
						Sign sign = (Sign) state;
						if(sign.getLine( 0 ).equalsIgnoreCase("[ArenaInfo]")){
							sign.setLine( 0 , "§6Arena Info§6");
							sign.setLine( 1, "&9" + sign.getLine( 1) + "§9");
							sign.setLine( 2, "§2 Loading... §2");
							sign.update();
							sql.addSign(player, signLoc, "ArenaInfo");
							plugin.addSign.remove(player);
							player.sendMessage(plugin.prefix + "Du hast das Schild hinzugefügt!");
						} else {
							player.sendMessage( plugin.prefix + "Dies ist kein gültiges Schild!");
							plugin.addSign.remove(player);
						}
					} else {
						player.sendMessage(plugin.prefix + "Dieses Schilt ist bereits Eingetragen!");
						plugin.addSign.remove(player);
					}
				} else if(plugin.removeSign.contains(player)){
					if(sql.existSignAtLocation(signLoc)){
						sql.removeSign(signLoc);
						signLoc.getBlock().setType(Material.AIR);
						plugin.removeSign.remove(player);
						player.sendMessage(plugin.prefix + "Du hast das Schilt Entfernt!");
					} else {
						player.sendMessage(plugin.prefix + "Dieses Schilt ist nicht Eingetragen!");
						plugin.removeSign.remove(player);
					}
				}
			}
		}
	}
}