package com.givee.demo.client.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;

public class CustomButton extends Button {
	public CustomButton(String caption, ClickListener listener) {
		super(caption, listener);
		setSizeFull();
		setDescription(caption);
	}

	public CustomButton(Resource icon, String description, ClickListener listener) {
		super(icon, listener);
		setSizeFull();
		setDescription(description);
	}

	public CustomButton(String caption) {
		this(caption, $ -> {});
	}

	public CustomButton(Resource icon, String description) {
		this(icon, description, $ -> {});
	}
}
