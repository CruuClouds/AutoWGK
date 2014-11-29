package de.fly4lol.autowgk.listener;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.pro_crafting.wg.event.ArenaStateChangeEvent;


public class ArenaStateChangeListener implements Listener{
	@SuppressWarnings("unused")
	private Main plugin;
	private MySQLMethods sql;
	
	public ArenaStateChangeListener(Main plugin) {
		this.plugin = plugin;
		this.sql = plugin.getSQL();
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void ArenaStateChangedHandler(ArenaStateChangeEvent event) {
		
		
		List<Location> signs = sql.getSignsByType("ArenaInfo");
		for(Location sign : signs){
			Material Type = sign.getBlock().getType();
			if(Type.equals( Material.SIGN_POST) ||Type.equals( Material.SIGN)){
				BlockState state = sign.getBlock().getState();
				Sign s = (Sign) state;
				String arenaName = s.getLine( 1).replaceAll("§9", "");
				if(event.getArena().getName().equalsIgnoreCase( arenaName )){
					s.setLine(2, "§2" + event.getTo().toString() );
					s.update();
				}
			} else {
				sign.getBlock().setType(Material.SIGN_POST);
				BlockState state = sign.getBlock().getState();
				Sign s = (Sign) state;
				s.setLine(1, "§4NUTZE");
				s.setLine(2, "§4AutoWGK Sign");
				s.setLine(3, "§4Remove");
				s.update();
			}
		}
	}
}