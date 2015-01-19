package de.fly4lol.autowgk.messagemanager;

import java.util.List;

import org.bukkit.entity.Player;

public class Messenger {
	private Message message;
	private Player player;

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
		List<MyMessage> messages = this.message.getMessage();
		Messages.lastMessage.put( this.player , this.message);
		for(MyMessage message : messages){
			if(message.getFancyMessage() == null){
				player.sendMessage(message.getMessage());
			} else {
				message.getFancyMessage().send( this.player );
			}
		}
	}
	
	public void sendLast(){
		Message lastMessage = Messages.lastMessage.get( player );
		List<MyMessage> messages = lastMessage.getMessage();
		if(messages.isEmpty()){
			player.sendMessage("§8[§9AutoWGK§8] §2Du hast noch keine Nachrichten bekommen!");
			
		} else {
			for(MyMessage message : messages){
				if(message.getFancyMessage() == null){
					player.sendMessage(message.getMessage());
				} else {
					message.getFancyMessage().send( this.player );
				}
			}
		}
			
	} 
}