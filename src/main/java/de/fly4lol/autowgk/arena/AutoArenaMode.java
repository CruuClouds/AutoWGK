package de.fly4lol.autowgk.arena;

public enum AutoArenaMode {
	NORMAL,
	DISABLED;
	
	public static AutoArenaMode parse(String mode) {
		if (mode.equalsIgnoreCase("normal")) {
			return NORMAL;
		} else if (mode.equalsIgnoreCase("closed") || mode.equalsIgnoreCase("off") || mode.equalsIgnoreCase("disabled")) {
			return DISABLED;
		}
		return null;
	}
}
