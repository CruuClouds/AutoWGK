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
	
	public Schematic(){
		
	}
	
	public Schematic(int id, String name, UUID owner, boolean isWarGear, SchematicState state, boolean isPublic) {
		this.id = id;
		this.name = name;
		this.owner = owner;
		this.isWarGear = isWarGear;
		this.state = state;
		this.isPublic = isPublic;
	}
	
	

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(UUID owner) {
		this.owner = owner;
	}

	public void setWarGear(boolean isWarGear) {
		this.isWarGear = isWarGear;
	}

	public void setState(SchematicState state) {
		this.state = state;
	}

	public void setPublic(boolean isPublic) {
		this.isPublic = isPublic;
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
		return this.owner + "/"+ this.name;
	}

	public int getId() {
		return this.id;
	}
}
