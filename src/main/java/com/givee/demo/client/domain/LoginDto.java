package com.givee.demo.client.domain;

public class LoginDto {
	private String database;
	private String username;
	private String password;

	public LoginDto() {
	}

	public LoginDto(String database, String username, String password) {
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
