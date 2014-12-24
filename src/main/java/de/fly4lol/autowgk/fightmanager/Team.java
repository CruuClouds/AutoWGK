package de.fly4lol.autowgk.fightmanager;

import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.Schematic;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.Group;

public class Team {
	private Player leader;
	private Schematic schematic;
	private AutoArena arena;
	private boolean isFinish;
	
	
	public Team(Player leader, Schematic schematic, boolean isReady){
		this.leader = leader;
		this.schematic = schematic;
		this.isFinish = isFinish;
	}
	
	public Team(){
		
	}
	
	

	public AutoArena getArena() {
		return arena;
	}

	public Team setArena(AutoArena arena) {
		this.arena = arena;
		return this;
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


	public boolean isFinish() {
		return isFinish;
	}


	public Team setReady(boolean isFinish) {
		this.isFinish = isFinish;
		return this;
	}
	
	public Team startGame(boolean isTeam1){
		Arena wgkArena = this.getArena().getArena();
		Player player = this.getLeader();
		Group group = wgkArena.getGroupManager().getGroup2();
		if(isTeam1){
			 group = wgkArena.getGroupManager().getGroup1();
		}
		group.add( player , true);
		player.teleport( wgkArena.getGroupManager().getGroupSpawn( group.getRole()));
		player.sendMessage("§7Mit §B\"/wgk team invite <spieler>\" §7lädst du Spieler zu deinem Team ein.");
		player.sendMessage("§7Mit §B\"/wgk team remove <spieler>\" §7entfernst du Spieler aus deinem Team.");
		player.sendMessage("§7Mit §B\"/wgk team ready\" §7schaltest du dein Team bereit.");
		this.getArena().getPlugin().wg.getScoreboard().addTeamMember( wgkArena , group.getMember( player), group.getRole());
		return this;
	}
	
	public Team pasteSchematic(Direction direction){
		return this;
	}
}
