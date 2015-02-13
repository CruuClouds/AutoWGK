package de.fly4lol.autowgk.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.Team;
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
			Team team = plugin.getUtil().getTeamByPlayer(event.getPlayer());
			if(team != null && team.getAutoArena().getWgkArena().getState() == State.Setup){
				GroupSide side = GroupSide.Team2;
				if(team.getRole() == PlayerRole.Team1){
					side = GroupSide.Team1;
					team.getAutoArena().setTeam1( null);
					
				} else {
					team.getAutoArena().setTeam2( null );
				}
				team.getAutoArena().getWgkArena().getReseter().cleanSide( side );
			}
		}
	}
	

}
