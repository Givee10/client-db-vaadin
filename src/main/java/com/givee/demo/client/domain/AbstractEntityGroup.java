package com.givee.demo.client.domain;

import java.util.*;

public class AbstractEntityGroup<T extends AbstractEntity> {
	private Map<Long, T> entityMap = new HashMap<>();

	public AbstractEntityGroup(List<T> entities) {
		entities.forEach(abstractEntity -> this.entityMap.put(abstractEntity.getId(), abstractEntity));
	}

	public Set<Long> keys() {
		return this.entityMap.keySet();
	}

	public Collection<T> values() {
		return this.entityMap.values();
	}

	public void update(List<T> list) {
		List<Long> keysToDelete = new ArrayList<>();
		List<Long> listKeys = new ArrayList<>();
		list.forEach(entity -> {
			listKeys.add(entity.getId());
			update(entity);
		});
		keys().forEach(key -> {
			if (!listKeys.contains(key)) keysToDelete.add(key);
		});
		keysToDelete.forEach(key -> entityMap.remove(key));
	}

	public void update(T entity) {
		Long id = entity.getId();
		if (keys().contains(id)) {
			entityMap.replace(id, entity);
		} else {
			entityMap.put(id, entity);
		}
	}

	public T findById(Long id) {
		return entityMap.getOrDefault(id, null);
	}
}
