package com.givee.demo.client;

import com.givee.demo.client.domain.LoginDto;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.AppRestTemplate;
import com.givee.demo.client.event.Broadcaster;
import com.givee.demo.client.event.EventManager;
import com.givee.demo.client.event.user.UserLoginEvent;
import com.givee.demo.client.event.user.UserLogoutEvent;
import com.givee.demo.client.view.LoginScreen;
import com.givee.demo.client.view.MainScreen;
import com.google.common.eventbus.Subscribe;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.navigator.PushStateNavigation;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.JavaScriptFunction;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.scheduling.TaskScheduler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@SpringUI()
@Title("Demo App")
@Theme(AppTheme.THEME_NAME)
@PushStateNavigation
@PreserveOnRefresh
@Push
public class AppUI extends UI implements Broadcaster.BroadcastListener, HasLogger {
	private AppEventBus eventBus = new AppEventBus();
	private AppRestTemplate restTemplate = new AppRestTemplate();
	private final Map<UI, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

	@Autowired
	private EventManager eventManager;
	@Autowired
	private TaskScheduler taskScheduler;

	public static AppEventBus getEventBus() {
		return ((AppUI) getCurrent()).eventBus;
	}

	public static AppRestTemplate getRestTemplate() {
		return ((AppUI) getCurrent()).restTemplate;
	}

	public static LoginDto getCurrentLogin() {
		return VaadinSession.getCurrent().getAttribute(LoginDto.class);
	}

	public static void setCurrentLogin(LoginDto currentLogin) {
		VaadinSession.getCurrent().setAttribute(LoginDto.class, currentLogin);
	}

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setLocale(Locale.US);
		AppEventBus.register(this);
		Broadcaster.register(this);
		Responsive.makeResponsive(this);

		WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
		getLogger().debug("Address: {}", webBrowser.getAddress());
		getLogger().debug("Device too old: {}", webBrowser.isTooOldToFunctionProperly());
		getLogger().debug("Touch device: {}", webBrowser.isTouchDevice());
		getLogger().debug("BrowserApplication: {}", webBrowser.getBrowserApplication());
		getLogger().debug("Screen: {} x {}", webBrowser.getScreenWidth(), webBrowser.getScreenHeight());

		updateContent();

		JavaScript javaScript = Page.getCurrent().getJavaScript();
		javaScript.addFunction("closeMyApplication", (JavaScriptFunction) arguments -> close());
		if (webBrowser.isChrome() || webBrowser.isOpera()) {
			javaScript.execute("window.onunload = function (e) { var e = e || window.event; closeMyApplication(); return; };");
		} else {
			javaScript.execute("window.onbeforeunload = function (e) { var e = e || window.event; closeMyApplication(); return; };");
		}
	}

	private void updateContent() {
		if (getCurrentLogin() != null) {
			setContent(new MainScreen());
			getNavigator().navigateTo(getNavigator().getState());
			ScheduledFuture<?> future = taskScheduler.scheduleAtFixedRate(this::updateViews, 60000);
			getLogger().debug("Starting update task in UI: " + this.getSession().getCsrfToken());
			scheduledTasks.put(this, future);
		} else {
			setContent(new LoginScreen());
		}
	}

	private void updateViews() {
		eventManager.publishUpdateEvent(this);
	}

	@Override
	public void detach() {
		getLogger().debug("Detaching " + this.getSession().getCsrfToken());
		getLogger().debug("Stopping update task in UI: " + this.getSession().getCsrfToken());
		if (scheduledTasks.containsKey(this)) {
			ScheduledFuture<?> future = scheduledTasks.get(this);
			future.cancel(true);
			scheduledTasks.remove(this);
		}
		AppEventBus.unregister(this);
		Broadcaster.unregister(this);
		super.detach();
	}

	@Override
	public void receiveBroadcast(Object object) {
		if (object instanceof ApplicationEvent) {
			ApplicationEvent event = (ApplicationEvent) object;
			//logger.debug("Event source: {}", ((UI) event.getSource()).getSession().getCsrfToken());
			if (event.getSource().equals(this)) {
				access(() -> AppEventBus.post(object));
			}
		}
	}

	@Subscribe
	public void handleLoginEvent(final UserLoginEvent event) {
		if (event.getSource().equals(this)) {
			//getLogger().debug("Handle Login event: " + event.getUser());
			//access(() -> refresh(VaadinRequest.getCurrent()));
			updateContent();
		}
	}

	@Subscribe
	public void handleLogoutEvent(final UserLogoutEvent event) {
		if (event.getSource().equals(this)) {
			//getLogger().debug("Handle Logout event: " + event.getUser());
			//access(() -> refresh(VaadinRequest.getCurrent()));
			getUI().getSession().close();
			getPage().reload();
		}
	}
}
