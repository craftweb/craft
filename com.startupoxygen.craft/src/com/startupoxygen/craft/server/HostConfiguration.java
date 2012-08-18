package com.startupoxygen.craft.server;

public class HostConfiguration {
	private String httpHost;
	private int httpPort;
	private int httpsPort;

	public HostConfiguration() {
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