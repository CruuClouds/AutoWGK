package de.fly4lol.autowgk.fightmanager;

public class Fight {
	private Arena arena;
	private Team team1;
	private Team team2;
	private boolean isRunnig;
	
	public Fight(Arena arena, Team team1, Team team2, boolean isRunning){
		this.arena = arena;
		this.team1 = team1;
		this.team2 = team2;
		this.isRunnig = isRunning;
	}

	public Arena getArena() {
		return arena;
	}

	public void setArena(Arena arena) {
		this.arena = arena;
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

	public boolean isRunnig() {
		return isRunnig;
	}

	public void setRunnig(boolean isRunnig) {
		this.isRunnig = isRunnig;
	}

}
