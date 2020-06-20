package com.givee.demo.client.view;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.givee.demo.client.AppUI;
import com.givee.demo.client.HasLogger;
import com.givee.demo.client.domain.ColumnDto;
import com.givee.demo.client.domain.TableDto;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.AppRestTemplate;
import com.givee.demo.client.event.UpdateViewEvent;
import com.givee.demo.client.ui.component.FullSizeVerticalLayout;
import com.givee.demo.client.ui.grid.ObjectNodeGrid;
import com.givee.demo.client.utils.CompsUtil;
import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import java.util.*;

public class MainView extends FullSizeVerticalLayout implements View, HasLogger {
	public static final String NAME = "main";
	public static final String CAPTION = "Главная";

	private AppRestTemplate restTemplate = AppUI.getRestTemplate();
	private Map<String, ObjectNodeGrid> gridMap = new HashMap<>();

	public MainView() {
		AppEventBus.register(this);
		Responsive.makeResponsive(this);

		TabSheet tabSheet = new TabSheet();
		tabSheet.setSizeFull();
		addComponentAndRatio(tabSheet, 1);

		List<TableDto> tables = restTemplate.getTables();
		for (TableDto tableDto : tables) {
			String tableName = tableDto.getTable_name();
			//getLogger().debug("Table: {}", tableName);
			List<ColumnDto> tableColumns = restTemplate.getTableColumns(tableName);
			//getLogger().debug("Columns: {}", StringUtil.writeValueAsString(tableColumns));
			ObjectNodeGrid grid = new ObjectNodeGrid(tableColumns);
			tabSheet.addTab(CompsUtil.getVerticalWrapperNoMargin(grid), tableName);
			gridMap.put(tableName, grid);
		}
	}

	private void update() {
		for (String tableName : gridMap.keySet()) {
			List<ObjectNode> tableValues = restTemplate.getTableValues(tableName);
			gridMap.get(tableName).setItems(tableValues);
		}
	}

	@Subscribe
	public void handleUpdateViewEvent(final UpdateViewEvent event) {
		update();
	}
}
