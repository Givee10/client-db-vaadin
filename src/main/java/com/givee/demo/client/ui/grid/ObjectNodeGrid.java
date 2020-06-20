package com.givee.demo.client.ui.grid;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.givee.demo.client.domain.ColumnDto;
import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Grid;

import java.util.List;

public class ObjectNodeGrid extends Grid<ObjectNode> {
	public ObjectNodeGrid(List<ColumnDto> tableColumns) {
		super();
		setWidth(100, Unit.PERCENTAGE);
		setHeight(100, Unit.PERCENTAGE);
		for (ColumnDto columnDto : tableColumns) {
			String columnName = columnDto.getColumn_name();
			addColumn((ValueProvider<ObjectNode, String>) jsonNodes -> {
				if (jsonNodes.has(columnName)) {
					return jsonNodes.get(columnName).asText();
				} else {
					return null;
				}
			}).setCaption(columnName);
		}
	}
}
