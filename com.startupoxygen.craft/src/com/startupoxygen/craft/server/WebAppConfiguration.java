package com.startupoxygen.craft.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.handler.AbstractHandler;

public class WebAppConfiguration {

	private List<WebApp> webApps;

	public List<WebApp> getWebApps() {
		return webApps;
	}

	public WebAppConfiguration() {
		this.webApps = new ArrayList<WebApp>();
	}
	
	public void setWebApps(List<WebApp> argWebApps) {
		if (argWebApps != null && !argWebApps.isEmpty()) {
			this.webApps = argWebApps;
		}
	}

	public void addCustomContextHandler(
			AbstractHandler argCustomContextHandler, String argContextPath,
			String... argHostName) {
		if (this.webApps == null) {
			this.webApps = new ArrayList<WebApp>();
		}
		this.webApps.add(new WebApp(true, argCustomContextHandler,
				argContextPath, argHostName));
	}

	public void addWebApp(String argContextPath, String argAppDir,
			String... argHostName) {
		if (this.webApps == null) {
			this.webApps = new ArrayList<WebApp>();
		}
		this.webApps.add(new WebApp(false, argContextPath, argAppDir,
				argHostName));
	}

}