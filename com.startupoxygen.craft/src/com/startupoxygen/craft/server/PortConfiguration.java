package com.startupoxygen.craft.server;

public class PortConfiguration {
	private String httpHost;
	private int httpPort;
	private int httpsPort;

	public PortConfiguration() {
		// Default Constructor
	}

	public String getHttpHost() {
		return this.httpHost;
	}

	public void setHttpHost(String argHttpHost) {
		this.httpHost = argHttpHost;
	}

	public int getHttpPort() {
		return this.httpPort;
	}

	public void setHttpPort(int argHttpPort) {
		this.httpPort = argHttpPort;
	}

	public int getHttpsPort() {
		return this.httpsPort;
	}

	public void setHttpsPort(int argHttpsPort) {
		this.httpsPort = argHttpsPort;
	}

}