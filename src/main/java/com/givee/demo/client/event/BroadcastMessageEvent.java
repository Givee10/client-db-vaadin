package com.givee.demo.client.event;

public class BroadcastMessageEvent extends ApplicationBaseEvent {
	public BroadcastMessageEvent(final String message) {
		super(message);
	}

	public String getMessage() {
		return (String) getSource();
	}
}
