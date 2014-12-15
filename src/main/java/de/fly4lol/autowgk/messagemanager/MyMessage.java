package de.fly4lol.autowgk.messagemanager;

import mkremins.fanciful.FancyMessage;

public class MyMessage {
	private String message;
	private FancyMessage fancyMessage;
	
	public MyMessage(){
		
	}

	public String getMessage() {
		return message;
	}

	public MyMessage setMessage(String message) {
		this.message = message;
		return this;
	}

	public FancyMessage getFancyMessage() {
		return fancyMessage;
	}

	public MyMessage setFancyMessage(FancyMessage fancyMessage) {
		this.fancyMessage = fancyMessage;
		return this;
	}
	
}
