package de.fly4lol.autowgk.fightmanager;

import org.bukkit.entity.Player;

import de.fly4lol.autowgk.util.Schematic;

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
}
