package com.startupoxygen.craft.server;

public class RequestLogConfiguration {
	private String accessLogTimeZone;
	private boolean accessLogEnabled;
	private String accessLogFilename;
	private String accessLogDirectory;
	private boolean statsOn;
	private boolean accessLogAppend;
	private int accessLogRetainDays;
	private boolean accessLogServerName;
	private boolean accessLogExtended;
	private boolean accessLogEnableLatency;
	private boolean accessLogCookies;

	public RequestLogConfiguration() {
		// Default Constructor
	}

	public boolean isAccessLogEnabled() {
		return this.accessLogEnabled;
	}

	public void setAccessLogEnabled(boolean argAccessLogEnabled) {
		this.accessLogEnabled = argAccessLogEnabled;
	}

	public String getAccessLogDirectory() {
		return this.accessLogDirectory;
	}

	public void setAccessLogDirectory(String argAccessLogDirectory) {
		this.accessLogDirectory = argAccessLogDirectory;
	}

	public String getAccessLogFilename() {
		return this.accessLogFilename;
	}

	public void setAccessLogFilename(String argAccessLogFilename) {
		this.accessLogFilename = argAccessLogFilename;
	}

	public int getAccessLogRetainDays() {
		return this.accessLogRetainDays;
	}

	public void setAccessLogRetainDays(int argAccessLogRetainDays) {
		this.accessLogRetainDays = argAccessLogRetainDays;
	}

	public boolean isAccessLogAppend() {
		return this.accessLogAppend;
	}

	public void setAccessLogAppend(boolean argAccessLogAppend) {
		this.accessLogAppend = argAccessLogAppend;
	}

	public boolean isAccessLogExtended() {
		return this.accessLogExtended;
	}

	public void setAccessLogExtended(boolean argAccessLogExtended) {
		this.accessLogExtended = argAccessLogExtended;
	}

	public String getAccessLogTimeZone() {
		return this.accessLogTimeZone;
	}

	public void setAccessLogTimeZone(String argAccessLogTimeZone) {
		this.accessLogTimeZone = argAccessLogTimeZone;
	}

	public boolean isAccessLogServerName() {
		return this.accessLogServerName;
	}

	public void setAccessLogServerName(boolean argAccessLogServerName) {
		this.accessLogServerName = argAccessLogServerName;
	}

	public boolean isAccessLogCookies() {
		return this.accessLogCookies;
	}

	public void setAccessLogCookies(boolean argAccessLogCookies) {
		this.accessLogCookies = argAccessLogCookies;
	}

	public boolean isAccessLogEnableLatency() {
		return this.accessLogEnableLatency;
	}

	public void setAccessLogEnableLatency(boolean argAccessLogEnableLatency) {
		this.accessLogEnableLatency = argAccessLogEnableLatency;
	}

	public boolean isStatsOn() {
		return this.statsOn;
	}

	public void setStatsOn(boolean argStatsOn) {
		this.statsOn = argStatsOn;
	}

}