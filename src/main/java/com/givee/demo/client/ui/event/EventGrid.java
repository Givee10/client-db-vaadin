package com.givee.demo.client.ui.event;

import com.givee.demo.client.domain.EventDto;
import com.givee.demo.client.ui.grid.CustomGrid;
import com.givee.demo.client.ui.grid.GridColumn;
import com.vaadin.shared.data.sort.SortDirection;

import java.util.ArrayList;
import java.util.List;

public class EventGrid extends CustomGrid<EventDto> {
	public EventGrid(List<GridColumn<EventDto>> gridColumns, List<EventDto> items) {
		super(gridColumns, items);
	}

	public EventGrid(List<EventDto> items) {
		this(EventConstants.getMainColumns(), items);
		sort(EventConstants.ID.getId(), SortDirection.DESCENDING);
	}

	public EventGrid() {
		this(new ArrayList<>());
	}

	@Override
	protected void onDoubleClick(EventDto entity, Column column) {

	}
}
