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
import de.fly4lol.autowgk.arena.AutoArena;
import de.fly4lol.autowgk.sign.SignType;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.event.ArenaStateChangeEvent;


public class ArenaStateChangeListener implements Listener{
	private Main plugin;
	
	public ArenaStateChangeListener(Main plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void ArenaStateChangedHandler(ArenaStateChangeEvent event) {
		List<Location> signs = plugin.getRepo().getSignsByType(SignType.ARENAINFO);
		for(Location sign : signs){
			Material type = sign.getBlock().getType();
			if(type == Material.SIGN_POST || type == Material.SIGN){
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
		
		if(event.getTo() == State.Idle){
			AutoArena arena = plugin.getArenenManager().getArena(event.getArena().getName());
			if(arena.getTeam1() != null){
				if(arena.getTeam1().isReady() ){
					if(arena.getTeam2() != null){
						if(arena.getTeam2().isReady()){
							arena.startGame();
						}
					}
				}
			}
		}
		
		if(event.getTo() == State.Spectate){
			AutoArena arena = plugin.getArenenManager().getArena(event.getArena().getName());
			arena.setTeam1(null);
			arena.setTeam2(null);
		}
	}
}