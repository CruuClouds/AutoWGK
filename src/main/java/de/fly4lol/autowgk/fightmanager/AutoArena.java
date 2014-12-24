package de.fly4lol.autowgk.fightmanager;

import mkremins.fanciful.FancyMessage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

import de.fly4lol.autowgk.Main;
import de.fly4lol.autowgk.messagemanager.Message;
import de.fly4lol.autowgk.messagemanager.Messenger;
import de.pro_crafting.wg.arena.Arena;
import de.pro_crafting.wg.arena.State;
import de.pro_crafting.wg.group.Group;

public class AutoArena {
	private Messenger messager;
	private Main plugin;
	private Util util;
	private String name;
	private Location team1Loc;
	private Location team2Loc;
	private AutoArenaMode mode;
	private String team1Direction;
	private String team2Direction;
	private Team team1;
	private Team team2;
	private Arena arena;
	
	public AutoArena(String name, Location team1Loc, Location team2Loc, AutoArenaMode mode, String team1Direction, String team2Direction , Team team1 , Team team2){
		this.team1Loc = team1Loc;
		this.team2Loc = team2Loc;
		this.mode = mode;
		this.team1Direction = team1Direction;
		this.team2Direction = team2Direction;
	}


	public AutoArena(){
	}
	
	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Location getTeam1Loc() {
		return team1Loc;
	}

	public void setTeam1Loc(Location team1Loc) {
		this.team1Loc = team1Loc;
	}

	public Location getTeam2Loc() {
		return team2Loc;
	}

	public void setTeam2Loc(Location team2Loc) {
		this.team2Loc = team2Loc;
	}

	public String getTeam1Direction() {
		return team1Direction;
	}

	public void setTeam1Direction(String team1Location) {
		this.team1Direction = team1Location;
	}

	public String getTeam2Direction() {
		return team2Direction;
	}

	public void setTeam2Direction(String team2Location) {
		this.team2Direction = team2Location;
	}

	public AutoArenaMode getMode() {
		return mode;
	}

	public void setMode(AutoArenaMode mode) {
		this.mode = mode;
	}	
	
	public Team getTeam1() {
		return team1;
	}

	public void setTeam1(Team team1) {
		this.team1 = team1;
	}

	public Team getTeam2() {
		return team2;
	}

	public void setTeam2(Team team2) {
		this.team2 = team2;
	}
	
	public void setPlugin(Main plugin) {
		this.plugin = plugin;
	}
	
	public Main getPlugin(){
		return this.plugin;
	}

	public boolean isJoinable(){
		Arena arena = plugin.wg.getArenaManager().getArena( this.getName());
		State state = arena.getState();
		if(arena != null){
			if(state == State.Idle || state == State.Resetting || state == State.Spectate ){
				if(this.getTeam1() == null || this.getTeam2() == null){
					return true;
				} 
			} else if(state == State.Setup && (this.getTeam1() == null || this.getTeam2() == null)){
				return true;
			}
		} 
		return false;
	}
	
	public void joinArena(Player player){
		if(this.getMode() != AutoArenaMode.DISABLED){
			if(this.isJoinable()){
				if(plugin.wg.getArenaManager().getArenaOfTeamMember( player ) ==  null){
					if(plugin.getUtil().getTeamByPlayer( player) == null){
						Team team = new Team();
						team.setLeader( player ).setArena( this );
						FancyMessage privateMessage = new FancyMessage( this.plugin.prefix)
						.then("§9§nPrivate")
						.tooltip("§5Klicke")
						.command("/AutoWGK schematic list");
						
						FancyMessage publicMessage = new FancyMessage( this.plugin.prefix)
						.then("§9§nPublic")
						.tooltip("§5Klicke")
						.command("/AutoWGK schematic public");
						
						Message message = new Message();
						message
						.addLine("")
						.addLine("")
						.addLine("            §2Wähle Zwischen Public und Private ")
						.addLine("")
						.addLine(publicMessage)
						.addLine("")
						.addLine(privateMessage)
						.addLine("");
						
						new Messenger().setMessage(message).setPlayer( player ).send();
						if(this.getTeam1() == null){
							this.setTeam1( team);
						} else {
							this.setTeam2( team);
						}
					} else {
						player.sendMessage(plugin.prefix + "Du bist schon in einem Team!");
					}
				} else {
					player.sendMessage(plugin.prefix + "Du bist schon in einem Team!");
				}
			} else {
				player.sendMessage(plugin.prefix + "Du kannst immoment nicht Joinen!");
			}
		} else {
			player.sendMessage(plugin.prefix + "Diese Arena ist immoment nicht für AutoWGK verfügbar!");
		}	
	}
	
	public void startGame(){
		
		this.getTeam1().startGame( true);
		this.getTeam2().startGame( false);
		if(this.getArena().getState() == State.Idle ){
			this.getArena().updateState( State.Setup);
		}
		
		
	}
	
	
}
