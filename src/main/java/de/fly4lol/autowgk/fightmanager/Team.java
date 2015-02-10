package de.fly4lol.autowgk.fightmanager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.util.Config;
import de.fly4lol.autowgk.util.Schematic;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.group.Group;
import de.pro_crafting.wg.group.PlayerRole;

public class Team {
	
	private Player		leader;
	private Schematic	schematic;
	private AutoArena	arena		= null;
	private boolean		isReady	= false;
	private PlayerRole role;
	
	public Team(Player leader, AutoArena arena, PlayerRole role) {
	
		this.leader = leader;
		this.isReady = false;
		this.arena = arena;
		this.role = role;
	}
	
	public AutoArena getAutoArena() {
	
		return arena;
	}
	
	public Player getLeader() {
	
		return leader;
	}
	
	public Team setLeader(Player leader) {
	
		this.leader = leader;
		return this;
	}
	
	public Schematic getSchematic() {
	
		return schematic;
	}
	
	public Team setSchematic(Schematic schematic) {
	
		this.schematic = schematic;
		return this;
	}
	
	public boolean isReady() {
	
		return isReady;
	}
	
	public Team setReady(boolean isReady) {
	
		this.isReady = isReady;
		return this;
	}
	
	public PlayerRole getRole() {
		return this.role;
	}
	
	public Team startGame(boolean isTeam1) {
	
		Arena wgkArena = this.getAutoArena().getWgkArena();
		Player player = this.getLeader();
		Group group = wgkArena.getGroupManager().getGroup2();
		if (isTeam1) {
			group = wgkArena.getGroupManager().getGroup1();
		}
		group.add(player, true);
		player.teleport(wgkArena.getGroupManager().getGroupSpawn(group.getRole()));
		player.sendMessage("§7Mit §B\"/wgk team invite <spieler>\" §7lädst du Spieler zu deinem Team ein.");
		player.sendMessage("§7Mit §B\"/wgk team remove <spieler>\" §7entfernst du Spieler aus deinem Team.");
		player.sendMessage("§7Mit §B\"/wgk team ready\" §7schaltest du dein Team bereit.");
		this.getAutoArena().getPlugin().wg.getScoreboard().addTeamMember(wgkArena, group.getMember(player), group.getRole());
		
		return this;
	}
	
	public Team pasteSchematic() {
	
		Util util = this.getAutoArena().getPlugin().getUtil();
		Config config = this.getAutoArena().getPlugin().getAutoWGKConfig();
		String arenaName = this.getAutoArena().getName();
		Direction direction = Direction.south;
		Location location = null;
		
		/*
		 * 
		 * Direction and Location
		 */
		
		if (this.getAutoArena().getTeam1() == this) {
			location = config.getPastingLocation(arenaName, true);
			
			if (config.isNorth(arenaName, true)) {
				direction = Direction.north;
			}
			
		} else if (this.getAutoArena().getTeam2() == this) {
			location = config.getPastingLocation(arenaName, false);
			
			if (config.isNorth(arenaName, false)) {
				direction = Direction.north;
			}
			
		} else {
			System.out.println("Team ist in keiner Arena! [PasteSchematic] Class: Team");
		}
		
		/*
		 * 
		 * Pasting
		 */
		
		util.pasteSchematic(this.schematic, direction, location, this.leader);
		
		return this;
	}
}
