package com.givee.demo.client.ui.event;

import com.givee.demo.client.domain.EventDto;
import com.givee.demo.client.ui.grid.GridColumn;

import java.util.Arrays;
import java.util.List;

public abstract class EventConstants {
	public static final GridColumn<EventDto> ID = GridColumn.createColumn("id", "№", EventDto::getId);
	public static final GridColumn<EventDto> DATE = GridColumn.createDateColumn("date", "Время события", EventDto::getCreateDate);
	public static final GridColumn<EventDto> TYPE = GridColumn.createColumn("type", "Тип", EventDto::getName);
	public static final GridColumn<EventDto> ORDER = GridColumn.createColumn("source", "Порядок", EventDto::getOrder);
	public static final GridColumn<EventDto> USER = GridColumn.createColumn("address", "Пользователь", EventDto::getUserId);

	public static List<GridColumn<EventDto>> getMainColumns() {
		return Arrays.asList(ID, DATE, TYPE, ORDER, USER);
	}
}
