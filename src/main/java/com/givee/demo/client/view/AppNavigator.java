package com.givee.demo.client.view;

import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.PostViewChangeEvent;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.navigator.ViewProvider;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

public class AppNavigator extends Navigator {
	private static final AppViewType ERROR_VIEW = AppViewType.MAIN;
	private ViewProvider errorViewProvider;

	public AppNavigator(final ComponentContainer container) {
		super(UI.getCurrent(), container);

		initViewChangeListener();
		initViewProviders();
	}

	private void initViewChangeListener() {
		addViewChangeListener(new ViewChangeListener() {

			@Override
			public boolean beforeViewChange(final ViewChangeEvent event) {
				// Since there's no conditions in switching between the views
				// we can always return true.
				return true;
			}

			@Override
			public void afterViewChange(final ViewChangeEvent event) {
				AppViewType view = AppViewType.getByViewName(event.getViewName());
				// Appropriate events get fired after the view is changed.
				AppEventBus.post(new PostViewChangeEvent(view));
			}
		});
	}

	private void initViewProviders() {
		// A dedicated view provider is added for each separate view type
		for (final AppViewType viewType : AppViewType.values()) {
			ViewProvider viewProvider = new ClassBasedViewProvider(
					viewType.getViewName(), viewType.getViewClass()) {

				// This field caches an already initialized view instance if the
				// view should be cached (stateful views).
				private View cachedInstance;

				@Override
				public View getView(final String viewName) {
					View result = null;
					if (viewType.getViewName().equals(viewName)) {
						if (cachedInstance == null) {
							cachedInstance = super.getView(viewType.getViewName());
						}
						result = cachedInstance;
					}
					return result;
				}
			};

			if (viewType == ERROR_VIEW) {
				errorViewProvider = viewProvider;
			}

			addProvider(viewProvider);
		}

		setErrorProvider(new ViewProvider() {
			@Override
			public String getViewName(final String viewAndParameters) {
				return ERROR_VIEW.getViewName();
			}

			@Override
			public View getView(final String viewName) {
				return errorViewProvider.getView(ERROR_VIEW.getViewName());
			}
		});
	}
}
