package de.fly4lol.autowgk;

import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.Direction;
import de.fly4lol.autowgk.fightmanager.Team;
import de.fly4lol.autowgk.util.Config;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.fly4lol.autowgk.util.Schematic;
import de.fly4lol.autowgk.util.SchematicState;
import de.fly4lol.messenger.Message;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.PlayerRole;

public class OldCommands {
	
	private final Main			plugin;
	private final MySQLMethods	sql;
	private final Config		config;
	
	public OldCommands(Main plugin) {
	
		this.plugin = plugin;
		this.config = plugin.getAutoWGKConfig();
		this.sql = plugin.getSQL();
	}
	
	/*
	 * 
	 * Schematic commands
	 */
	
	@Command(name = "AutoWGK.schematic.list", usage = "/AutoWGK", permission = "autowgk.schematic.list", aliases = { "awgk.schem.list", "awgk.schematic.list", "Autowgk.schem.list" })
	public void autowgkSchematicList(CommandArgs args) {
	
		Player player = args.getPlayer();
		if (args.getArgs().length == 0) {
			List<Schematic> schematics = this.sql.getSchematisByOwner(player);
			Message message = new Message();
			message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
			for (Schematic schematic : schematics) {
				if (schematic.isWarGear()) {
					String prefix = "§8[§6Private§8] §b";
					if (schematic.isPublic()) {
						prefix = "§8[§6Public§8] §b";
					}
					FancyMessage wargearMessage = new FancyMessage("").then("§9#" + schematic.getId() + " " + prefix + schematic.getName()).tooltip("§eHier klicken").command("/AutoWGK schematic load " + schematic.getId());
					message.addLine(wargearMessage);
				}
			}
			new Messenger().addPlayer(player).setMessage(message).send();
		} else if (args.getArgs().length == 1) {
			if (player.hasPermission("autowgk.schematic.list.other")) {
				@SuppressWarnings("deprecation")
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.getArgs()[0]);
				List<Schematic> schematics = this.sql.getSchematisByOwner(offlinePlayer);
				Message message = new Message();
				message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
				for (Schematic schematic : schematics) {
					if (schematic.isWarGear()) {
						if (schematic.getState().equals(SchematicState.Checked)) {
							String prefix = "§8[§6Private§8] §b";
							if (schematic.isPublic()) {
								prefix = "§8[§6Public§8] §b";
							}
							FancyMessage wargearMessage = new FancyMessage("").then("§9#" + schematic.getId() + " " + prefix + schematic.getName()).tooltip("§eHier klicken").command("/AutoWGK schematic load " + schematic.getId());
							message.addLine(wargearMessage);
						}
					}
				}
				new Messenger().addPlayer(player).setMessage(message).send();
			}
		}
	}
	
	@Command(name = "AutoWGK.schematic.public", usage = "/AutoWGK", permission = "autowgk.schematic.public", aliases = { "awgk.schem.public", "awgk.schematic.public", "Autowgk.schem.public" })
	public void autowgkSchematicPublic(CommandArgs args) {
	
		Player player = args.getPlayer();
		List<Schematic> schematics = this.sql.getPublicSchematics();
		Message message = new Message();
		message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
		for (Schematic schematic : schematics) {
			if (schematic.isWarGear()) {
				if (schematic.getState().equals(SchematicState.Checked)) {
					String prefix = "§8[§6" + Bukkit.getOfflinePlayer(schematic.getOwner()).getName() + "§8] §b";
					FancyMessage wargearMessage = new FancyMessage("").then("§9#" + schematic.getId() + " " + prefix + schematic.getName()).tooltip("§e Hier klicken").command("/AutoWGK schematic load " + schematic.getId());
					message.addLine(wargearMessage);
				}
			}
		}
		new Messenger().addPlayer(player).setMessage(message).send();
		
	}
	
	@Command(name = "AutoWGK.schematic.load", usage = "/AutoWGK", permission = "autowgk.schematic.load", aliases = { "awgk.schem.load", "awgk.schematic.load", "Autowgk.schem.load" })
	public void autowgkSchematicLoad(CommandArgs args) {
	
		Player player = args.getPlayer();
		Team team = this.plugin.getUtil().getTeamByPlayer(player);
		AutoArena arena = team.getAutoArena();
		Team otherTeam = null;
		if (args.getArgs().length != 1) {
			player.sendMessage(plugin.prefix + "Du musst eine Schematic angeben!");
			return;
		}
		if (team == null) {
			player.sendMessage(plugin.prefix + "Du bist in keinem Team!");
			return;
		}
		int id = 0;
		try {
			id = Integer.parseInt(args.getArgs()[0]);
		} catch (Exception e) {
			player.sendMessage(plugin.prefix + "Du musst eine Id angeben!");
		}
		Schematic schematic = plugin.getSQL().getSchematicByID(id);
		String direction = schematic.getName().substring(schematic.getName().length() - 2, schematic.getName().length());
		
		Direction northSouth = Direction.south;
		if (direction.equalsIgnoreCase("_n")) {
			northSouth = Direction.north;
		} else if (direction.equalsIgnoreCase("_s")) {
			northSouth = Direction.south;
		} else {
			player.sendMessage(plugin.prefix + "Dein WarGear konnte nicht geladen werden!");
		}
		
		schematic.setDirection(northSouth);
		team.setSchematic(schematic).setReady(true);
		player.sendMessage(plugin.prefix + "Du hast das WarGear §e" + schematic.getName() + " §3ausgewählt!");
		
		if (team.getRole() == PlayerRole.Team1) {
			otherTeam = arena.getTeam2();
		} else {
			otherTeam = arena.getTeam1();
		}
		if (otherTeam != null && otherTeam.isReady() && arena.getWgkArena().getState() == State.Idle) {
			arena.startGame();
		} else if (arena.getWgkArena().getState() == State.Setup) {
			team.pasteSchematic();
			team.startGame();
		}
	}
}
