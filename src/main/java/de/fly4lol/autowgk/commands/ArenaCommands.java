package de.fly4lol.autowgk.commands;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.Repository;
import de.fly4lol.autowgk.fightmanager.AutoArena;
import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.fightmanager.Direction;
import de.fly4lol.messenger.Message;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
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
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		repo.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §e" + arena.getName()+ " §3erstellt!");
		return;
		
	}
	
	@Command(name = "AutoWGK.arena.addTeam", usage = "/AutoWGK", permission = "autowgk.arena.addTeam" , aliases = {"awgk.addTeam" , "awgk.arena.addTeam"}, inGameOnly=true)
	public void autowgkArenaAddteam(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie existiert nicht!");
			return;
		}
		
		if(args.getArgs().length != 2){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK arena addteam <team1/team2> <north/south>");
			return;
		}
		
		String team = args.getArgs()[0];

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
		return;
		
	}
	
	@Command(name = "AutoWGK.arena.setmode", usage = "/AutoWGK", permission = "autowgk.arena.setmode" , aliases = {"awgk.arena.setmode"}, inGameOnly=true)
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
	
	public void autowgkArenaJoin(Player player){
		
		if(this.plugin.getUtil().getArenaAt( player.getLocation()) == null){
			player.sendMessage(plugin.prefix + "Die stehst in keiner Arena oder sie Existiert nicht!");
			return;
		}
		AutoArena autoArena = this.plugin.getUtil().getArenaAt( player.getLocation());
		
		if(!player.hasPermission("autowgk.arena.join")){
			player.sendMessage(plugin.noPerms);
			return;
		}
		
		if(autoArena.getMode() == AutoArenaMode.DISABLED){
			player.sendMessage(plugin.prefix + "Du kannst momentan nicht joinen!");
			return;
		}
		
		if(plugin.getUtil().getTeamByPlayer( player ) != null){
			player.sendMessage(plugin.prefix + "Du bist schon in einem Team!");
			return;
		}
		
		if(plugin.wg.getArenaManager().getGroup( player ).getArena() != null){
			player.sendMessage(plugin.prefix + "Du kannst hier Immoment nich Joinen!");
			return;
		}
		
		if(!autoArena.isJoinable()){
			player.sendMessage(plugin.prefix + "Beide Teams haben Bereits einen Leader!");
			return;
		}
		
		autoArena.joinArena( player );
		

		FancyMessage privateMessage = new FancyMessage( this.plugin.prefix)
		.then("§e§nPrivate")
		.tooltip("§eHier klicken")
		.command("/AutoWGK schematic list");
		
		FancyMessage publicMessage = new FancyMessage( this.plugin.prefix)
		.then("§e§nPublic")
		.tooltip("§eHier klicken")
		.command("/AutoWGK schematic public");
		
		Message message = new Message();
		message
		.addLine("")
		.addLine("")
		.addLine("            §eWähle Zwischen Public und Private ")
		.addLine("")
		.addLine(publicMessage)
		.addLine("")
		.addLine(privateMessage)
		.addLine("");
		
		new Messenger().setMessage(message).addPlayer( player ).send();
		
	}
}
