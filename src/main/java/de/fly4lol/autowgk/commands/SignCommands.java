package de.fly4lol.autowgk.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.util.MySQLMethods;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;

public class SignCommands {
	private Main plugin;
	
	public SignCommands(Main plugin){
		this.plugin = plugin;
	}
	
	@Command(name = "AutoWGK.sign", usage = "/AutoWGK", permission = "autowgk.sign" , aliases = {"awgk.sign"})
	public void autowgkSign(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands:");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK sign add");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK sign remove");
	}
	
	@Command(name = "AutoWGK.sign.add", usage = "/AutoWGK", permission = "autowgk.sign.add" , aliases = {"awgk.sign.add"}, inGameOnly=true)
	public void autowgkSignAdd(CommandArgs args) {
		Player player = args.getPlayer();
		plugin.addSign.add(player);
		player.sendMessage(plugin.prefix + "Klicke auf ein Schild um dieses zu adden.");
	}
	
	@Command(name = "AutoWGK.sign.remove", usage = "/AutoWGK", permission = "autowgk.sign.remove" , aliases = {"awgk.sign.remove"}, inGameOnly=true)
	public void autowgkSignRemove(CommandArgs args) {
		Player player = args.getPlayer();
		plugin.removeSign.add(player);
		player.sendMessage(plugin.prefix + "Klicke auf ein Schild um dieses zu removen.");
		
	}
	
	@Command(name = "AutoWGK.sign.remove.world", usage = "/AutoWGK", permission = "autowgk.sign.remove.world" , aliases = {"awgk.sign.remove.world"}, inGameOnly=true)
	public void autowgkSignRemoveWorld(CommandArgs args) {
		Player player = args.getPlayer();
		MySQLMethods sql = plugin.getSQL();
		
		if(args.getArgs().length != 1){
			player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK sign remove world <worldname>");
			return;
		}
		
		if(Bukkit.getWorld(args.getArgs()[0]) == null){
			player.sendMessage(plugin.prefix + "Diese Welt existiert nicht!");
			return;
		}
		
		List<Location> signs = new ArrayList<Location>();
		signs = sql.getSignsByWorld(args.getArgs()[0]);
		for (Location sign : signs){
			sql.removeSign(sign);
		}
		
		return;
	}
}
