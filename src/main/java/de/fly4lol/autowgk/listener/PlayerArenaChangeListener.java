package de.fly4lol.autowgk.listener;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.arena.Team;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.event.PlayerArenaChangeEvent;
import de.pro_crafting.wg.group.GroupSide;
import de.pro_crafting.wg.group.PlayerRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;


public class PlayerArenaChangeListener implements Listener {
    private Main plugin;

    public PlayerArenaChangeListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void playerArenaChangeHandler(PlayerArenaChangeEvent event) {
        Bukkit.broadcastMessage(event.getFrom().getName()+"|"+event.getTo().getName());

        leaveArena(event.getPlayer());
        event.setMessage("Du wirst aus deinem Team geworfen, da du die Arena verlassen hast!");
    }

    @EventHandler
    public void playerQuitHandler(PlayerQuitEvent event) {
        leaveArena(event.getPlayer());
    }

    private void leaveArena(Player player) {
        Team team = plugin.getArenenManager().getTeamByPlayer(player);
        if (team == null) {
            return;
        }
        Arena arena = team.getAutoArena().getWgkArena();

        if(arena.getState() == State.Idle || arena.getState() == State.Setup){
            if(team.getRole() == PlayerRole.Team1){
                team.getAutoArena().setTeam1(null);
            } else if(team.getRole() == PlayerRole.Team2){
                team.getAutoArena().setTeam2(null);
            }
        }
        if (arena.getState() == State.Setup) {
            GroupSide side = team.getRole() == PlayerRole.Team1 ? GroupSide.Team1 : GroupSide.Team2;
            team.getAutoArena().getWgkArena().getReseter().cleanSide(side);
            this.plugin.getRepo().getWarGear().getScoreboard().removeTeamMember(arena, arena.getGroupManager().getGroupMember(player), team.getRole());
            arena.getGroupManager().getGroupOfPlayer(player).remove(player);

        }
    }
}
