package de.fly4lol.autowgk.fightmanager;

import org.bukkit.Location;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.Config;
import de.pro_crafting.wg.arena.Arena;

public class Util {
	private Main plugin;
	private Config config;
	
	public Util(Main plugin) {
		this.plugin = plugin;
		this.config = plugin.getAutoWGKConfig();
	}
	
	public AutoArena getArenaAt(Location location){
		Arena arena = plugin.wg.getArenaManager().getArenaAt( location);
		if(arena == null){
			return null;
		} else {
			String arenaName = arena.getName();
			return plugin.loadedArenen.get( arenaName);
		}
	}

}
