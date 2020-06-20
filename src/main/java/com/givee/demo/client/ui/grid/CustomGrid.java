package com.givee.demo.client.ui.grid;

import com.givee.demo.client.HasLogger;
import com.givee.demo.client.domain.AbstractEntity;
import com.givee.demo.client.domain.AbstractEntityGroup;
import com.givee.demo.client.utils.StringUtil;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.shared.ui.grid.ScrollDestination;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;
import com.vaadin.ui.components.grid.HeaderRow;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public abstract class CustomGrid<T extends AbstractEntity> extends Grid<T> implements HasLogger {
	private static final int LAYOUT_WIDTH = 25;

	private final List<GridColumn<T>> columns;
	private final AbstractEntityGroup<T> entityGroup;
	private final ListDataProvider<T> dataProvider;

	public CustomGrid(List<GridColumn<T>> columns, List<T> items) {
		this.columns = columns;
		this.entityGroup = new AbstractEntityGroup<>(items);
		this.dataProvider = new ListDataProvider<T>(entityGroup.values()) {
			@Override
			public Object getId(T item) {
				return item.getId();
			}
		};
		setSizeFull();
		setDataProvider(dataProvider);
		setItems(entityGroup.values());
		setSelectionMode(SelectionMode.SINGLE);
		setColumnReorderingAllowed(true);
		setColumnResizeMode(ColumnResizeMode.ANIMATED);
		initColumns();

		addItemClickListener(event -> {
			if (event.getMouseEventDetails().getButton().equals(MouseEventDetails.MouseButton.LEFT)) {
				if (event.getMouseEventDetails().isDoubleClick()) {
					onDoubleClick(event.getItem(), event.getColumn());
				} else {
					onClick(event.getItem(), event.getColumn());
				}
			}
		});
	}

	private void initColumns() {
		for (GridColumn<T> column : columns) {
			if (column.isComponent()) {
				Column<T, ?> col = addComponentColumn((ValueProvider<T, Component>) column.getValueProvider())
						.setId(column.getId()).setCaption(column.getCaption());
				col.setWidth(LAYOUT_WIDTH);
				col.setResizable(false);
				col.setSortable(false);
				//((Column<T, Component>) col).setRenderer(new ComponentRenderer());
			} else {
				Column<T, ?> col = addColumn(column.getValueProvider()).setId(column.getId()).setCaption(column.getCaption());
				col.setDescriptionGenerator(column.getDescriptionGenerator());
				if (column.isDate())
					col.setComparator(new GridDateComparator<>(column.getValueProvider()));
				getHeader().getDefaultRow().getCell(col).setDescription(column.getCaption());
			}
		}
	}

	public void initFilters() {
		HeaderRow filterRow = appendHeaderRow();
		for (GridColumn<T> column : columns) {
			if (!column.isComponent()) {
				TextField filterField = new TextField();
				filterField.setValueChangeMode(ValueChangeMode.EAGER);
				filterField.addValueChangeListener(event -> dataProvider.addFilter(t ->
						StringUtil.containsIgnoreCase(String.valueOf(column.getValueProvider().apply(t)), filterField.getValue())));
				filterRow.getCell(column.getId()).setComponent(filterField);
				filterField.setSizeFull();
				filterField.setPlaceholder("Filter");
			}
		}
	}

	public void hideColumns(String... columnIds) {
		for (String id : columnIds) {
			getColumn(id).setHidden(true);
		}
	}

	public void showColumns(String... columnIds) {
		for (String id : columnIds) {
			getColumn(id).setHidden(false);
		}
	}

	public void update(List<T> items) {
		entityGroup.update(items);
		dataProvider.refreshAll();
	}

	public void refreshItem(T item) {
		entityGroup.update(item);
		dataProvider.refreshItem(item);
	}

	public Boolean isSelected(T item) {
		return getSelectedItems().contains(item);
	}

	public void scrollTo(T item) {
		if (item != null) {
			int i = getItems().indexOf(item);
			if (i >= 0)
				scrollTo(i, ScrollDestination.MIDDLE);
		}
	}

	public void scrollTo(Long itemId) {
		scrollTo(entityGroup.findById(itemId));
	}

	public void select(Long itemId) {
		select(entityGroup.findById(itemId));
	}

	public void refreshAll() {
		dataProvider.refreshAll();
	}

	public T getSelectedRow() {
		return getSelectedItems().stream().findFirst().orElse(null);
	}

	public List<T> getItems() {
		return new ArrayList<>(dataProvider.getItems());
	}

	private void onClick(final T entity, final Column column) {
		Set<T> selectedItems = getSelectedItems();
		deselectAll();
		if (selectedItems.contains(entity) && selectedItems.size() == 1)
			deselect(entity);
		else
			select(entity);
	}

	protected abstract void onDoubleClick(final T entity, final Column column);

	@Override
	public void setItems(Collection<T> items) {
		//super.setItems(items);
		update(new ArrayList<>(items));
	}
}
