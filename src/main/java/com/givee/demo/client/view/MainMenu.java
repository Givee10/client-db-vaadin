package com.givee.demo.client.view;

import com.givee.demo.client.AppTheme;
import com.givee.demo.client.AppUI;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.PostViewChangeEvent;
import com.givee.demo.client.event.UpdateBadgeEvent;
import com.givee.demo.client.event.user.UserLogoutEvent;
import com.google.common.eventbus.Subscribe;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class MainMenu extends VerticalLayout {
	public static final String ID = "dashboard-menu";
	private final Map<String, Label> badgeLabels = new HashMap<>();

	public MainMenu() {
		AppEventBus.register(this);
		setWidthUndefined();
		setDefaultComponentAlignment(Alignment.TOP_CENTER);
		setMargin(false);

		//setPrimaryStyleName(AppTheme.MENU_ROOT);
		setId(ID);
		addStyleName(AppTheme.MENU_PART);
		addStyleName(AppTheme.MENU_PART_LARGE_ICONS);

		//addComponent(buildLogo());
		addComponent(buildMenuItems());
	}

	private Component buildLogo() {
		VerticalLayout title = new VerticalLayout();
		title.setStyleName(AppTheme.STYLE_ET_NORMAL);
		title.setSizeFull();
		//title.setMargin(new MarginInfo(true,false));
		title.setMargin(false);
		title.setSpacing(false);

		Image logo = new Image(null, new ThemeResource("img/logo.png"));
		logo.setWidth(100, Unit.PERCENTAGE);

		title.addComponent(logo);
		title.setComponentAlignment(logo, Alignment.MIDDLE_CENTER);

		return title;
	}

	/**
	 * Создание пунктов меню
	 *
	 * @return Компонент с пунктами меню
	 */
	private Component buildMenuItems() {
		CssLayout menuItems = new CssLayout();
		menuItems.addStyleName(AppTheme.MENU_ITEMS);

		for (AppViewType view : AppViewType.values()) {
			Component menuItemComponent = new MenuButton(view);
			if (view.getBadgeId() != null) {
				Label label = new Label();
				label.setId(view.getBadgeId());
				badgeLabels.put(view.getBadgeId(), label);
				menuItemComponent = buildBadgeWrapper(menuItemComponent, label);
			}
			menuItems.addComponent(menuItemComponent);
		}
		menuItems.addComponent(new LogoutButton());

		return menuItems;
	}

	private Component buildBadgeWrapper(final Component menuItemButton, final Component badgeLabel) {
		CssLayout wrapper = new CssLayout(menuItemButton);
		wrapper.addStyleName(AppTheme.MENU_BADGEWRAPPER);
		wrapper.addStyleName(ValoTheme.MENU_ITEM);
		badgeLabel.addStyleName(ValoTheme.MENU_BADGE);
		badgeLabel.setWidthUndefined();
		badgeLabel.setVisible(false);
		wrapper.addComponent(badgeLabel);
		return wrapper;
	}

	@Subscribe
	public void updateBadge(final UpdateBadgeEvent event) {
		Label label = badgeLabels.get(event.getBadgeId());
		if (label != null) {
			String value = event.getBadgeValue();
			label.setValue(String.valueOf(value));
			label.setVisible(value != null && !"".equals(value) && !"0".equals(value));
		}
	}

	private class MenuButton extends Button {
		private final AppViewType view;

		public MenuButton(final AppViewType view) {
			super(null, view.getIcon());
			this.view = view;
			AppEventBus.register(this);
			setPrimaryStyleName(AppTheme.MENU_ITEM);
			setEnabled(view.isEnabled());
			setDescription(view.getCaption());
			addClickListener(event -> AppEventBus.post(new PostViewChangeEvent(view)));
		}

		@Subscribe
		public void changeView(PostViewChangeEvent event) {
			if (event.getView() == view) {
				addStyleName(AppTheme.MENU_ITEM_SELECTED);
				UI.getCurrent().getNavigator().navigateTo(view.getViewName());
			} else {
				removeStyleName(AppTheme.MENU_ITEM_SELECTED);
			}
		}
	}

	private class LogoutButton extends Button {
		public LogoutButton() {
			super(null, AppTheme.ICON_LOGOUT);
			AppEventBus.register(this);
			setPrimaryStyleName(AppTheme.MENU_ITEM);
			setDescription("Выход из системы");
			addClickListener(event -> AppEventBus.post(new UserLogoutEvent(getUI(), AppUI.getCurrentLogin())));
		}
	}
}
