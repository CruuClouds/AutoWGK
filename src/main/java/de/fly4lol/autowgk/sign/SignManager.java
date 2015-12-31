package de.fly4lol.autowgk.sign;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.Repository;

public class SignManager implements Listener {
	private Map<UUID, PlayerSignType> signPlayers;
	private Main plugin;
	private Repository repo;
	
	public SignManager(Main plugin) {
		this.plugin = plugin;
		this.repo = plugin.getRepo();
		this.signPlayers = new HashMap<UUID, PlayerSignType>();
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void playerInteractHandler(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (!event.hasBlock()) {
			return;
		}
		Material type = event.getClickedBlock().getType();
		if(!type.equals(Material.SIGN ) && !type.equals(Material.SIGN_POST) && !type.equals(Material.WALL_SIGN)){
			return;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if(this.plugin.getRepo().existSignAtLocation(event.getClickedBlock().getLocation())){
				player.sendMessage(plugin.prefix + "Du darfst dieses Schild nicht zerstören!");
				if(player.hasPermission("autowgk.sign.remove")){
					player.sendMessage(plugin.prefix + "Nutze: §e/AutoWGK sign remove");
				}
			}
			return;
		}
		
		Location signLoc = event.getClickedBlock().getLocation();
		
		if(repo.existSignAtLocation( signLoc )){
			if(repo.getSignType( signLoc).equals( SignType.ARENAJOIN)){
				this.plugin.getArenaCommands().autowgkArenaJoin( player );
			}
		} else if(!this.signPlayers.containsKey(player.getUniqueId())){
			return;
		}
		
		PlayerSignType signType = this.signPlayers.get(player.getUniqueId());
		if (signType == null) {
			return;
		}
		if (signType == PlayerSignType.Add) {
			if(!repo.existSignAtLocation(signLoc)){
				BlockState state = signLoc.getBlock().getState();
				Sign sign = (Sign) state;
				if(sign.getLine( 0 ).equalsIgnoreCase("[ArenaInfo]")){
					sign.setLine( 0 , "§6Arena Info§6");
					sign.setLine( 1, "§9" + sign.getLine( 1) + "§9");
					sign.setLine( 2, "§2 Loading... ");
					sign.update();
					repo.addSign(player, signLoc, SignType.ARENAINFO);
					player.sendMessage(plugin.prefix + "Du hast das Schild hinzugefügt!");
				} else if(sign.getLine( 0 ).equalsIgnoreCase("[ArenaJoin]")) {
					sign.setLine( 0 , "§6Arena Join§6" );
					sign.setLine( 1, "§9Klicke hier ");
					sign.setLine( 2, "§9um zu Joinen");
					sign.update();
					repo.addSign( player , signLoc, SignType.ARENAJOIN);
					player.sendMessage(plugin.prefix + "Du hast das Schild hinzugefügt!");
				} else {
					player.sendMessage( plugin.prefix + "Dies ist kein gültiges Schild!");
				}
			} else {
				player.sendMessage(plugin.prefix + "Dieses Schild ist bereits eingetragen!");
			}
		} else {
			if(repo.existSignAtLocation(signLoc)){
				repo.removeSign(signLoc);
				signLoc.getBlock().setType(Material.AIR);
				player.sendMessage(plugin.prefix + "Du hast das Schild entfernt!");
			} else {
				player.sendMessage(plugin.prefix + "Dieses Schild ist nicht eingetragen!");
			}
		}
		this.signPlayers.remove(player.getUniqueId());
		event.setCancelled(true);
	}
	
	public void add(Player player, PlayerSignType type) {
		this.signPlayers.put(player.getUniqueId(), type);
	}
}
