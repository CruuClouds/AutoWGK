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
		player.sendMessage("§2Nutze einen der folgenden Commands.");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic load");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic list");
		player.sendMessage(this.plugin.prefix + "Nutze: /AutoWgk Schematic public");
	}
	
	


	@Command(name = "AutoWGK.schematic.private", usage = "/AutoWGK", permission = "autowgk.schematic.private" , aliases = {"awgk.create" , "awgk.arena.create"})
	public boolean autowgkArenaCreate(CommandArgs args) {
		Player player = args.getPlayer();
		Arena arena = plugin.wg.getArenaManager().getArenaAt(player.getLocation());
		if(arena == null){
			player.sendMessage(plugin.prefix + "Du stehst in keiner Arena oder sie Existiert nicht!");
			return false;
		}
		
		if(args.getArgs().length >= 2){
			player.sendMessage(plugin.prefix + "Nutze: /AutoWGK schematic private");
			return false;
		}
		
		if(plugin.loadedArenen.get( arena.getName() ).getMode() == AutoArenaMode.DISABLED){
			player.sendMessage(plugin.prefix + "Du kannst immoment nicht in dieser Arena Spielen!");
			return false;
		}
		
		config.creatArena( arena.getName());
		player.sendMessage(plugin.prefix + "Du hast die Arena §6" + arena.getName()+ " §2Erstellt!");
		return true;
		
	}
}
