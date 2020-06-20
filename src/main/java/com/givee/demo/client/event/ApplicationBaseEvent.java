package com.givee.demo.client.event;

import org.springframework.context.ApplicationEvent;

public class ApplicationBaseEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private final String message;

	public ApplicationBaseEvent(Object source) {
		this(source, null);
	}

	public ApplicationBaseEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ApplicationBaseEvent [message=\"" + message + "\"]";
	}
}
