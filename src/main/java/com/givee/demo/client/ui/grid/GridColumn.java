package com.givee.demo.client.ui.grid;

import com.givee.demo.client.utils.DatesUtil;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.Setter;
import com.vaadin.ui.Component;
import com.vaadin.ui.DescriptionGenerator;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class GridColumn<T> {
	private String id, caption;
	private ValueProvider<T, ?> valueProvider;
	private boolean date, component;

	public GridColumn(String id, String caption, ValueProvider<T, ?> valueProvider) {
		this(id, caption, valueProvider, false, false);
	}

	public GridColumn(String id, String caption, ValueProvider<T, ?> valueProvider, boolean date, boolean component) {
		this.id = id;
		this.caption = caption;
		this.valueProvider = valueProvider;
		this.date = date;
		this.component = component;
	}

	public static <V> GridColumn<V> createColumn(String id, String caption, ValueProvider<V, ?> valueProvider) {
		return new GridColumn<>(id, caption, valueProvider);
	}

	public static <V> GridColumn<V> createBooleanColumn(String id, String caption, ValueProvider<V, Boolean> valueProvider, String text) {
		return new GridColumn<>(id, caption, booleanProvider(valueProvider, text));
	}

	public static <V> GridColumn<V> createComponentColumn(String id, String caption, ValueProvider<V, Component> valueProvider) {
		return new GridColumn<>(id, caption, valueProvider, false, true);
	}

	public static <V> GridColumn<V> createDateColumn(String id, String caption, ValueProvider<V, LocalDateTime> valueProvider) {
		return new GridColumn<>(id, caption, dateProvider(valueProvider), true, false);
	}

	public static String nullProvider(Object o) {
		return null;
	}

	public static String dateProvider(ZonedDateTime o) {
		return DatesUtil.zonedDateTimeToString(o, DatesUtil.GRID_DATE_FORMAT);
	}

	public static <V> ValueProvider<V, String> booleanProvider(ValueProvider<V, Boolean> provider, String text) {
		return (ValueProvider<V, String>) entity -> provider.apply(entity) ? text : "";
	}

	public static <V> ValueProvider<V, String> dateProvider(ValueProvider<V, LocalDateTime> provider) {
		return (ValueProvider<V, String>) entity ->
				DatesUtil.localDateTimeToString(provider.apply(entity), DatesUtil.GRID_DATE_FORMAT);
	}

	public static <V> ValueProvider<V, LocalDateTime> dateGetter(ValueProvider<V, ZonedDateTime> provider) {
		return (ValueProvider<V, LocalDateTime>) entity ->
				DatesUtil.zonedToLocal(provider.apply(entity));
	}

	public static <V> Setter<V, LocalDateTime> dateSetter(Setter<V, ZonedDateTime> setter) {
		return (Setter<V, LocalDateTime>) (entity, localDateTime) ->
				setter.accept(entity, DatesUtil.localToZoned(localDateTime));
	}

	public String getId() {
		return id;
	}

	public String getCaption() {
		return caption;
	}

	public ValueProvider<T, ?> getValueProvider() {
		return valueProvider;
	}

	public boolean isDate() {
		return date;
	}

	public boolean isComponent() {
		return component;
	}

	public DescriptionGenerator<T> getDescriptionGenerator() {
		return (DescriptionGenerator<T>) t -> {
			Object apply = getValueProvider().apply(t);
			return apply == null ? null : String.valueOf(apply);
		};
	}
}
