package de.fly4lol.autowgk.messagemanager;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class Messenger {
	private HashMap<Player, Message> lastMessage = new HashMap<Player, Message>();
	private Message message;
	@SuppressWarnings("unused")
	private Player player;
	private Messages messages;

	Messenger(Message message, Player player){
		this.message = message;
		this.player = player;
	}
	
	
	
	public Messenger(){
		
	}
	
	public Messenger setMessage(Message message) {
		this.message = message;
		return this;
	}

	public Messenger setPlayer(Player player) {
		this.player = player;
		return this;
	}

	public void send(){
		if(this.message.equals( Message.AUTOWGKJOIN )){
			messages.sendAutoJoinMessage( this.player );
			this.lastMessage.put( this.player , Message.AUTOWGKJOIN);
		}
	}
	
	public void sendLast(){
		if(this.lastMessage.get( this.player ) != null){
			Message message = this.lastMessage.get(player);
			this.setMessage( message );
			this.send();
		}
		
	}
}