package de.fly4lol.autowgk.commands;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.Repository;
import de.fly4lol.autowgk.arena.AutoArena;
import de.fly4lol.autowgk.arena.AutoArenaMode;
import de.fly4lol.autowgk.arena.Direction;
import de.fly4lol.messenger.MessageHolder;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.Util;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.group.PlayerRole;

public class ArenaCommands {
	private Main plugin;
	private Repository repo;
	public ArenaCommands(Main plugin){
		this.plugin = plugin;
		this.repo = plugin.getRepo();
	}
	
	@Command(name = "AutoWGK.arena", usage = "/AutoWGK", permission = "autowgk.arena" , aliases = {"awgk.arena"})
	public void autowgkArena(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands:");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena create");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena addteam");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena setmode");
	}
	
	@Command(name = "AutoWGK.arena.create", usage = "/AutoWGK", permission = "autowgk.arena.create" , aliases = {"awgk.create" , "awgk.arena.create"}, inGameOnly=true)
	public void autowgkArenaCreate(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = repo.getWarGear().getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		repo.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §e" + arena.getName()+ " §3erstellt!");
	}
	
	@Command(name = "AutoWGK.arena.addTeam", usage = "/AutoWGK", permission = "autowgk.arena.addTeam" , aliases = {"awgk.addTeam" , "awgk.arena.addTeam"}, inGameOnly=true)
	public void autowgkArenaAddteam(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = repo.getWarGear().getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		if(args.length() != 2){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/south>");
			return;
		}
		
		String team = args.getArgs(0);

		if(!team.equalsIgnoreCase("team1")){
			if(!team.equalsIgnoreCase("team2")){
				if(!team.equalsIgnoreCase("1")){
					if(!team.equalsIgnoreCase("2")){
						player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/south>");
						return;
					}
				}
				
			}
			
		}
		
		Direction direction = Direction.parse(args.getArgs(1));
		if (direction == null) {
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/south>");
			return;
		}
		
		boolean team1 = false;
		
		if(team.equalsIgnoreCase("team1") || (team.equalsIgnoreCase( "1"))){
			team1 = true;
		}
		
		Location loc = player.getLocation();
		repo.addTeam( arena.getName() , team1 ? PlayerRole.Team1 : PlayerRole.Team2, direction, loc.getBlockX() , loc.getBlockY() , loc.getBlockZ() ,  loc.getWorld().getName() );
		player.sendMessage(plugin.prefix + "Du hast das §e" + team + " §3hinzugefügt");
	}
	
	@Command(name = "AutoWGK.arena.info", usage = "/AutoWGK arena info", permission = "autowgk.arena.info")
	public void autowgkArenaInfo(CommandArgs args) {
		Arena arena = Util.getArenaFromSender(this.plugin.getRepo().getWarGear(), args.getSender(), args.getArgs());
		if (arena == null) {
			args.getSender().sendMessage("§cDu stehst in keiner Arena, oder Sie existiert nicht.");
			return;
		}
		
		AutoArena autoArena = this.plugin.getArenenManager().getArena(arena.getName());
		CommandSender sender = args.getSender();
		sender.sendMessage("§a---Arena Info---");
		sender.sendMessage("§7Arena Name: §B" + arena.getName());
		String team1 = autoArena.getTeam1() == null ? "-" : autoArena.getTeam1().getLeader().getName();
		String team2 = autoArena.getTeam2() == null ? "-" : autoArena.getTeam2().getLeader().getName();
		sender.sendMessage("§7Team1 Leader: §B" + team1);
		sender.sendMessage("§7Team2 Leader: §B" + team2);
	}
	
	@Command(name = "AutoWGK.arena.setmode", usage = "/AutoWGK", permission = "autowgk.arena.setmode" , aliases = {"awgk.arena.setmode"}, inGameOnly=true)
	public void autowgkArenaSetmode(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.getRepo().getWarGear().getArenaManager().getArenaAt(player.getLocation());
		if(args.length() !=  1){
			player.sendMessage( plugin.prefix + "Nutze: §e/AutoWGK arena setmode <DISABLED/NORMAL>");
			return;
		}
		
		AutoArenaMode mode = AutoArenaMode.parse(args.getArgs(0));
		
		if (mode == null) {
			player.sendMessage( plugin.prefix + "Nutze: §e/AutoWGK arena setmode <DISABLED/NORMAL>");		return;
		}
		
		plugin.getArenenManager().getArena(arena.getName()).setMode( mode );
		player.sendMessage(plugin.prefix + "Du hast den Modus der Arena in§e " + mode.toString() + " §3geändert!");	
	}
	
	public void autowgkArenaJoin(Player player){
		AutoArena arena = this.plugin.getArenenManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Die stehst in keiner Arena oder sie Existiert nicht!");
			return;
		}
		if(!player.hasPermission("autowgk.arena.join")){
			player.sendMessage(plugin.noPerms);
			return;
		}
		
		if(arena.getMode() == AutoArenaMode.DISABLED){
			player.sendMessage(plugin.prefix + "Du kannst momentan nicht joinen!");
			return;
		}
		
		if(plugin.getArenenManager().getTeamByPlayer( player ) != null){
			player.sendMessage(plugin.prefix + "Du bist schon in einem Team!");
			return;
		}
		
		if(plugin.getRepo().getWarGear().getArenaManager().getGroup( player ).getArena() != null){
			player.sendMessage(plugin.prefix + "Du kannst hier Immoment nich Joinen!");
			return;
		}
		
		if(!arena.isJoinable()){
			player.sendMessage(plugin.prefix + "Beide Teams haben Bereits einen Leader!");
			return;
		}
		
		arena.joinArena( player );
		

		FancyMessage privateMessage = new FancyMessage( this.plugin.prefix)
		.then("§e§nPrivate")
		.tooltip("§eHier klicken")
		.command("/AutoWGK schematic list");
		
		FancyMessage publicMessage = new FancyMessage( this.plugin.prefix)
		.then("§e§nPublic")
		.tooltip("§eHier klicken")
		.command("/AutoWGK schematic public");
		
		MessageHolder msg = new MessageHolder();
		msg.addLine("")
		.addLine("")
		.addLine("            §eWähle Zwischen Public und Private ")
		.addLine("")
		.addLine(publicMessage)
		.addLine("")
		.addLine(privateMessage)
		.addLine("");
		
		Messenger.getInstance().send(player, msg);
		
	}
}
