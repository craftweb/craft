package com.startupoxygen.craft.server;

import org.eclipse.jetty.server.handler.AbstractHandler;

class WebApp {

	public boolean customContextHandler;
	public String contextPath;
	public String appDir;
	public String[] hostName;
	public AbstractHandler jettyHandler;

	public WebApp(boolean argCustomContextHandler, String argContextPath,
			String argAppDir, String... argHostName) {
		this.customContextHandler = argCustomContextHandler;
		this.contextPath = argContextPath;
		this.appDir = argAppDir;
		this.hostName = argHostName;
	}

	public WebApp(boolean argCustomContextHandler,
			AbstractHandler argJettyHandler, String argContextPath,
			String... argHostName) {
		this.customContextHandler = argCustomContextHandler;
		this.jettyHandler = argJettyHandler;
		this.contextPath = argContextPath;
		this.hostName = argHostName;
	}

}