package com.givee.demo.client.view;

import com.givee.demo.client.AppTheme;
import com.givee.demo.client.AppUI;
import com.givee.demo.client.HasLogger;
import com.givee.demo.client.domain.LoginDto;
import com.givee.demo.client.event.AppEventBus;
import com.givee.demo.client.event.user.UserLoginEvent;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginScreen extends VerticalLayout implements HasLogger {
	private TextField userName;
	private PasswordField passwordField;
	private TextField dbName;
	private Button login;

	public LoginScreen() {
		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();
		userName = new TextField("Логин");
		userName.setIcon(AppTheme.ICON_PERSON);
		//userName.focus();
		passwordField = new PasswordField("Пароль");
		passwordField.setIcon(AppTheme.ICON_PASS);
		dbName = new TextField("БД");
		dbName.setIcon(AppTheme.ICON_DATABASE);
		login = new Button("Войти");
		loginForm.addComponent(dbName);
		loginForm.addComponent(userName);
		loginForm.addComponent(passwordField);
		loginForm.addComponent(login);
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addClickListener($ -> login());

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSizeUndefined();
		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		addComponent(loginLayout);
		setSizeFull();
		setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);
	}

	private void login() {
		login.setEnabled(false);
		String user = userName.getValue();
		String pass = passwordField.getValue();
		String name = dbName.getValue();
		passwordField.setValue("");

		if (user.trim().isEmpty() || pass.trim().isEmpty()) {
			Notification.show("Значение не может быть пустым", Notification.Type.WARNING_MESSAGE);
			login.setEnabled(true);
			return;
		}

		LoginDto loginDto = new LoginDto(name, user, pass);
		AppUI.setCurrentLogin(loginDto);

		if (AppUI.getRestTemplate().doLogin() != null) {
			AppEventBus.post(new UserLoginEvent(getUI(), loginDto));
		} else {
			Notification.show("Неверный логин или пароль", Notification.Type.ERROR_MESSAGE);
			AppUI.setCurrentLogin(null);
		}
	}
}
