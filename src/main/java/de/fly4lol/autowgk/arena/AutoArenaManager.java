package de.fly4lol.autowgk.arena;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;

public class AutoArenaManager {

	private Main plugin;
	private Map<String, AutoArena> arenas;
	private List<Player> afkPlayers = new ArrayList<Player>();
	
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
	
public void removeAFKPlayers(AutoArena arena){
		
		for(Player player : this.afkPlayers){
			AutoArena arena2 =this.plugin.getArenenManager().getArenaOfTeamMember( player );
			if(arena2 == null){
				this.afkPlayers.remove( player );
			} else if(this.isFightRunning( arena2.getWgkArena())){
				this.afkPlayers.remove( player );
			}
		}
		
		if(arena.getTeam1() != null){
			if(!arena.getTeam1().getLeader().isOnline()){
				arena.setTeam1( null );
			}
		}

		
		if(arena.getTeam2() != null){
			if(!arena.getTeam2().getLeader().isOnline()){
				arena.setTeam2( null );
			}
		}
		
		if(! this.isFightRunning( arena.getWgkArena())){
			
			if(arena.getTeam1() != null){
				Player player = arena.getTeam1().getLeader();
				if(this.afkPlayers.contains( player )){
					arena.setTeam1( null);
				} else {
					this.afkPlayers.add( player );
				}
			}

			
			if(arena.getTeam2() != null){
				Player player = arena.getTeam2().getLeader();
				if(this.afkPlayers.contains( player )){
					arena.setTeam2( null);
				} else {
					this.afkPlayers.add( player );
				}
			}
			
		}
	}
	
	public boolean isFightRunning(Arena arena ){
		if(arena.getState() == State.PreRunning){
			return true;
		}
		
		if(arena.getState() == State.Running){
			return true;
		}
		
		if(arena.getState() == State.Setup){
			return true;
		}
		
		return false;
	}
}
