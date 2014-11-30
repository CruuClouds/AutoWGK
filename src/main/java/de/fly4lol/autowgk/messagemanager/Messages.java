package de.fly4lol.autowgk.messagemanager;

import mkremins.fanciful.FancyMessage;

import org.bukkit.entity.Player;

import de.fly4lol.autowgk.Main;

public class Messages {
	private static Main plugin;
	
	public Messages(Main plugin){
		this.plugin = plugin;
	}
	
	public static void sendAutoJoinMessage(Player player){
		player.sendMessage("");
		player.sendMessage("");
		player.sendMessage("            §2Wähle Zwischen Public und Private ");
		player.sendMessage("");
		new FancyMessage( plugin.prefix )
		.then("§2§nPublic")
		.tooltip("§5Klicke")
		.command("/AutoWGK public")
		.send( player );
		player.sendMessage("");
		new FancyMessage( plugin.prefix)
		.then("§9§nPrivate")
		.tooltip("§5Klicke")
		.command("/AutoWGK private")
		.send( player );
		player.sendMessage("");
	}
}
