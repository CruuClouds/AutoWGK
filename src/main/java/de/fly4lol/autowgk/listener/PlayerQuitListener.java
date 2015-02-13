package de.fly4lol.autowgk.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.fightmanager.Team;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.GroupSide;
import de.pro_crafting.wg.group.PlayerRole;

public class PlayerQuitListener implements Listener{
	private Main plugin;
	
	public PlayerQuitListener(Main plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void playerQuitHandler(PlayerQuitEvent event) {
		Team team = plugin.getUtil().getTeamByPlayer( event.getPlayer() );
		Arena wgkArena = null;
		if(team != null){
			wgkArena = team.getAutoArena().getWgkArena();
			if(team.getAutoArena().getTeam1() == team){
				team.getAutoArena().setTeam1( null);
				return;
			}
			
			if(team.getAutoArena().getTeam2() == team){
				team.getAutoArena().setTeam2( null);
				return;
			}
			
		}
		if(team != null && team.getAutoArena().getWgkArena().getState() == State.Setup){
			if( team != null ) {	
				if(wgkArena.getState() == State.Idle){
					if(team.getRole() == PlayerRole.Team1){
						team.getAutoArena().setTeam1( team);
					} else if(team.getRole() == PlayerRole.Team2){
						team.getAutoArena().setTeam2( team );
					}
				} else if(wgkArena.getState() == State.Setup){
					GroupSide side = GroupSide.Team2;
					if(team.getAutoArena().getTeam1() == team){
						side = GroupSide.Team1;
						team.getAutoArena().setTeam1( null);
						
					} else {
						team.getAutoArena().setTeam2( null );
					}
					if(wgkArena != null){
						team.getAutoArena().getWgkArena().getReseter().cleanSide( side );
						this.plugin.getRepo().getWarGear().getScoreboard().removeTeamMember(wgkArena  , wgkArena.getGroupManager().getGroupMember( event.getPlayer()), wgkArena.getGroupManager().getRole( event.getPlayer()));
						wgkArena.getGroupManager().getGroupOfPlayer( event.getPlayer() ).remove( event.getPlayer() );
					}	
				}			
			}
		}
	}
}
