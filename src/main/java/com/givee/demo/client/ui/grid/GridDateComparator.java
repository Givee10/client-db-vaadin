package com.givee.demo.client.ui.grid;

import com.givee.demo.client.utils.DatesUtil;
import com.vaadin.data.ValueProvider;
import com.vaadin.server.SerializableComparator;

import java.time.LocalDateTime;

public class GridDateComparator<T> implements SerializableComparator<T> {
	private final ValueProvider<T, ?> valueProvider;

	public GridDateComparator(ValueProvider<T, ?> valueProvider) {
		this.valueProvider = valueProvider;
	}

	@Override
	public int compare(T o1, T o2) {
		String a1 = String.valueOf(valueProvider.apply(o1));
		String a2 = String.valueOf(valueProvider.apply(o2));
		LocalDateTime t1 = DatesUtil.stringToLocal(a1);
		LocalDateTime t2 = DatesUtil.stringToLocal(a2);
		if (t1 == null && t2 == null) return 0;
		if (t1 != null && t2 == null) return 1;
		if (t1 == null && t2 != null) return -1;
		return t1.compareTo(t2);
	}
}
