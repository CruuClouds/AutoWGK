package de.fly4lol.autowgk.arena;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.Team;
import de.pro_crafting.wg.arena.Arena;

public class AutoArenaManager {

	private Main plugin;
	private Map<String, AutoArena> arenas;
	
	public AutoArenaManager(Main plugin) {
		this.plugin = plugin;
		this.arenas = new HashMap<String, AutoArena>();
		this.loadArenas();
	}
	
	public void loadArenas() {
		this.arenas.clear();
		for (String element : this.plugin.getRepo().getArenaNames()) {
			loadArena(element);
		}
	}
	
	public void loadArena(String name) {
		this.plugin.getLogger().info("Lade AutoArena "+name);
		if (this.getArena(name) != null) {
			this.plugin.getLogger().info("AutoArena "+name+" ist bereits geladen.");
			return;
		}
		AutoArena arena = new AutoArena(this.plugin, name);
		this.plugin.getLogger().info("AutoArena "+name+" geladen.");
		this.arenas.put(name.toLowerCase(), arena);
	}

	public AutoArena getArena(String name) {
		return arenas.get(name.toLowerCase());
	}
	
	public Collection<AutoArena> getArenas() {
		return this.arenas.values();
	}
	
	public Set<String> getArenaNames() {
		return this.arenas.keySet();
	}
	
	public boolean isArenaLoaded(String arenaName) {
		return getArena(arenaName) != null;
	}
	
	public AutoArena getArenaOfTeamMember(OfflinePlayer player) {
		for (AutoArena arena : this.arenas.values()) {
			if (arena.getTeam(player) != null) {
				return arena;
			}
		}
		return null;
	}
	
	public AutoArena getArenaAt(Location location) {
		
		Arena arena = this.plugin.getRepo().getWarGear().getArenaManager().getArenaAt(location);
		if (arena == null) {
			return null;
		} else {
			String arenaName = arena.getName();		
			return plugin.getArenenManager().getArena((arenaName));
		}
	}
	
	public Team getTeamByPlayer(Player player) {
		for (AutoArena arena : plugin.getArenenManager().getArenas()) {
			Team team = arena.getTeam(player);
			if (team != null) {
				return team;
			}
		}
		return null;
	}
}
