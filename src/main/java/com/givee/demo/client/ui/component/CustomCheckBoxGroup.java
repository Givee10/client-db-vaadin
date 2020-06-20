package com.givee.demo.client.ui.component;

import com.vaadin.data.provider.Query;
import com.vaadin.ui.CheckBoxGroup;

import java.util.List;
import java.util.stream.Collectors;

public class CustomCheckBoxGroup<T> extends CheckBoxGroup<T> {
	public CustomCheckBoxGroup() {
		super();
		setSizeFull();
	}

	public List<T> getAllItems() {
		return getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
	}

	public void selectAll() {
		getAllItems().forEach(this::select);
	}
}
