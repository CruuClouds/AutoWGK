package de.fly4lol.autowgk;

import java.util.ArrayList;
import java.util.List;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.messagemanager.Message;
import de.fly4lol.autowgk.messagemanager.Messenger;
import de.fly4lol.autowgk.util.Config;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.fly4lol.autowgk.util.Schematic;
import de.fly4lol.autowgk.util.SchematicState;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.arena.Arena;

public class Commands {
	private Main plugin;
	private MySQLMethods sql;
	private Config config;

	public Commands(Main plugin) {
		this.plugin = plugin;
		this.config = plugin.getAutoWGKConfig();
		this.sql = plugin.getSQL();
	}

	@Command(name = "AutoWGK", usage = "/AutoWGK", permission = "autowgk.use" , aliases = {"awgk"})
	public void autowgk(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage("§2Nutze einen der folgenden Commands.");
		sender.sendMessage(plugin.prefix + "/AutoWGK Sign");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena");
		sender.sendMessage(plugin.prefix + "/AutoWGK ShowLast");
		sender.sendMessage(plugin.prefix + "/AutoWGK Schematic");
	}

	/*
	 * 
	 * Command for Sign Zeugs
	 * 
	 */

	@Command(name = "AutoWGK.sign", usage = "/AutoWGK", permission = "autowgk.sign" , aliases = {"awgk.sign"})
	public void autowgkSign(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage(plugin.prefix + "/AutoWGK sign add");
		sender.sendMessage(plugin.prefix + "/AutoWGK sign remove");
	}

	@Command(name = "AutoWGK.sign.add", usage = "/AutoWGK", permission = "autowgk.sign.add" , aliases = {"awgk.sign.add"})
	public void autowgkSignAdd(CommandArgs args) {
		Player player = args.getPlayer();
		plugin.addSign.add(player);
		player.sendMessage(plugin.prefix + "Klicke auf ein Schild um dieses zu adden.");
	}

	@Command(name = "AutoWGK.sign.remove", usage = "/AutoWGK", permission = "autowgk.sign.remove" , aliases = {"awgk.sign.remove"})
	public void autowgkSignRemove(CommandArgs args) {
		Player player = args.getPlayer();
		plugin.removeSign.add(player);
		player.sendMessage(plugin.prefix + "Klicke auf ein Schild um dieses zu removen.");
	}

	@Command(name = "AutoWGK.sign.remove.world", usage = "/AutoWGK", permission = "autowgk.sign.remove.world" , aliases = {"awgk.sign.remove.world"})
	public void autowgkSignRemoveWorld(CommandArgs args) {
		Player player = args.getPlayer();
		if(args.getArgs().length == 1){
			if(Bukkit.getWorld(args.getArgs()[0]) != null){
				List<Location> signs = new ArrayList<Location>();
				signs = sql.getSignsByWorld(args.getArgs()[0]);
				for (Location sign : signs){
					sql.removeSign(sign);
				}
				player.sendMessage(plugin.prefix + "Du hast alle schilder in der Welt §6" + args.getArgs()[0] + " §2Gelöscht!");
			} else {
				player.sendMessage(plugin.prefix + "Diese Welt existiert nicht !");
			}
		} else {
			player.sendMessage(plugin.prefix + "Nutze: /AutoWGK sign remove world <worldname>");
		}

	}
	
	/*
	 * 
	 *  Arena Commands
	 * 
	 */
	
	@Command(name = "AutoWGK.arena", usage = "/AutoWGK", permission = "autowgk.arena" , aliases = {"awgk.arena"})
	public void autowgkArena(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage("§2Nutze einen der folgenden Commands.");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena Create");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena addTeam");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena setMode");
	}
	
	@Command(name = "AutoWGK.arena.create", usage = "/AutoWGK", permission = "autowgk.arena.create" , aliases = {"awgk.arena.create"})
	public void autowgkArenaCreate(CommandArgs args) {
		CommandSender sender = args.getSender();
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena != null){
			 config.creatArena( arena.getName());
			 player.sendMessage(plugin.prefix + "Du hast die Arena §6" + arena.getName()+ " §2Erstellt!");
		} else {
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena ");
		}
	}
	
	@Command(name = "AutoWGK.arena.addteam", usage = "/AutoWGK", permission = "autowgk.arena.addteam" , aliases = {"awgk.arena.addteam"})
	public void autowgkArenaAddteam(CommandArgs args) {
		CommandSender sender = args.getSender();
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		String team = args.getArgs()[0];
		String direction = args.getArgs()[1];
		Block block = player.getLocation().getBlock();
		boolean team1 = false;
		boolean north = false;
		if(args.getArgs().length == 2){
			if(( team.equalsIgnoreCase("team1") || team.equalsIgnoreCase("team2")) && (direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("south"))){
				if(team.equalsIgnoreCase("team1")){
					team1 = true;
				}
				if(direction.equalsIgnoreCase("norht")){
					north = true;
				}
				config.addTeam( arena.getName() , team1, north, block.getX(), block.getY(), block.getZ(), block.getWorld().getName());
				player.sendMessage(plugin.prefix + "Du hast das §6" + team + " §2Hinzugefügt");
			
			} else {
				player.sendMessage(plugin.prefix + "Nutze: /autowgk arena addteam team1/team2 nort/south");
			}
		} else {
			player.sendMessage(plugin.prefix + "Nutze: /autowgk arena addteam team1/team2 nort/south");
		}
		
	}
	
	@Command(name = "AutoWGK.arena.setmode", usage = "/AutoWGK", permission = "autowgk.arena.setmode" , aliases = {"awgk.arena.setmode"})
	public void autowgkArenaSetmode(CommandArgs args) {
		CommandSender sender = args.getSender();
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		String stringMode = args.getArgs()[0];
		if(args.getArgs().length ==  1){
			if(arena != null){
				if(stringMode.equalsIgnoreCase("normal") || stringMode.equalsIgnoreCase("disabled")){
					AutoArenaMode mode = AutoArenaMode.valueOf(stringMode);
					config.setMode( arena.getName(), mode);
					player.sendMessage(plugin.prefix + "Du hast den Mode der arena zu §6" + mode.toString() + " §2Geändert");
				} else {
					player.sendMessage(plugin.prefix + "Nutze: /AutoWGK Arena setmode normal/disabled ");
				}
			} else {
				player.sendMessage(plugin.prefix + "Du stehst in keiner Arena ");
			}
		} else {
			player.sendMessage(plugin.prefix + "Nutze: /AutoWGK Arena setmode normal/disabled ");
		}
		
	}
	
	/*
	 * 
	 * Show Last command
	 * 
	 */
	
	@Command(name = "AutoWGK.showlast", usage = "/AutoWGK", permission = "autowgk.showlast" , aliases = {"awgk.last" , "AutoWGK.last" })
	public void autowgkShowlast(CommandArgs args) {
		Player player = args.getPlayer();
		new Messenger().setPlayer( player ).sendLast();
	}
	
	/*
	 * 
	 * Schematic commands
	 * 
	 */
	
	@Command(name = "AutoWGK.schematic", usage = "/AutoWGK", permission = "autowgk.schematic" , aliases = {"awgk.schem"  , "AutoWGK.schem" })
	public void autowgkSchematic(CommandArgs args) {
		Player player = args.getPlayer();
		player.sendMessage("§2Nutze einen der folgenden Commands.");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic load");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic list");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic public");
	}
	
	@Command(name = "AutoWGK.schematic.list", usage = "/AutoWGK", permission = "autowgk.schematic.list" , aliases = {"awgk.schem.list" , "awgk.schematic.list" , "Autowgk.schem.list"})
	public void autowgkSchematicList(CommandArgs args) {
		Player player = args.getPlayer();
		if(args.getArgs().length == 0 ){
			List<Schematic> schematics = this.sql.getSchematisByOwner( player );
			Message message = new Message();
			message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
			for(Schematic schematic : schematics ){
				if(schematic.isWarGear()){
					String prefix = "§8[§6Private§8] §b";
					if(schematic.isPublic()){
						prefix = "§8[§6Public§8] §b";
					}
					FancyMessage wargearMessage = new FancyMessage("")
					.then("§9#" + schematic.getId() + " " + prefix + schematic.getName() )
					.tooltip("§5Klicke")
					.command("/AutoWGK schematic load " + schematic.getId());
					message.addLine(wargearMessage);
				}
			}
			new Messenger().setPlayer( player ).setMessage( message).send();
		} else if(args.getArgs().length == 1){
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args.getArgs()[0]);
			List<Schematic> schematics = this.sql.getSchematisByOwner( offlinePlayer );
			Message message = new Message();
			message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
			for(Schematic schematic : schematics ){
				if(schematic.isWarGear()){
					if(schematic.getState().equals(SchematicState.Checked)){
						String prefix = "§8[§6Private§8] §b";
						if(schematic.isPublic()){
							prefix = "§8[§6Public§8] §b";
						}
						FancyMessage wargearMessage = new FancyMessage("")
						.then("§9#" + schematic.getId() + " " + prefix + schematic.getName() )
						.tooltip("§5Klicke")
						.command("/AutoWGK schematic load " + schematic.getId());
						message.addLine(wargearMessage);
					}
				}
			}
			new Messenger().setPlayer( player ).setMessage( message).send();
		}
	}
	
	@Command(name = "AutoWGK.schematic.public", usage = "/AutoWGK", permission = "autowgk.schematic.public" , aliases = {"awgk.schem.public" , "awgk.schematic.public" , "Autowgk.schem.public"})
	public void autowgkSchematicPublic(CommandArgs args) {
		Player player = args.getPlayer();
		List<Schematic> schematics = this.sql.getPublicSchematics();
		Message message = new Message();
		message.addLine(this.plugin.prefix + "Klicke auf ein WarGear um dies zu laden!");
		for(Schematic schematic : schematics ){
			if(schematic.isWarGear()){
				if(schematic.getState().equals(SchematicState.Checked)){
					String prefix = "§8[§6" + Bukkit.getOfflinePlayer( schematic.getOwner()).getName()+ "§8] §b";
					FancyMessage wargearMessage = new FancyMessage("")
					.then("§9#" + schematic.getId() + " " + prefix + schematic.getName() )
					.tooltip("§5Klicke")
					.command("/AutoWGK schematic load " + schematic.getId());
					message.addLine(wargearMessage);
				}
			}
		}
		new Messenger().setPlayer( player ).setMessage( message).send();
	
	}
	
	
	
	




}
