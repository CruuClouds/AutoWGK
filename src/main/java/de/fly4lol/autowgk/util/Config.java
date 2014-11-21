package de.fly4lol.autowgk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.Arena;
import de.fly4lol.autowgk.fightmanager.ArenaMode;

public class Config {
	
	private Main plugin;
	
	public Config(Main plugin) {
		this.plugin = plugin;
	}
 	
	public void creatArena(String Arena){
		plugin.getConfig().set("Arenen." + Arena, "");
	}
	
	public void addTeam(String Arena, boolean team1, boolean north, int X, int Y, int Z, String World){
		String team = "Team2";
		String direction = "south";
		if(team1){
			team = "Team1";
		}
		if(north){
			direction = "north";
		}
		plugin.getConfig().set("Arenen." + Arena + "."+ team, "");
		plugin.getConfig().set("Arenen." + Arena + ".Mode" , ArenaMode.NORMAL.ordinal());
		plugin.saveConfig();
		plugin.getConfig().set("Arenen." + Arena + "." + team + ".Direction", direction );
		plugin.getConfig().set("Arenen." + Arena + "." + team + ".World", World);
		plugin.getConfig().set("Arenen." + Arena + "." + team + ".X", X);
		plugin.getConfig().set("Arenen." + Arena + "." + team + ".Y", Y);
		plugin.getConfig().set("Arenen." + Arena + "." + team + ".Z", Z);
		plugin.saveConfig();
	}
	
	public void setMode(String Arena, Enum Mode){
		
		plugin.getConfig().set("Arenen." + Arena + ".Mode" , Mode.toString());
		plugin.saveConfig();
	}
	
	public ArenaMode getMode(String Arena){
		return ArenaMode.values()[ plugin.getConfig().getInt("Arenen." + Arena + ".Mode") ];
		
	}
	
	public Location getPastingLocation(String Arena, boolean Team1){
		String team = "Team2";
		if(Team1){
			team = "Team1";
		}
		String world = plugin.getConfig().getString("Arenen." + Arena + "." + team + ".World");
		double x = plugin.getConfig().getDouble("Arenen." + Arena + "." + team + ".X");
		double y = plugin.getConfig().getDouble("Arenen." + Arena + "." + team + ".Y");
		double z = plugin.getConfig().getDouble("Arenen." + Arena + "." + team + ".Z");
		World World = Bukkit.getWorld(world);
		
		Location loc = new Location(World, x, y, z);
		return loc;
	}
	
	public boolean isNorth(String Arena, boolean team1){
		String team = "Team2";
		if(team1){
			team = "Team1";
		}
		String location = plugin.getConfig().getString("Arenen." + Arena + "." + team + ".Direction");
		if(location.equalsIgnoreCase("north")){
			return true;
		} else {
			return false;
		}
	}
	
	public List<Arena> getAutoArenen(){
		List<Arena> liste = new ArrayList<Arena>();
		Set<String> arenen= plugin.getConfig().getConfigurationSection("Arenen").getKeys(false);
		
		for(String arena1 : arenen){
			
			Arena arena = new Arena();
			String directionTeam1 = "south";
			String directionTeam2 = "south";
			
			if(this.isNorth( arena1, true)){
				directionTeam1= "north";
			}
			
			if(this.isNorth( arena1, false)){
				directionTeam2= "north";
			}
			
			arena.setName(arena1);
			arena.setTeam1Loc(this.getPastingLocation( arena.getName(), true));
			arena.setTeam2Loc(this.getPastingLocation( arena.getName(), false));
			arena.setTeam1Direction( directionTeam1);
			arena.setTeam2Direction( directionTeam2);
			arena.setMode( this.getMode( arena.getName()));
			liste.add( arena );
		}
		return liste;
	}

}
