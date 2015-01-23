package de.fly4lol.autowgk.commands;

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
		sender.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands:");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena create");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena addteam");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena setmode");
	}
	
	@Command(name = "AutoWGK.arena.create", usage = "/AutoWGK", permission = "autowgk.arena.create" , aliases = {"awgk.create" , "awgk.arena.create"})
	public void autowgkArenaCreate(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		config.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §e" + arena.getName()+ " §3erstellt!");
		return;
		
	}
	
	@Command(name = "AutoWGK.arena.addTeam", usage = "/AutoWGK", permission = "autowgk.arena.addTeam" , aliases = {"awgk.addTeam" , "awgk.arena.addTeam"})
	public void autowgkArenaAddteam(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		if(args.getArgs().length != 2){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/nouth>");
			return;
		}
		
		String team = args.getArgs()[0];
		String direction = args.getArgs()[1];
		
		if(!(team.equalsIgnoreCase("team1")) || !(team.equalsIgnoreCase("team2")) || !(team.equalsIgnoreCase( "1")) || !(team.equalsIgnoreCase("2"))){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/nouth>");
			return;
		}
		

		if(!(direction.equalsIgnoreCase("south")) || !(direction.equalsIgnoreCase("north")) || !(direction.equalsIgnoreCase("s")) || !(direction.equalsIgnoreCase("n"))){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/nouth>");
			return;
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
		player.sendMessage(plugin.prefix + "Du hast das §e" + team + " §3hinzugefügt");
		return;
		
	}
	
	@Command(name = "AutoWGK.arena.setmode", usage = "/AutoWGK", permission = "autowgk.arena.setmode" , aliases = {"awgk.arena.setmode"})
	public void autowgkArenaSetmode(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		AutoArenaMode mode = AutoArenaMode.NORMAL;
		if(args.getArgs().length !=  1){
			player.sendMessage( plugin.prefix + "Nutze: §e/AutoWGK arena setmode <DISABLED/NORMAL>");
			return;
		}
		
		String stringMode = args.getArgs()[0];
		
		if(stringMode.equalsIgnoreCase("normal")){
			mode = AutoArenaMode.NORMAL;
			plugin.loadedArenen.get( arena.getName() ).setMode( mode );
			player.sendMessage(plugin.prefix + "Du hast den Modus der Arena in§e " + mode.toString() + " §3geändert!");
			return;
		}
		
		if(stringMode.equalsIgnoreCase("closed") || stringMode.equalsIgnoreCase("off") || stringMode.equalsIgnoreCase("disabled")){
			mode = AutoArenaMode.DISABLED;
			plugin.loadedArenen.get( arena.getName() ).setMode( mode );
			player.sendMessage(plugin.prefix + "Du hast den modus der Arena in §e" + mode.toString() + " §3geändert!");
			return;
		}
		
		player.sendMessage( plugin.prefix + "Nutze: §e/AutoWGK arena setmode <DISABLED/NORMAL>");
		return;
	}
	
	

}
