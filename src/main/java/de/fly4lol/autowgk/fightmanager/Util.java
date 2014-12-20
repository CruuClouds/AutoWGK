package de.fly4lol.autowgk.fightmanager;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

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
	
	public Team getTeamByPlayer(Player player){
		List<AutoArena> autoArenen = plugin.getAutoWGKConfig().getAutoArenen();
		for(AutoArena arena : autoArenen){
			Team team1 = plugin.loadedArenen.get(arena).getTeam1();
			Team team2 = plugin.loadedArenen.get(arena).getTeam2();
			if(team1.getLeader().equals( player)){
				return team1;
			}
			if(team2.getLeader().equals( player)){
				return team2;
			}
		}
		return null;
	}
	
	public AutoArena getArenaOfTeam(Team team){
		
		return null;
	}

}
