package de.fly4lol.autowgk.listener;

import java.util.Arrays;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.arena.Team;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.GroupSide;
import de.pro_crafting.wg.group.PlayerRole;

public class PlayerCommandPreprocessListener implements Listener{
	private Main plugin;
	
	public PlayerCommandPreprocessListener(Main plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerCommandHandler(PlayerCommandPreprocessEvent event) {
		if(event.getMessage().equalsIgnoreCase("/wgk team leave")){
			Team team = plugin.getArenenManager().getTeamByPlayer(event.getPlayer());
			if (team == null) {
				return;
			}
			
			Arena wgkArena = team.getAutoArena().getWgkArena();
			
			if(wgkArena.getState() == State.Idle || wgkArena.getState() == State.Setup){
				if(team.getRole() == PlayerRole.Team1){
					team.getAutoArena().setTeam1(null);
				} else if(team.getRole() == PlayerRole.Team2){
					team.getAutoArena().setTeam2(null);
				}
			} 
			if (wgkArena.getState() == State.Setup) {
				GroupSide side = team.getRole() == PlayerRole.Team1 ? GroupSide.Team1 : GroupSide.Team2;
				team.getAutoArena().getWgkArena().getReseter().cleanSide(side);	
				this.plugin.getRepo().getWarGear().getScoreboard().removeTeamMember(wgkArena, wgkArena.getGroupManager().getGroupMember(event.getPlayer()), team.getRole());
				wgkArena.getGroupManager().getGroupOfPlayer(event.getPlayer()).remove(event.getPlayer());
				
			}
		}
	}
	

}
