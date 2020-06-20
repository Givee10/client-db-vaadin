package com.givee.demo.client.ui.component;

import com.givee.demo.client.AppTheme;
import com.givee.demo.client.utils.CompsUtil;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;

import java.util.*;

public class CustomPanel extends CustomComponent {
	private final VerticalLayout layout = new FullSizeVerticalLayout();

	private final String caption;
	private final Component content;
	private final HorizontalLayout header = new HorizontalLayout();

	private boolean show = true;
	private boolean maximized = false;
	private MenuBar toolBar = new MenuBar();
	private MenuBar.MenuItem maxItem;

	private PopupView view;

	private List<ToggleMaximizedListener> listeners = new ArrayList<>();
	private List<ToggleVisibleListener> visibleListeners = new ArrayList<>();

	public CustomPanel(String caption, Component content) {
		this.caption = caption;
		this.content = content;

		setSizeFull();
		initContent();

		setCompositionRoot(layout);
	}

	private void initContent() {
		layout.addStyleName(AppTheme.LAYOUT_WELL);
		layout.addComponent(buildHeader());
		layout.addComponent(content);
		layout.setExpandRatio(content, 1);
		layout.setMargin(false);
		layout.setSpacing(false);
	}

	private Component buildHeader() {
		header.setWidth(100, Unit.PERCENTAGE);
		header.addStyleName(AppTheme.PANEL_CAPTION);

		Label title = new Label(caption);
		title.addStyleName(AppTheme.LABEL_COLORED);

		header.addComponent(title);
		header.setExpandRatio(title, 1);

		header.addComponent(buildPopup());
		header.addComponent(buildToolbar());

		return header;
	}

	private Component buildToolbar() {
		toolBar.addStyleName(AppTheme.MENUBAR_BORDERLESS);

		maxItem = toolBar.addItem("", AppTheme.ICON_EXPAND, selectedItem -> {
			if (maximized) {
				maximized = false;
				toggleMaximized(false);
				selectedItem.setIcon(AppTheme.ICON_EXPAND);
			} else {
				maximized = true;
				toggleMaximized(true);
				selectedItem.setIcon(AppTheme.ICON_COMPRESS);
			}
			maxItem.setDescription(maximized ? "Свернуть" : "Развернуть");
		});
		maxItem.setStyleName(AppTheme.BUTTON_ICON_ONLY);
		maxItem.setDescription("Развернуть");

		return toolBar;
	}

	private Component buildPopup() {
		view = new PopupView(null, CompsUtil.getVerticalWrapperNoMargin());
		view.setHideOnMouseOut(false);
		hidePopup();

		return view;
	}

	private void toggleMaximized(boolean b) {
		listeners.forEach(l -> l.toggleMaximized(this, b));
	}

	public void addToggleMaximizedListener(ToggleMaximizedListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	public void removeToggleMaximizedListener(ToggleMaximizedListener listener) {
		listeners.remove(listener);
	}

	public void setMinMaxVisible(boolean isVisible) {
		maxItem.setVisible(isVisible);
	}

	public MenuBar.MenuItem addToolbarItem(String caption, String description, Resource icon, MenuBar.Command command) {
		MenuBar.MenuItem item = toolBar.addItemBefore(caption, icon, command, maxItem);
		item.setDescription(description);
		item.setStyleName(AppTheme.BUTTON_ICON_ONLY);
		return item;
	}

	public MenuBar.MenuItem addFilterItem(String caption, String description, Resource icon, LinkedHashMap<String, MenuBar.Command> filterItems) {
		MenuBar.MenuItem item = addToolbarItem(caption, description, icon, null);
		for (Map.Entry<String, MenuBar.Command> legendItem : filterItems.entrySet()) {
			MenuBar.MenuItem item2 = item.addItem(legendItem.getKey(), legendItem.getValue());
			item2.setCheckable(true);
			item2.setChecked(true);
		}
		return item;
	}

	public MenuBar.MenuItem addLegendItem(String caption, String description, Resource icon, LinkedHashMap<String, String> legendItems) {
		MenuBar.MenuItem item = addToolbarItem(caption, description, icon, null);
		for (Map.Entry<String, String> legendItem : legendItems.entrySet()) {
			MenuBar.MenuItem item2 = item.addItem(legendItem.getKey(), AppTheme.ICON_LEGEND_ITEM, null);
			//item2.setCheckable(true);
			item2.setChecked(true);
			item2.setStyleName(legendItem.getValue());
		}
		return item;
	}

	public void addPopupContent(Component component) {
		view.setContent(new PopupView.Content() {
			@Override
			public String getMinimizedValueAsHTML() {
				return null;
			}

			@Override
			public Component getPopupComponent() {
				return component;
			}
		});
	}

	public void hidePopup() {
		view.setPopupVisible(false);
	}

	public void showPopup() {
		view.setPopupVisible(true);
	}

	public void toggleShow() {
		show = !show;
		super.setVisible(show);
		visibleListeners.forEach(l -> l.toggleVisible(this, show));
	}

	public void addToggleVisibleListener(ToggleVisibleListener listener) {
		if (!visibleListeners.contains(listener)) {
			visibleListeners.add(listener);
		}
	}

	public void removeToggleVisibleListener(ToggleVisibleListener listener) {
		visibleListeners.remove(listener);
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible && show);
	}

	/**
	 * Класс для подписчиков на событие изменения размера панели
	 */
	public interface ToggleMaximizedListener {
		void toggleMaximized(final CustomPanel panel, final boolean maximized);
	}

	public interface ToggleVisibleListener {
		void toggleVisible(final CustomPanel panel, final boolean visible);
	}

	public MenuBar getToolBar() {
		return toolBar;
	}
}
