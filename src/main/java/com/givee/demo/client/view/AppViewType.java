package com.givee.demo.client.view;

import com.givee.demo.client.AppTheme;
import com.vaadin.navigator.View;
import com.vaadin.server.Resource;

public enum AppViewType {
	MAIN(MainView.NAME, MainView.CAPTION, MainView.class, AppTheme.ICON_DATABASE, null, true),
	GRID(GridView.NAME, GridView.CAPTION, GridView.class, AppTheme.ICON_SETUP, null, false);

	private final String viewName;
	private final String caption;
	private final Class<? extends View> viewClass;
	private final Resource icon;
	private final String badgeId;
	private final boolean enabled;

	AppViewType(final String viewName, String caption, final Class<? extends View> viewClass, final Resource icon, final String badgeId, final boolean enabled) {
		this.viewName = viewName;
		this.caption = caption;
		this.viewClass = viewClass;
		this.icon = icon;
		this.badgeId = badgeId;
		this.enabled = enabled;
	}

	public static AppViewType getByViewName(final String viewName) {
		AppViewType result = null;
		for (AppViewType viewType : values()) {
			if (viewType.getViewName().equals(viewName)) {
				result = viewType;
				break;
			}
		}
		return result;
	}

	public String getViewName() {
		return viewName;
	}

	public String getCaption() {
		return caption;
	}

	public Class<? extends View> getViewClass() {
		return viewClass;
	}

	public Resource getIcon() {
		return icon;
	}

	public String getBadgeId() {
		return badgeId;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
