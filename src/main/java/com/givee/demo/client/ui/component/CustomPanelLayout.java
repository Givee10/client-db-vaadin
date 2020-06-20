package com.givee.demo.client.ui.component;

import com.givee.demo.client.AppTheme;
import com.givee.demo.client.HasLogger;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.UpdateViewEvent;
import com.givee.demo.client.utils.CompsUtil;
import com.givee.demo.client.utils.StringUtil;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;

import java.util.LinkedHashMap;

public class CustomPanelLayout extends FullSizeVerticalLayout implements HasLogger {
	private static final LinkedHashMap<String, String> legend = new LinkedHashMap<String, String>() {{
		put("Hello", AppTheme.STYLE_ET_NORMAL);
		put("World", AppTheme.STYLE_ET_NORMAL);
	}};

	private CustomCheckBoxGroup<String> checkBoxGroup = new CustomCheckBoxGroup<>();

	public CustomPanelLayout() {
		AppEventBus.register(this);
		Responsive.makeResponsive(this);

		setMargin(false);
		setSpacing(false);
	}

	private void update() {
		CompsUtil.buildTrayNotification("Update");
		getLogger().debug(StringUtil.writeValueAsString(checkBoxGroup.getSelectedItems()));
	}

	public void addToolBarItems(CustomPanel panel) {
		panel.addPopupContent(createPopupComponent(panel));
		panel.addToolbarItem("", "Filter", AppTheme.ICON_FILTER, (MenuBar.Command) menuItem -> panel.showPopup());
		panel.addLegendItem("", "Legend", AppTheme.ICON_LEGEND, legend);
		panel.addToolbarItem("", "Refresh", AppTheme.ICON_REFRESH, (MenuBar.Command) menuItem -> update());
	}

	private Component createPopupComponent(CustomPanel panel) {
		checkBoxGroup.setItems(legend.keySet());

		CustomButton selectAll = new CustomButton("Все", $ -> checkBoxGroup.selectAll());
		CustomButton close = new CustomButton("Показать", $ -> {
			panel.hidePopup();
			update();
		});
		return CompsUtil.getVerticalWrapperNoMargin(selectAll, checkBoxGroup, close);
	}

	@Subscribe
	public void handleUpdateViewEvent(final UpdateViewEvent event) {
		update();
	}
}
