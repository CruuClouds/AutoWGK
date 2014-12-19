package de.fly4lol.autowgk.messagemanager;

import java.util.ArrayList;
import java.util.List;

import mkremins.fanciful.FancyMessage;

public class Message {
	public List<MyMessage> message = new ArrayList<MyMessage>();
	
	public Message(){
		
		
	}
	
	public Message addLine(String string){
		MyMessage myMessage =  new MyMessage().setMessage( string );
		message.add( myMessage );
		return this;
	}
	
	public Message addLine(FancyMessage fancyMessage){
		MyMessage mymessage = new MyMessage().setFancyMessage( fancyMessage);
		message.add( mymessage);
		return this;
	}
	
	public List<MyMessage> getMessage(){
		return this.message;
	}
}
