package com.givee.demo.client;

import com.vaadin.server.VaadinServlet;

import javax.servlet.ServletException;

public class AppServlet extends VaadinServlet {
	@Override
	protected final void servletInitialized() throws ServletException {
		super.servletInitialized();
		getService().addSessionInitListener(new AppSessionInitListener());
	}
}
