package de.fly4lol.autowgk.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.fightmanager.Direction;
import de.pro_crafting.wg.group.PlayerRole;

public class Config {
	
	private Main plugin;
	
	public Config(Main plugin) {
		this.plugin = plugin;
	}
 	
	public void creatArena(String Arena){
		plugin.getConfig().set("Arenen." + Arena, "");
	}
	
	public void addTeam(String Arena,PlayerRole role, Direction direction, int X, int Y, int Z, String World){
		plugin.getConfig().set("Arenen." + Arena + "."+ role.name(), "");
		plugin.getConfig().set("Arenen." + Arena + ".Mode" , AutoArenaMode.NORMAL.ordinal());
		plugin.saveConfig();
		plugin.getConfig().set("Arenen." + Arena + "." + role.name() + ".Direction", direction.getName() );
		plugin.getConfig().set("Arenen." + Arena + "." + role.name() + ".World", World);
		plugin.getConfig().set("Arenen." + Arena + "." + role.name() + ".X", X);
		plugin.getConfig().set("Arenen." + Arena + "." + role.name() + ".Y", Y);
		plugin.getConfig().set("Arenen." + Arena + "." + role.name() + ".Z", Z);
		plugin.saveConfig();
	}
	
	public void setMode(String Arena, AutoArenaMode Mode){
		
		plugin.getConfig().set("Arenen." + Arena + ".Mode" , Mode.toString());
		plugin.saveConfig();
	}
	
	public AutoArenaMode getMode(String Arena){
		return AutoArenaMode.values()[ plugin.getConfig().getInt("Arenen." + Arena + ".Mode") ];
		
	}
	
	public Location getPastingLocation(String Arena, PlayerRole role){
		String world = plugin.getConfig().getString("Arenen." + Arena + "." + role.name() + ".World");
		double x = plugin.getConfig().getDouble("Arenen." + Arena + "." + role.name() + ".X");
		double y = plugin.getConfig().getDouble("Arenen." + Arena + "." + role.name() + ".Y");
		double z = plugin.getConfig().getDouble("Arenen." + Arena + "." + role.name() + ".Z");
		World World = Bukkit.getWorld(world);
		
		Location loc = new Location(World, x, y, z);
		return loc;
	}
	
	public Direction getDirection(String arena, PlayerRole role) {
		String direction = plugin.getConfig().getString("Arenen." + arena + "." + role.name() + ".Direction");
		return direction.equalsIgnoreCase("north") ? Direction.North : Direction.South;
	}
	
	public List<AutoArena> getAutoArenen(){
		List<AutoArena> ret = new ArrayList<AutoArena>();
		Set<String> arenen= plugin.getConfig().getConfigurationSection("Arenen").getKeys(false);
		
		for(String arena : arenen){
			ret.add(new AutoArena(plugin, arena));
		}
		return ret;
	}

}
