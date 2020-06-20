package com.givee.demo.client.event;

import com.givee.demo.client.HasLogger;
import com.givee.demo.client.domain.LoginDto;
import com.givee.demo.client.event.user.UserLoginEvent;
import com.givee.demo.client.event.user.UserLogoutEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class EventManager implements ApplicationListener<ApplicationEvent>, HasLogger {
	private final ApplicationEventPublisher applicationEventPublisher;

	@Autowired
	public EventManager(ApplicationEventPublisher applicationEventPublisher) {
		this.applicationEventPublisher = applicationEventPublisher;
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof UserLoginEvent) {
			//getLogger().debug("New Login Event: " + ((UserLoginEvent) event).getUser());
			AppEventBus.post(event);
		}
		if (event instanceof UserLogoutEvent) {
			//getLogger().debug("New Logout Event: " + ((UserLogoutEvent) event).getUser());
			AppEventBus.post(event);
		}
		if (event instanceof UpdateViewEvent) {
			//getLogger().debug("New Update View Event");
			Broadcaster.broadcast(event);
		}
	}

	public void publishLoginEvent(UI ui, LoginDto userDto) {
		getLogger().debug("Publishing Login event");
		UserLoginEvent event = new UserLoginEvent(ui, userDto);
		applicationEventPublisher.publishEvent(event);
	}

	public void publishLogoutEvent(UI ui, LoginDto userDto) {
		getLogger().debug("Publishing Logout event");
		UserLogoutEvent event = new UserLogoutEvent(ui, userDto);
		applicationEventPublisher.publishEvent(event);
	}

	public void publishUpdateEvent(UI ui) {
		//getLogger().debug("Publishing Update View event");
		UpdateViewEvent event = new UpdateViewEvent(ui);
		applicationEventPublisher.publishEvent(event);
	}
}
