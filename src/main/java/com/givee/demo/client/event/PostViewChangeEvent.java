package com.givee.demo.client.event;

import com.givee.demo.client.view.AppViewType;

public class PostViewChangeEvent {
	private final AppViewType view;

	public PostViewChangeEvent(final AppViewType view) {
		this.view = view;
	}

	public AppViewType getView() {
		return view;
	}
}
