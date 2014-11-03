package de.fly4lol.autowgk.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.fly4lol.autowgk.Main;

public class Config {
	
	public static Main plugin;
	
	
	public void creatArena(String Arena){
		plugin.getConfig().set("Arenen." + Arena, "");
	}
	
	public void addTeam(String Arena, boolean team1, boolean north, int X, int Y, int Z, String World){
		String team = "Team2";
		String location = "south";
		if(team1){
			team = "Team1";
		}
		if(north){
			location = "north";
		}
		plugin.getConfig().set("Arenen." + Arena + "."+ team, location);
		plugin.getConfig().set("Arenen." + Arena + ".Enabled" , true);
		plugin.saveConfig();
		plugin.getConfig().set("Arenen." + "Arena" + "." + team + ".World", World);
		plugin.getConfig().set("Arenen." + "Arena" + "." + team + ".X", X);
		plugin.getConfig().set("Arenen." + "Arena" + "." + team + ".Y", Y);
		plugin.getConfig().set("Arenen." + "Arena" + "." + team + ".Z", Z);
		plugin.saveConfig();
	}
	
	public void setEnabled(String Arena, boolean isEnabled){
		String Enabled = "false";
		if(isEnabled){
			Enabled = "true";
		}
		plugin.getConfig().set("Arenen." + Arena + ".Enabled" , Enabled);
		plugin.saveConfig();
	}
	
	public boolean isEnabled(String Arena){
		return plugin.getConfig().getBoolean("Arenen." + Arena + ".Enabled");
	}
	
	public Location getPastingLocation(String Arena, boolean Team1){
		String team = "Team2";
		if(Team1){
			team = "Team1";
		}
		String world = plugin.getConfig().getString("Arenen." + "Arena" + "." + team + ".World");
		double x = plugin.getConfig().getDouble("Arenen." + "Arena" + "." + team + ".X");
		double y = plugin.getConfig().getDouble("Arenen." + "Arena" + "." + team + ".Y");
		double z = plugin.getConfig().getDouble("Arenen." + "Arena" + "." + team + ".Z");
		World World = Bukkit.getWorld(world);
		
		Location loc = new Location(World, x, y, z);
		return loc;
	}
	
	public boolean isNorth(String Arena, boolean team1){
		String team = "Team2";
		if(team1){
			team = "Team1";
		}
		String location = plugin.getConfig().getString("Arenen." + Arena + "." + team);
		return location.equalsIgnoreCase("north");
	}

}
