package de.fly4lol.autowgk.arena;

import java.io.File;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.schematic.Schematic;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.PlayerRole;

public class AutoArena {
	private Main			plugin;
	private String			name;
	private Location		team1Loc;
	private Location		team2Loc;
	private AutoArenaMode	mode;
	private Direction			team1Direction;
	private Direction			team2Direction;
	private Team			team1;
	private Team			team2;
	private Arena			arena;
	
	public AutoArena(Main plugin, String name) {
		this.name = name;
		this.plugin = plugin;
		this.team1Loc = this.plugin.getRepo().getPastingLocation(name, PlayerRole.Team1);
		this.team2Loc = this.plugin.getRepo().getPastingLocation(name, PlayerRole.Team2);
		this.team1Direction = this.plugin.getRepo().getDirection(name, PlayerRole.Team1);
		this.team2Direction = this.plugin.getRepo().getDirection(name, PlayerRole.Team2);
		this.mode = this.plugin.getRepo().getMode(name);
		this.arena = this.plugin.getRepo().getWarGear().getArenaManager().getArena(name);
	}
	
	public Arena getWgkArena() {
	
		return arena;
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
	
	public Direction getTeam1Direction() {
	
		return team1Direction;
	}
	
	public void setTeam1Direction(Direction team1Location) {
	
		this.team1Direction = team1Location;
	}
	
	public Direction getTeam2Direction() {
	
		return team2Direction;
	}
	
	public void setTeam2Direction(Direction team2Location) {
	
		this.team2Direction = team2Location;
	}
	
	public AutoArenaMode getMode() {
	
		return mode;
	}
	
	public void setMode(AutoArenaMode mode) {
	
		this.mode = mode;
	}
	
	public Team getTeam1() {
	
		return team1;
	}
	
	public void setTeam1(Team team1) {
	
		this.team1 = team1;
	}
	
	public Team getTeam2() {
	
		return team2;
	}
	
	public void setTeam2(Team team2) {
	
		this.team2 = team2;
	}
	
	public Main getPlugin() {
	
		return this.plugin;
	}
	
	public boolean isJoinable() {
	
		Arena arena = this.getWgkArena();
		State state = arena.getState();
		return arena != null && (this.getTeam1() == null || this.getTeam2() == null) && state != State.PreRunning && state != State.Running;
	}
	
	public Team joinArena(Player player) {
		Team team = new Team(player, this, PlayerRole.Team1);
		if (this.getTeam1() == null) {
			this.setTeam1(team);
		} else {
			team = new Team(player, this, PlayerRole.Team2);
			this.setTeam2(team);
		}
		return team;
	}
	
	public void startGame() {
	
		if (this.getWgkArena().getState() == State.Idle) {
			this.getWgkArena().updateState(State.Setup);
		}
		this.getTeam1().startGame();
		this.getTeam2().startGame();	
	}
	
	public Team getTeam(OfflinePlayer player) {
		if (team1 != null && team1.getLeader().equals(player)){
			return team1;
		} else if (team2 != null && team2.getLeader().equals(player)){
			return team2;
		}
		return null;
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
