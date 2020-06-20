package com.givee.demo.client.view;

import com.givee.demo.client.HasLogger;
import com.givee.demo.client.utils.CompsUtil;
import com.vaadin.ui.*;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class MainScreen extends HorizontalLayout implements HasLogger {
	public MainScreen() {
		setSizeFull();
		addStyleName("mainview");
		setSpacing(false);

		addComponent(new MainMenu());
		ComponentContainer content = new CssLayout();
		content.addStyleName("view-content");
		content.setSizeFull();

		Label footer = new Label(LocalDateTime.now().getYear() + " г.");
		footer.setSizeUndefined();

		VerticalLayout verticalLayout = CompsUtil.getVerticalWrapperNoMargin(content, footer);
		verticalLayout.setExpandRatio(content, 1);
		verticalLayout.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);

		addComponent(verticalLayout);
		setExpandRatio(verticalLayout, 1);

		new AppNavigator(content);
	}
}
