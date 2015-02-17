package de.fly4lol.autowgk.util;

import java.util.UUID;

import de.fly4lol.autowgk.fightmanager.Direction;

public class Schematic {
	private int id;
	private String name;
	private UUID owner;
	private boolean isWarGear;
	private SchematicState state;
	private boolean isPublic;
	private Direction direction;

	public Schematic(int id, String name, UUID owner, boolean isWarGear, boolean isPublic, SchematicState state) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.isWarGear = isWarGear;
		this.state = state;
		this.isPublic = isPublic;
		
		this.direction = Direction.parse(name.substring(name.length() - 2, name.length()));
	}

	public Direction getDirection() {
		return direction;
	}

	public String getName() {
		return name;
	}

	public UUID getOwner() {
		return owner;
	}

	public boolean isWarGear() {
		return isWarGear;
	}

	public SchematicState getState() {
		return state;
	}

	public boolean isPublic() {
		return isPublic;
	}
	
	public boolean isOwner(UUID id) {
		return this.getOwner().equals(id);
	}
	
	public String getPath() {
		String base = this.owner + "/"+ this.name;
		if (!base.endsWith(".schematic")) {
			return base + ".schematic";
		}
		return base;
	}

	public int getId() {
		return this.id;
	}
}
