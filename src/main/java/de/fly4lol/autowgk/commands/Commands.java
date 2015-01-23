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
		sender.sendMessage("§2Nutze einen der folgenden Commands.");
		sender.sendMessage(plugin.prefix + "/AutoWGK Sign");
		sender.sendMessage(plugin.prefix + "/AutoWGK Arena");
		sender.sendMessage(plugin.prefix + "/AutoWGK ShowLast");
		sender.sendMessage(plugin.prefix + "/AutoWGK Schematic");
	}
	
	@Command(name = "AutoWGK.showlast", usage = "/AutoWGK", permission = "autowgk.showlast" , aliases = {"awgk.last" , "AutoWGK.last" })
	public void autowgkShowlast(CommandArgs args) {
		Player player = args.getPlayer();
		new Messenger().addPlayer( player ).sendLast();
	}
}
