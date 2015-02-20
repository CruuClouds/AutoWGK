package de.fly4lol.autowgk.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;
import de.fly4lol.messenger.Messenger;
import de.pro_crafting.commandframework.Command;
import de.pro_crafting.commandframework.CommandArgs;

public class Commands {
	private Main plugin;
	
	public Commands(Main plugin){
		this.plugin = plugin;
	}
	
	@Command(name = "AutoWGK", usage = "/AutoWGK", permission = "autowgk.use" , aliases = {"awgk"})
	public void autowgk(CommandArgs args) {
		CommandSender sender = args.getSender();
		sender.sendMessage(plugin.prefix + "Nutze einen der folgenden Commands:");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK sign");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK arena");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK showlast");
		sender.sendMessage(plugin.prefix + "§e/AutoWGK schematic");
	}
	
	@Command(name = "AutoWGK.showlast", usage = "/AutoWGK", permission = "autowgk.showlast" , aliases = {"awgk.last" , "AutoWGK.last" }, inGameOnly=true)
	public void autowgkShowlast(CommandArgs args) {
		Player player = args.getPlayer();
		Messenger.getInstance().sendLast(player);
	}
}
