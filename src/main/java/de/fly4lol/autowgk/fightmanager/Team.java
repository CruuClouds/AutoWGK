package de.fly4lol.autowgk.fightmanager;

import org.bukkit.entity.Player;

public class Team {
	public Player leader;
	public String schematic;
	public boolean isFinish;
	
	
	public void Arena(Player leader, String schematic, boolean isFinish){
		this.leader = leader;
		this.schematic = schematic;
		this.isFinish = isFinish;
	}
}
