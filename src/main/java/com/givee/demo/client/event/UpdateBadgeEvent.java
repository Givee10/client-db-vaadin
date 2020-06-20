package com.givee.demo.client.event;

public class UpdateBadgeEvent {
	private final String badgeId;
	private final String badgeValue;

	public UpdateBadgeEvent(String badgeId, String badgeValue) {
		this.badgeId = badgeId;
		this.badgeValue = badgeValue;
	}

	public String getBadgeId() {
		return badgeId;
	}

	public String getBadgeValue() {
		return badgeValue;
	}
}
