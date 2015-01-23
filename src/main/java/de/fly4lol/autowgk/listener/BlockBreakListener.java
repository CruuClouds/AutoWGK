package de.fly4lol.autowgk.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.MySQLMethods;


public class BlockBreakListener implements Listener{
	private Main plugin;
	private MySQLMethods sql;
	
	public BlockBreakListener(Main plugin) {
		this.plugin = plugin;
		this.sql = plugin.getSQL();
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void blockBrakeHandler(BlockBreakEvent event) {
		Location loc = event.getBlock().getLocation();
		Material Type = event.getBlock().getType();
		Player player = event.getPlayer();
		if(Type.equals( Material.SIGN ) || Type.equals( Material.SIGN_POST )){
			if(sql.existSignAtLocation(loc )){
				event.setCancelled( true );
				player.sendMessage(plugin.prefix + "Du darfst dieses Schild nicht zerstören!");
				if(player.hasPermission("autowgk.sign.remove")){
					player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK sign remove");
				}
			}
		}
	}
}