package de.fly4lol.autowgk;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.WorldEdit;

import de.fly4lol.autowgk.arena.AutoArenaMode;
import de.fly4lol.autowgk.arena.Direction;
import de.fly4lol.autowgk.schematic.Schematic;
import de.fly4lol.autowgk.sign.SignType;
import de.pro_crafting.sql.api.Connection;
import de.pro_crafting.sql.api.ConnectionManager;
import de.pro_crafting.sql.api.Credentials;
import de.pro_crafting.sql.bukkit.BukkitCredentials;
import de.pro_crafting.wg.WarGear;
import de.pro_crafting.wg.group.PlayerRole;

public class Repository {
	
	private Connection conn;
	private Main plugin;
	private WarGear wgPlugin;
	
	public Repository(Main plugin){
		this.plugin = plugin;
		this.wgPlugin = WarGear.getPlugin(WarGear.class);
		connect();
	}
	
	private void connect() {
		if (!this.plugin.getConfig().isSet("database")) {
			this.plugin.getConfig().set("database", new BukkitCredentials().serialize());
			this.plugin.saveConfig();
		} else {
			Credentials cred = new BukkitCredentials(this.plugin.getConfig().getConfigurationSection("database").getValues(false));
			ConnectionManager.getInstance().addConnectionData(cred);
			conn = ConnectionManager.getInstance().get(cred.getName());
		}
	}
	
	public WorldEdit getWorldEdit() {
		return WorldEdit.getInstance();
	}
	
	public WarGear getWarGear() {
		return this.wgPlugin;
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
	
	public List<String> getArenaNames(){
		Set<String> arenen= plugin.getConfig().getConfigurationSection("Arenen").getKeys(false);
		return new ArrayList<String>(arenen);
	}
	
	/*
	 * 
	 * Methods for sign zeugs
	 * 
	 */
	
	
	public void addSign(Player player, Location location,  SignType Type){
		try {
			PreparedStatement prep = conn.prepare("Insert Into signs.signs (uuid, World, X, Y, Z , Type) Values (?, ?, ?, ?, ?, ?)");
			prep.setString(1, player.getUniqueId().toString());
			prep.setString(2, location.getWorld().getName());
			prep.setInt(3, location.getBlockX());
			prep.setInt(4, location.getBlockY());
			prep.setInt(5, location.getBlockZ());
			prep.setInt(6, Type.ordinal());
			prep.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeSign(Location location){
		try {
			PreparedStatement prep = conn.prepare("Delete from signs.signs Where World=? AND X=? AND Y=? AND Z=?");
			prep.setString(1, location.getWorld().getName());
			prep.setInt(2, location.getBlockX());
			prep.setInt(3, location.getBlockY());
			prep.setInt(4, location.getBlockZ());
			prep.execute();	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean existSignAtLocation(Location location){
		try {
			PreparedStatement prep = conn.prepare("Select * From signs.signs Where World=? AND X=? AND Y=? AND Z=?");
			String world = location.getWorld().getName();
			int X = location.getBlockX();
			int Y = location.getBlockY();
			int Z = location.getBlockZ();
			prep.setString(1, world);
			prep.setInt(2, X);
			prep.setInt(3, Y);
			prep.setInt(4, Z);
			ResultSet res = prep.executeQuery();
			return res.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<Location> getSignsByType(SignType Type){
		List<Location> signs = new ArrayList <Location>();
		try {
			PreparedStatement prep = conn.prepare("Select * From signs.signs Where Type=?");
			prep.setInt(1, Type.ordinal());
			ResultSet res = prep.executeQuery();
			while(res.next()){
				World world = Bukkit.getWorld(res.getString("world"));
				int X = res.getInt("X");
				int Y = res.getInt("Y");
				int Z = res.getInt("Z");
				Location location = new Location(world, X, Y, Z);
				signs.add( location );
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return signs;
	}
	
	public SignType getSignType(Location location){
		SignType type = SignType.SIGN;
		try {
			PreparedStatement prep = conn.prepare("Select * From signs.signs Where World=? AND X=? AND Y=? AND Z=?");
			String world = location.getWorld().getName();
			int X = location.getBlockX();
			int Y = location.getBlockY();
			int Z = location.getBlockZ();
			prep.setString(1, world);
			prep.setInt(2, X);
			prep.setInt(3, Y);
			prep.setInt(4, Z);
			ResultSet res = prep.executeQuery();
			if(res.next()){
				int typeOrdinal = res.getInt("Type");
				type = SignType.values()[typeOrdinal];
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return type;
	}
	
	public List<Location> getSignsByOwner(OfflinePlayer player){
		List<Location> signs = new ArrayList <Location>();
		try {
			PreparedStatement prep = conn.prepare("Select * From signs.signs Where uuid=?");
			prep.setString(1, player.getUniqueId().toString());
			ResultSet res = prep.executeQuery();
			while(res.next()){
				World world = Bukkit.getWorld(res.getString("world"));
				int X = res.getInt("X");
				int Y = res.getInt("Y");
				int Z = res.getInt("Z");
				Location location = new Location(world, X, Y, Z);
				signs.add( location );
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return signs;
	}
	
	public List<Location> getSignsByWorld(String worldname){
		List<Location> signs = new ArrayList <Location>();
		try {
			PreparedStatement prep = conn.prepare("Select * From signs.signs Where World=?");
			prep.setString(1, worldname);
			ResultSet res = prep.executeQuery();
			while(res.next()){
				World world = Bukkit.getWorld(res.getString("world"));
				int X = res.getInt("X");
				int Y = res.getInt("Y");
				int Z = res.getInt("Z");
				Location location = new Location(world, X, Y, Z);
				signs.add( location );
			}
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return signs;
	}
	
	/*
	 * 
	 *  Schematics 
	 * 
	 */
	
	public Schematic getSchematicByID(int id){
		try {
			PreparedStatement prep = conn.prepare("Select * From schematics Where id=? And isWarGear=? And state=?");
			prep.setInt(1, id);
			prep.setInt(2, 1);
			prep.setInt(3, 1);
			ResultSet res = prep.executeQuery();
			if(res.next()){
				Schematic schematic = new Schematic(id, res.getString("schematic"), UUID.fromString( res.getString("uuid")), res.getBoolean("isPublic"));
				return schematic;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Schematic> getSchematisByOwner(OfflinePlayer player){
		List<Schematic> schematics = new ArrayList<Schematic>();
		try {
			PreparedStatement prep = conn.prepare("Select * From schematics Where uuid=? And isWarGear=? And state=?");
			prep.setString(1, player.getUniqueId().toString());
			prep.setInt(2, 1);
			prep.setInt(3, 1);
			ResultSet res = prep.executeQuery();
			while(res.next()){
				Schematic schematic = new Schematic(res.getInt("id"), res.getString("schematic"), UUID.fromString( res.getString("uuid")), res.getBoolean("isPublic"));
				schematics.add(schematic);
			}
			return schematics;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return schematics;
	}
	
	public List<Schematic> getPublicSchematics(){
		List<Schematic> schematics = new ArrayList<Schematic>();
		try {
			PreparedStatement prep = conn.prepare("Select * From schematics Where isPublic=? And isWarGear=? And state=?");
			prep.setInt(1 , 1);
			prep.setInt(2, 1);
			prep.setInt(3, 1);
			ResultSet res = prep.executeQuery();
			while(res.next()){
				Schematic schematic = new Schematic(res.getInt("id"), res.getString("schematic"), UUID.fromString( res.getString("uuid")), res.getBoolean("isPublic"));
				schematics.add(schematic);
			}
			return schematics;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return schematics;
	}
	
	
	public boolean hasRights(int schematicId, UUID player) {
		try {
			PreparedStatement prep = conn.prepare("Select * From schematics Where id=? And (isPublic=? OR uuid=?)"+
												"UNION" +
												"Select * From permission_player Where schematicid=? AND uuid=?");
			prep.setInt(1, schematicId);
			prep.setInt(2, 1);
			prep.setString(3, player.toString());
			prep.setInt(4, schematicId);
			prep.setString(5, player.toString());
			ResultSet res = prep.executeQuery();
			return res.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
