package de.fly4lol.autowgk.commands;

import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.Repository;
import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.fightmanager.Direction;
import de.fly4lol.autowgk.fightmanager.Team;
import de.fly4lol.autowgk.util.Schematic;
import de.fly4lol.autowgk.util.SchematicState;
import de.fly4lol.messenger.Message;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.PlayerRole;

public class SchematicCommands {
	private Main plugin;
	private Repository repo;
	
	public SchematicCommands(Main plugin){
		this.plugin = plugin;
		this.repo = plugin.getRepo();
	}
	
	@Command(name = "AutoWGK.schematic", usage = "/AutoWGK", permission = "autowgk.schematic" , aliases = {"awgk.schem"  , "AutoWGK.schem" })
	public void autowgkSchematic(CommandArgs args) {
		Player player = args.getPlayer();
		player.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands.");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic load");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic list");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic public");
	}

	@Command(name = "AutoWGK.schematic.private", usage = "/AutoWGK", permission = "autowgk.schematic.private" , aliases = {"awgk.create" , "awgk.arena.create"}, inGameOnly=true)
	public void autowgkArenaCreate(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		if(args.getArgs().length >= 2){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK schematic private");
			return;
		}
		
		if(plugin.loadedArenen.get( arena.getName() ).getMode() == AutoArenaMode.DISABLED){
			player.sendMessage(plugin.prefix + "Du kannst im Moment nicht in dieser Arena spielen!");
			return;
		}
		
		repo.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §e" + arena.getName()+ " §3erstellt!");
	}
	
	@Command(name = "AutoWGK.schematic.list", usage = "/AutoWGK", permission = "autowgk.schematic.list", aliases = { "awgk.schem.list", "awgk.schematic.list", "Autowgk.schem.list" }, inGameOnly=true)
	public void autowgkSchematicList(CommandArgs args) {
		Player player = args.getPlayer();
		OfflinePlayer schematicsOf = player;
		if (args.length() == 1 && player.hasPermission("autowgk.schematic.list.other")) {
			schematicsOf = Bukkit.getOfflinePlayer(args.getArgs()[0]);
			if (schematicsOf == null) {
				return;
			}
		}
		
		List<Schematic> schematics = this.repo.getSchematisByOwner(schematicsOf);
		Message message = new Message();
		message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
		for (Schematic schematic : schematics) {
			if (schematic.isWarGear() && schematic.getState() == SchematicState.Checked) {
				String prefix = "§8[§6Private§8] §b";
				if (schematic.isPublic()) {
					prefix = "§8[§6Public§8] §b";
				}
				FancyMessage wargearMessage = new FancyMessage("").then("§9#" + schematic.getId() + " " + prefix + schematic.getName()).tooltip("§eHier klicken").command("/AutoWGK schematic load " + schematic.getId());
				message.addLine(wargearMessage);
			}
		}
		new Messenger().addPlayer(player).setMessage(message).send();
	}
	
	@Command(name = "AutoWGK.schematic.public", usage = "/AutoWGK", permission = "autowgk.schematic.public", aliases = { "awgk.schem.public", "awgk.schematic.public", "Autowgk.schem.public" }, inGameOnly=true)
	public void autowgkSchematicPublic(CommandArgs args) {
	
		Player player = args.getPlayer();
		List<Schematic> schematics = this.repo.getPublicSchematics();
		Message message = new Message();
		message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
		for (Schematic schematic : schematics) {
			if (schematic.isWarGear()  && schematic.getState() == SchematicState.Checked) {
				String prefix = "§8[§6" + Bukkit.getOfflinePlayer(schematic.getOwner()).getName() + "§8] §b";
				FancyMessage wargearMessage = new FancyMessage("").then("§9#" + schematic.getId() + " " + prefix + schematic.getName()).tooltip("§e Hier klicken").command("/AutoWGK schematic load " + schematic.getId());
				message.addLine(wargearMessage);
			}
		}
		new Messenger().addPlayer(player).setMessage(message).send();
		
	}
	
	@Command(name = "AutoWGK.schematic.load", usage = "/AutoWGK", permission = "autowgk.schematic.load", aliases = { "awgk.schem.load", "awgk.schematic.load", "Autowgk.schem.load" }, inGameOnly=true)
	public void autowgkSchematicLoad(CommandArgs args) {
		
		Player player = args.getPlayer();
		if (args.getArgs().length != 1) {
			player.sendMessage(plugin.prefix + "Du musst eine Schematic angeben!");
			return;
		}

		Team team = this.plugin.getUtil().getTeamByPlayer(player);
		AutoArena arena = team.getAutoArena();
		Team otherTeam = null;
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
		Schematic schematic = this.repo.getSchematicByID(id);
		String direction = schematic.getName().substring(schematic.getName().length() - 2, schematic.getName().length());
		
		Direction northSouth = Direction.South;
		if (direction.equalsIgnoreCase("_n")) {
			northSouth = Direction.North;
		} else if (!direction.equalsIgnoreCase("_s")) {
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
			team.startGame();
		}
	}
}
