package de.fly4lol.autowgk.arena;

public enum Direction {
	North("north"),
	South("south");
	
	private String configName;
	private Direction(String configName) {
		this.configName = configName;
	}
	
	public String getName() {
		return this.configName;
	}
	
	public static Direction parse(String direction) {
		if (direction.equalsIgnoreCase("n") || direction.equalsIgnoreCase("north") || direction.equalsIgnoreCase("_n")) {
			return Direction.North;
		} else if (direction.equalsIgnoreCase("s") || direction.equalsIgnoreCase("south") || direction.equalsIgnoreCase("_s")) {
			return Direction.South;
		}
		return null;
	}
}
