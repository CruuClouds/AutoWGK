package de.fly4lol.autowgk.fightmanager;

import java.io.File;

import org.bukkit.Location;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.schematic.Schematic;

public class Util {
	
	private final Main		plugin;
	
	public Util(Main plugin) {
	
		this.plugin = plugin;
	}
	
	public void pasteSchematic(Schematic schematic, Direction direction, Location location) {
		if (schematic.getDirection() == null) {
			return;
		}
		WorldEdit we = this.plugin.getRepo().getWorldEdit();
		LocalConfiguration config = we.getConfiguration();
		
		File dir = we.getWorkingDirectoryFile(config.saveDir);
		File schematicFile = new File(dir, schematic.getPath());
		
		
		try {
			EditSession es = we.getEditSessionFactory().getEditSession(BukkitUtil.getLocalWorld(location.getWorld()), 32000000);
			CuboidClipboard cc = MCEditSchematicFormat.MCEDIT.load(schematicFile);
			
			if (direction != schematic.getDirection()) {
				cc.rotate2D(180);
			}
			
			es.enableQueue();
			cc.paste(es, BukkitUtil.toVector(location), false);
			es.flushQueue();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
