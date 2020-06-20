package com.givee.demo.client.view;

import com.givee.demo.client.AppUI;
import com.givee.demo.client.HasLogger;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.AppRestTemplate;
import com.givee.demo.client.event.UpdateViewEvent;
import com.givee.demo.client.ui.component.CustomButton;
import com.givee.demo.client.ui.component.FullSizeVerticalLayout;
import com.givee.demo.client.ui.event.EventGrid;
import com.givee.demo.client.utils.CompsUtil;
import com.givee.demo.client.utils.StringUtil;
import com.google.common.eventbus.Subscribe;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

public class GridView extends FullSizeVerticalLayout implements View, HasLogger {
	public static final String NAME = "grid";
	public static final String CAPTION = "Таблицы";

	private AppRestTemplate restTemplate = AppUI.getRestTemplate();
	private EventGrid eventGrid = new EventGrid();

	public GridView() {
		AppEventBus.register(this);
		Responsive.makeResponsive(this);

		eventGrid.initFilters();
		CustomButton button1 = new CustomButton("GO TO 01", $ -> scrollAndSelect(1L));
		CustomButton button2 = new CustomButton("GO TO 11", $ -> scrollAndSelect(11L));
		CustomButton button3 = new CustomButton("GO TO 21", $ -> scrollAndSelect(21L));
		CustomButton button4 = new CustomButton("GO TO 31", $ -> scrollAndSelect(31L));
		CustomButton button5 = new CustomButton("GO TO 41", $ -> scrollAndSelect(41L));
		CustomButton button6 = new CustomButton("GO TO 51", $ -> scrollAndSelect(51L));
		CustomButton button7 = new CustomButton("GO TO 61", $ -> scrollAndSelect(61L));
		CustomButton button8 = new CustomButton("GO TO 71", $ -> scrollAndSelect(71L));
		CustomButton button9 = new CustomButton("GO TO 81", $ -> scrollAndSelect(81L));
		CustomButton button0 = new CustomButton("GO TO 91", $ -> scrollAndSelect(91L));
		VerticalLayout buttonLayout = CompsUtil.getVerticalWrapperNoMargin(button1, button2, button3, button4, button5, button6, button7, button8, button9, button0);
		buttonLayout.setWidthUndefined();
		HorizontalLayout gridLayout = CompsUtil.getHorizontalWrapperNoMargin(eventGrid, buttonLayout);
		gridLayout.setExpandRatio(eventGrid, 1);
		addComponentAndRatio(gridLayout, 1);

		update();
	}

	private void update() {
		getLogger().debug(StringUtil.writeValueAsString(eventGrid.getItems()));
	}

	private void scrollAndSelect(Long id) {
		eventGrid.select(id);
		eventGrid.scrollTo(id);
	}

	@Subscribe
	public void handleUpdateViewEvent(final UpdateViewEvent event) {
		update();
	}
}
