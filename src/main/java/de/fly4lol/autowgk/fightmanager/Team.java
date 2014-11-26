package de.fly4lol.autowgk.fightmanager;

import org.bukkit.entity.Player;

public class Team {
	private Player leader;
	private String schematic;
	private boolean isFinish;
	
	
	public Team(Player leader, String schematic, boolean isReady){
		this.leader = leader;
		this.schematic = schematic;
		this.isFinish = isFinish;
	}
	
	public Team(){
		
	}


	public Player getLeader() {
		return leader;
	}


	public void setLeader(Player leader) {
		this.leader = leader;
	}


	public String getSchematic() {
		return schematic;
	}


	public void setSchematic(String schematic) {
		this.schematic = schematic;
	}


	public boolean isFinish() {
		return isFinish;
	}


	public void setReady(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
}
