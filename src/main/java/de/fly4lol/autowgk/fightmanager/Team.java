package de.fly4lol.autowgk.fightmanager;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Repository;
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
	
	public Team startGame() {
			
		Arena wgkArena = this.getAutoArena().getWgkArena();
		Player player = this.getLeader();
		Group group = wgkArena.getGroupManager().getTeamOfGroup(this.role);
		group.add(player, true);
		player.teleport(wgkArena.getGroupManager().getGroupSpawn(group.getRole()));
		this.getAutoArena().getPlugin().getRepo().getWarGear().getScoreboard().addTeamMember(wgkArena, group.getMember(player), group.getRole());
		player.sendMessage("§7Mit §B\"/wgk team invite <spieler>\" §7lädst du Spieler zu deinem Team ein.");
		player.sendMessage("§7Mit §B\"/wgk team remove <spieler>\" §7entfernst du Spieler aus deinem Team.");
		player.sendMessage("§7Mit §B\"/wgk team ready\" §7schaltest du dein Team bereit.");
		
		pasteSchematic();
		return this;
	}
	
	public Team pasteSchematic() {
	
		Util util = this.getAutoArena().getPlugin().getUtil();
		Repository repo = this.getAutoArena().getPlugin().getRepo();
		String arenaName = this.getAutoArena().getName();
		
		Direction direction = repo.getDirection(arenaName, this.role);
		Location location = repo.getPastingLocation(arenaName, this.role);
		
		util.pasteSchematic(this.schematic, direction, location, this.leader);
		
		return this;
	}
}
