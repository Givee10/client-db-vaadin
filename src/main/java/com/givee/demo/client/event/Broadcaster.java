package com.givee.demo.client.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Broadcaster {
	private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<>();

	public static void register(BroadcastListener listener) {
		listeners.add(listener);
	}

	public static void unregister(BroadcastListener listener) {
		listeners.remove(listener);
	}

	public static void broadcast(Object object) {
		for (BroadcastListener listener : listeners) {
			listener.receiveBroadcast(object);
		}
	}

	public interface BroadcastListener {
		void receiveBroadcast(final Object object);
	}
}
