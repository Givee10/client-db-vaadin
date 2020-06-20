package com.givee.demo.client.utils;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;

/**
 * Работа с компонентами
 */
public class CompsUtil {
	public static Component getStatisticWrapper(Component component) {
		FormLayout layout = new FormLayout();
		layout.addComponent(component);
		component.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(true);
		return layout;
	}

	public static Component buildEmptyTab() {
		final VerticalLayout layout = getVerticalWrapperWithMargin();
		layout.setHeight(200, Sizeable.Unit.PIXELS);
		return layout;
	}

	public static Component buildGridWithButtons(Grid grid, Button... buttons) {
		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.setMargin(false);
		layout.setSpacing(false);

		final VerticalLayout buttonLayout = new VerticalLayout();
		buttonLayout.setWidthUndefined();
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		buttonLayout.addComponents(buttons);

		layout.addComponent(grid);
		layout.setExpandRatio(grid, 1);
		layout.addComponent(buttonLayout);
		return layout;
	}

	/**
	 * Возвращает компонент для группы
	 *
	 * @param caption
	 * @param component
	 */
	public static Panel getGroupWrapper(String caption, Component component) {
		final Panel panel = new Panel(caption);
		panel.setSizeFull();
		panel.setContent(component);
		return panel;
	}

	/**
	 * Возвращет VerticalLayout для перечисленных компонентов
	 *
	 * @param components
	 * @return
	 */
	public static VerticalLayout getVerticalWrapper(Component... components) {
		final VerticalLayout layout = new VerticalLayout(components);
		layout.setSpacing(true);
		return layout;
	}

	public static VerticalLayout getVerticalWrapperNoMargin(Component... components) {
		final VerticalLayout layout = new VerticalLayout(components);
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(false);
		return layout;
	}

	public static VerticalLayout getVerticalWrapperWithMargin(Component... components) {
		final VerticalLayout layout = new VerticalLayout(components);
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(true);
		return layout;
	}

	/**
	 * Возвращет HorizontalLayout для перечисленных компонентов
	 *
	 * @param components
	 * @return
	 */
	public static HorizontalLayout getHorizontalWrapper(Component... components) {
		final HorizontalLayout layout = new HorizontalLayout(components);
		layout.setSpacing(true);
		return layout;
	}

	public static HorizontalLayout getHorizontalWrapperNoMargin(Component... components) {
		final HorizontalLayout layout = new HorizontalLayout(components);
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(false);
		return layout;
	}

	public static HorizontalLayout getHorizontalWrapperWithMargin(Component... components) {
		final HorizontalLayout layout = new HorizontalLayout(components);
		layout.setSizeFull();
		layout.setSpacing(true);
		layout.setMargin(true);
		return layout;
	}

	public static void buildNotification(String message, Notification.Type type) {
		Notification.show(message, type);
	}

	public static void buildErrorNotification(String message) {
		buildNotification(message, Notification.Type.ERROR_MESSAGE);
	}

	public static void buildMainNotification(String message) {
		buildNotification(message, Notification.Type.HUMANIZED_MESSAGE);
	}

	public static void buildTrayNotification(String message) {
		buildNotification(message, Notification.Type.TRAY_NOTIFICATION);
	}

	public static void buildWarningNotification(String message) {
		buildNotification(message, Notification.Type.WARNING_MESSAGE);
	}
}
