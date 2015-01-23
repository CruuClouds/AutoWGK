package de.fly4lol.autowgk.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.util.Config;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.arena.Arena;

public class ArenaCommands {
	private Main plugin;
	private Config config;
	public ArenaCommands(Main plugin){
		this.plugin = plugin;
		plugin.getAutoWGKConfig();
	}
	
	@Command(name = "AutoWGK.arena", usage = "/AutoWGK", permission = "autowgk.arena" , aliases = {"awgk.arena"})
	public void autowgkArena(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage("§2Nutze einen der folgenden Commands.");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena Create");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena addTeam");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena setMode");
	}
	
	@Command(name = "AutoWGK.arena.create", usage = "/AutoWGK", permission = "autowgk.arena.create" , aliases = {"awgk.create" , "awgk.arena.create"})
	public boolean autowgkArenaCreate(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie Existiert nicht!");
			return false;
		}
		
		config.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §6" + arena.getName()+ " §2Erstellt!");
		return true;
		
	}
	
	@Command(name = "AutoWGK.arena.addTeam", usage = "/AutoWGK", permission = "autowgk.arena.addTeam" , aliases = {"awgk.addTeam" , "awgk.arena.addTeam"})
	public boolean autowgkArenaAddteam(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie Existiert nicht!");
			return false;
		}
		
		if(args.getArgs().length != 2){
			player.sendMessage(plugin.prefix + "Nutze: AutoWGK arena addTeam <Team1/Team2> <North/South>");
			return false;
		}
		
		String team = args.getArgs()[0];
		String direction = args.getArgs()[1];
		
		if(!(team.equalsIgnoreCase("team1")) || !(team.equalsIgnoreCase("team2")) || !(team.equalsIgnoreCase( "1")) || !(team.equalsIgnoreCase("2"))){
			player.sendMessage(plugin.prefix + "Nutze: AutoWGK arena addTeam <Team1/Team2> <North/South>");
			return false;
		}
		

		if(!(direction.equalsIgnoreCase("south")) || !(direction.equalsIgnoreCase("north")) || !(direction.equalsIgnoreCase("s")) || !(direction.equalsIgnoreCase("n"))){
			player.sendMessage(plugin.prefix + "Nutze: AutoWGK arena addTeam <Team1/Team2> <North/South>");
			return false;
		}
		
		boolean north = false;
		
		if(direction.equalsIgnoreCase("north") ||  (direction.equalsIgnoreCase("n") )){
			north = true;
		}
		
		boolean team1 = false;
		
		if(team.equalsIgnoreCase("team1") || (team.equalsIgnoreCase( "1"))){
			team1 = true;
		}
		
		Location loc = player.getLocation();
		config.addTeam( arena.getName() , team1, north, loc.getBlockX() , loc.getBlockY() , loc.getBlockZ() ,  loc.getWorld().getName() );
		player.sendMessage(plugin.prefix + "Du hast das §6" + team + " §2Hinzugefügt");
		return true;
		
	}
	
	@Command(name = "AutoWGK.arena.setmode", usage = "/AutoWGK", permission = "autowgk.arena.setmode" , aliases = {"awgk.arena.setmode"})
	public boolean autowgkArenaSetmode(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		AutoArenaMode mode = AutoArenaMode.NORMAL;
		if(args.getArgs().length !=  1){
			player.sendMessage( plugin.prefix + "Nutze: /autowgk arena setMode <DISABLED/NORMAL>");
			return false;
		}
		
		String stringMode = args.getArgs()[0];
		
		if(stringMode.equalsIgnoreCase("normal")){
			mode = AutoArenaMode.NORMAL;
			plugin.loadedArenen.get( arena.getName() ).setMode( mode );
			player.sendMessage(plugin.prefix + "Du hast den modus der Arena Geändert:§6 " + mode.toString());
			return true;
		}
		
		if(stringMode.equalsIgnoreCase("closed") || stringMode.equalsIgnoreCase("off") || stringMode.equalsIgnoreCase("Disabled")){
			mode = AutoArenaMode.DISABLED;
			plugin.loadedArenen.get( arena.getName() ).setMode( mode );
			player.sendMessage(plugin.prefix + "Du hast den modus der Arena Geändert:§6 " + mode.toString());
			return true;
		}
		
		player.sendMessage( plugin.prefix + "Nutze: /autowgk arena setMode <DISABLED/NORMAL>");
		return false;
	}
	
	

}
