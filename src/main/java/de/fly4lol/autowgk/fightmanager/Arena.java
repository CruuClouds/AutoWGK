package de.fly4lol.autowgk.fightmanager;

import org.bukkit.Location;

public class Arena {
	private String name;
	private Location team1Loc;
	private Location team2Loc;
	private ArenaMode mode;
	private String team1Direction;
	private String team2Direction;
	
	public Arena(String name, Location team1Loc, Location team2Loc, ArenaMode mode, String team1Direction, String team2Direction){
		this.team1Loc = team1Loc;
		this.team2Loc = team2Loc;
		this.mode = mode;
		this.team1Direction = team1Direction;
		this.team2Direction = team2Direction;
	}
	
	public Arena(){
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getTeam1Loc() {
		return team1Loc;
	}

	public void setTeam1Loc(Location team1Loc) {
		this.team1Loc = team1Loc;
	}

	public Location getTeam2Loc() {
		return team2Loc;
	}

	public void setTeam2Loc(Location team2Loc) {
		this.team2Loc = team2Loc;
	}

	public String getTeam1Direction() {
		return team1Direction;
	}

	public void setTeam1Direction(String team1Location) {
		this.team1Direction = team1Location;
	}

	public String getTeam2Direction() {
		return team2Direction;
	}

	public void setTeam2Direction(String team2Location) {
		this.team2Direction = team2Location;
	}

	public ArenaMode getMode() {
		return mode;
	}

	public void setMode(ArenaMode mode) {
		this.mode = mode;
	}
	
	
	
}
