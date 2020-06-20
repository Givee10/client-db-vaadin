package com.givee.demo.client.event;

import com.givee.demo.client.AppUI;
import com.givee.demo.client.HasLogger;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;

public class AppEventBus implements SubscriberExceptionHandler, HasLogger {
	private final EventBus eventBus = new EventBus(this);

	public static void post(final Object event) {
		AppUI.getEventBus().eventBus.post(event);
	}

	public static void register(final Object object) {
		AppUI.getEventBus().eventBus.register(object);
	}

	public static void unregister(final Object object) {
		AppUI.getEventBus().eventBus.unregister(object);
	}

	@Override
	public final void handleException(final Throwable exception, final SubscriberExceptionContext context) {
		getLogger().error("Error in subscriber: " + context.getSubscriber(), exception);
	}
}
