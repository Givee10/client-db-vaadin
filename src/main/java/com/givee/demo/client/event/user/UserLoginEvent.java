package com.givee.demo.client.event.user;

import com.givee.demo.client.domain.LoginDto;
import com.givee.demo.client.event.ApplicationBaseEvent;

public class UserLoginEvent extends ApplicationBaseEvent {
	private LoginDto user;

	public UserLoginEvent(Object source, LoginDto user) {
		super(source);
		this.user = user;
	}

	public LoginDto getUser() {
		return user;
	}
}
