package de.fly4lol.autowgk.messagemanager;

import java.util.List;

import mkremins.fanciful.FancyMessage;

public class Message {
	public List<MyMessage> message;
	
	public Message(){
		
	}
	
	public Message addLine(String string){
		MyMessage message = new MyMessage().setMessage( string );
		this.message.add( message );
		return this;
	}
	
	public Message addLine(FancyMessage fancyMessage){
		MyMessage message = new MyMessage().setFancyMessage( fancyMessage);
		this.message.add( message);
		return this;
	}
	
	public List<MyMessage> getMessage(){
		return this.message;
	}
}
