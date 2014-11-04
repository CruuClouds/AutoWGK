package de.fly4lol.autowgk.util;

import org.bukkit.Location;

public class Arena {
	private Location team1Loc;
	private Location team2Loc;
	private boolean isEnabled;
	private String team1Location;
	private String team2Location;
	
	public Arena(Location team1Loc, Location team2Loc, boolean isEnabled, String team1Location, String team2Location){
		this.team1Loc = team1Loc;
		this.team2Loc = team2Loc;
		this.isEnabled = isEnabled;
		this.team1Location = team1Location;
		this.team2Location = team2Location;
		
	}

}
