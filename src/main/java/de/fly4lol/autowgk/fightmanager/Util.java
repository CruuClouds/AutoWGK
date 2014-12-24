package de.fly4lol.autowgk.fightmanager;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.primesoft.asyncworldedit.worldedit.AsyncEditSession;
import org.primesoft.asyncworldedit.worldedit.AsyncEditSessionFactory;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.Config;
import de.fly4lol.autowgk.util.Schematic;
import de.pro_crafting.wg.arena.Arena;

public class Util {
	private Main plugin;
	private Config config;
	
	public Util(Main plugin) {
		this.plugin = plugin;
		this.config = plugin.getAutoWGKConfig();
	}
	
	public AutoArena getArenaAt(Location location){
		Arena arena = plugin.wg.getArenaManager().getArenaAt( location);
		if(arena == null){
			return null;
		} else {
			String arenaName = arena.getName();
			return plugin.loadedArenen.get( arenaName);
		}
	}
	
	public Team getTeamByPlayer(Player player){
		List<AutoArena> autoArenen = plugin.getAutoWGKConfig().getAutoArenen();
		for(AutoArena arena : autoArenen){
			Team team1 = plugin.loadedArenen.get(arena.getName()).getTeam1();
			Team team2 = plugin.loadedArenen.get(arena.getName()).getTeam2();
			if(team1 == null || team1.getLeader() == player){
				return team1;
			}
			if(team2 == null || team2.getLeader() ==player ){
				return team2;
			}
		}
		return null;
	}
	
//	public void pasteSchematic(Schematic schematic, Direction direction, Location location){
//		try {
//			String schmaticName = schematic.getName();
//            WorldEdit we = ((WorldEditPlugin)Bukkit.getPluginManager().getPlugin("WorldEdit")).getWorldEdit();
//            LocalConfiguration config = we.getConfiguration();
//           
//            File dir = we.getWorkingDirectoryFile(config.saveDir);
//            if (!schmaticName.contains(".schematic")) schmaticName = schmaticName+".schematic";
//            File schematicName = new File(dir, schematicName);
//           
//            AsyncEditSession es = new AsyncEditSession(new AsyncEditSessionFactory(PluginMain.getInstance()), PluginMain.getInstance(), "CONSOLE!!!!!!1elf", new BukkitWorld(where.getWorld()), config.maxChangeLimit);
//            CuboidClipboard cc = MCEditSchematicFormat.MCEDIT.load(schematic);
//            
//            
//            cc.rotate2D(180);
//            es.enableQueue();
//            es.setFastMode(true);
//            cc.paste(es, BukkitUtil.toVector(where), false);
//            es.setFastMode(false);
//            es.flushQueue();
//    } catch (Exception ex) {
//            ex.printStackTrace();
//    }
//	}

}
