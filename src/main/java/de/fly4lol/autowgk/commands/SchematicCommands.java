package de.fly4lol.autowgk.commands;

import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.AutoArenaMode;
import de.fly4lol.autowgk.util.Config;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;
import de.pro_crafting.wg.arena.Arena;

public class SchematicCommands {
	private Main plugin;
	private Config config;
	
	public SchematicCommands(Main plugin){
		this.plugin = plugin;
		this.config = plugin.getAutoWGKConfig();
	}
	
	@Command(name = "AutoWGK.schematic", usage = "/AutoWGK", permission = "autowgk.schematic" , aliases = {"awgk.schem"  , "AutoWGK.schem" })
	public void autowgkSchematic(CommandArgs args) {
		Player player = args.getPlayer();
		player.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands.");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic load");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic list");
		player.sendMessage(this.plugin.prefix + "§eNutze: /AutoWgk schematic public");
	}
	
	


	@Command(name = "AutoWGK.schematic.private", usage = "/AutoWGK", permission = "autowgk.schematic.private" , aliases = {"awgk.create" , "awgk.arena.create"})
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
		
		config.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §e" + arena.getName()+ " §3erstellt!");
	}
}
