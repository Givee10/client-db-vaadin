package com.givee.demo.client.ui.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;

public class FullSizeVerticalLayout extends VerticalLayout {
	public FullSizeVerticalLayout() {
		setHeight(100, Unit.PERCENTAGE);
	}

	public FullSizeVerticalLayout(Component... components) {
		this();
		for (Component c : components) {
			addComponent(c);
		}
	}

	public void addComponentAndRatio(Component component, float ratio) {
		addComponent(component);
		setExpandRatio(component, ratio);
	}
}
