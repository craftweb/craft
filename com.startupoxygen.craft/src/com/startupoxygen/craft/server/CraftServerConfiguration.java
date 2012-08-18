package com.startupoxygen.craft.server;

public class CraftServerConfiguration {
	private HostConfiguration hostConfiguration;
	private HttpConfiguration httpConfiguration;
	private KeystoreConfiguration keystoreConfiguration;
	private CipherSuitesConfiguration cipherSuitesConfiguration;
	private ProcessConfiguration processConfiguration;
	private ThreadPoolConfiguration threadPoolConfiguration;
	private WebAppConfiguration webAppConfiguration;
	private RequestLogConfiguration requestLogConfiguration;

	public CraftServerConfiguration() {
		// Default Constructor
	}

	public HostConfiguration getHostConfiguration() {
		return this.hostConfiguration;
	}

	public void setHostConfiguration(HostConfiguration argHostConfiguration) {
		this.hostConfiguration = argHostConfiguration;
	}

	public HttpConfiguration getHttpConfiguration() {
		return this.httpConfiguration;
	}

	public void setHttpConfiguration(HttpConfiguration argHttpConfiguration) {
		this.httpConfiguration = argHttpConfiguration;
	}

	public KeystoreConfiguration getKeystoreConfiguration() {
		return this.keystoreConfiguration;
	}

	public void setKeystoreConfiguration(
			KeystoreConfiguration argKeystoreConfiguration) {
		this.keystoreConfiguration = argKeystoreConfiguration;
	}

	public CipherSuitesConfiguration getCipherSuitesConfiguration() {
		return this.cipherSuitesConfiguration;
	}

	public void setCipherSuitesConfiguration(
			CipherSuitesConfiguration argCipherSuitesConfiguration) {
		this.cipherSuitesConfiguration = argCipherSuitesConfiguration;
	}

	public ProcessConfiguration getProcessConfiguration() {
		return this.processConfiguration;
	}

	public void setProcessConfiguration(
			ProcessConfiguration argProcessConfiguration) {
		this.processConfiguration = argProcessConfiguration;
	}

	public ThreadPoolConfiguration getThreadPoolConfiguration() {
		return this.threadPoolConfiguration;
	}

	public void setThreadPoolConfiguration(
			ThreadPoolConfiguration argThreadPoolConfiguration) {
		this.threadPoolConfiguration = argThreadPoolConfiguration;
	}

	public WebAppConfiguration getWebAppConfiguration() {
		return this.webAppConfiguration;
	}

	public void setWebAppConfiguration(
			WebAppConfiguration argWebAppConfiguration) {
		this.webAppConfiguration = argWebAppConfiguration;
	}

	public RequestLogConfiguration getRequestLogConfiguration() {
		return this.requestLogConfiguration;
	}

	public void setRequestLogConfiguration(
			RequestLogConfiguration argLogginConfiguration) {
		this.requestLogConfiguration = argLogginConfiguration;
	}

	public String getHttpHost() {
		return this.hostConfiguration.getHttpHost();
	}

	public void setHttpHost(String argHttpHost) {
		this.hostConfiguration.setHttpHost(argHttpHost);
	}

	public int getHttpPort() {
		return this.hostConfiguration.getHttpPort();
	}

	public void setHttpPort(int argHttpPort) {
		this.hostConfiguration.setHttpPort(argHttpPort);
	}

	public int getHttpsPort() {
		return this.hostConfiguration.getHttpsPort();
	}

	public void setHttpsPort(int argHttpsPort) {
		this.hostConfiguration.setHttpsPort(argHttpsPort);
	}

	public int getHttpRequestHeaderSize() {
		return this.httpConfiguration.getHttpRequestHeaderSize();
	}

	public void setHttpRequestHeaderSize(int argHttpRequestHeaderSize) {
		this.httpConfiguration
				.setHttpRequestHeaderSize(argHttpRequestHeaderSize);
	}

	public int getHttpsRequestHeaderSize() {
		return this.httpConfiguration.getHttpsRequestHeaderSize();
	}

	public void setHttpsRequestHeaderSize(int argHttpsRequestHeaderSize) {
		this.httpConfiguration
				.setHttpsRequestHeaderSize(argHttpsRequestHeaderSize);
	}

	public int getHttpResponseHeaderSize() {
		return this.httpConfiguration.getHttpResponseHeaderSize();
	}

	public void setHttpResponseHeaderSize(int argHttpResponseHeaderSize) {
		this.httpConfiguration
				.setHttpResponseHeaderSize(argHttpResponseHeaderSize);
	}

	public int getHttpsResponseHeaderSize() {
		return this.httpConfiguration.getHttpsResponseHeaderSize();
	}

	public void setHttpsResponseHeaderSize(int argHttpsResponseHeaderSize) {
		this.httpConfiguration
				.setHttpsResponseHeaderSize(argHttpsResponseHeaderSize);
	}

	public String getKeystoreFile() {
		return this.keystoreConfiguration.getKeystoreFile();
	}

	public void setKeystoreFile(String argKeystoreFile) {
		this.keystoreConfiguration.setKeystoreFile(argKeystoreFile);
	}

	public String getKeystorePassword() {
		return this.keystoreConfiguration.getKeystorePassword();
	}

	public void setKeystorePassword(String argKeystorePassword) {
		this.keystoreConfiguration.setKeystorePassword(argKeystorePassword);
	}

	public String getKeyPassowrd() {
		return this.keystoreConfiguration.getKeyPassowrd();
	}

	public void setKeyPassowrd(String argKeyPassowrd) {
		this.keystoreConfiguration.setKeyPassowrd(argKeyPassowrd);
	}

	public String[] getIncluded() {
		return this.cipherSuitesConfiguration.getIncluded();
	}

	public void setIncluded(String[] argIncluded) {
		this.cipherSuitesConfiguration.setIncluded(argIncluded);
	}

	public String[] getExcluded() {
		return this.cipherSuitesConfiguration.getExcluded();
	}

	public void setExcluded(String[] argExcluded) {
		this.cipherSuitesConfiguration.setExcluded(argExcluded);
	}

	public String getUserName() {
		return this.processConfiguration.getUserName();
	}

	public void setUserName(String argUserName) {
		this.processConfiguration.setUserName(argUserName);
	}

	public String getGroupName() {
		return this.processConfiguration.getGroupName();
	}

	public void setGroupName(String argGroupName) {
		this.processConfiguration.setGroupName(argGroupName);
	}

	public String getUmask() {
		return this.processConfiguration.getUmask();
	}

	public void setUmask(String argUmask) {
		this.processConfiguration.setUmask(argUmask);
	}

	public boolean isStartAsPrivileged() {
		return this.processConfiguration.isStartAsPrivileged();
	}

	public void setStartAsPrivileged(boolean argStartAsPrivileged) {
		this.processConfiguration.setStartAsPrivileged(argStartAsPrivileged);
	}

	public int getThreadPoolSize() {
		return this.threadPoolConfiguration.getThreadPoolSize();
	}

	public void setThreadPoolSize(int argThreadPoolSize) {
		this.threadPoolConfiguration.setThreadPoolSize(argThreadPoolSize);
	}

	public int getAcceptorThreadSize() {
		return this.threadPoolConfiguration.getAcceptorThreadSize();
	}

	public void setAcceptorThreadSize(int argAcceptorThreadSize) {
		this.threadPoolConfiguration
				.setAcceptorThreadSize(argAcceptorThreadSize);
	}

	public int getAcceptorQueueSize() {
		return this.threadPoolConfiguration.getAcceptorQueueSize();
	}

	public void setAcceptorQueueSize(int argAcceptorQueueSize) {
		this.threadPoolConfiguration.setAcceptorQueueSize(argAcceptorQueueSize);
	}

	public int getMaxIdleTime() {
		return this.threadPoolConfiguration.getMaxIdleTime();
	}

	public void setMaxIdleTime(int argMaxIdleTime) {
		this.threadPoolConfiguration.setMaxIdleTime(argMaxIdleTime);
	}

	public boolean isAccessLogEnabled() {
		return this.requestLogConfiguration.isAccessLogEnabled();
	}

	public void setAccessLogEnabled(boolean argAccessLogEnabled) {
		this.requestLogConfiguration.setAccessLogEnabled(argAccessLogEnabled);
	}

	public String getAccessLogDirectory() {
		return this.requestLogConfiguration.getAccessLogDirectory();
	}

	public void setAccessLogDirectory(String argAccessLogDirectory) {
		this.requestLogConfiguration.setAccessLogDirectory(argAccessLogDirectory);
	}

	public String getAccessLogFilename() {
		return this.requestLogConfiguration.getAccessLogFilename();
	}

	public void setAccessLogFilename(String argAccessLogFilename) {
		this.requestLogConfiguration.setAccessLogFilename(argAccessLogFilename);
	}

	public int getAccessLogRetainDays() {
		return this.requestLogConfiguration.getAccessLogRetainDays();
	}

	public void setAccessLogRetainDays(int argAccessLogRetainDays) {
		this.requestLogConfiguration
				.setAccessLogRetainDays(argAccessLogRetainDays);
	}

	public boolean isAccessLogAppend() {
		return this.requestLogConfiguration.isAccessLogAppend();
	}

	public void setAccessLogAppend(boolean argAccessLogAppend) {
		this.requestLogConfiguration.setAccessLogAppend(argAccessLogAppend);
	}

	public boolean isAccessLogExtended() {
		return this.requestLogConfiguration.isAccessLogExtended();
	}

	public void setAccessLogExtended(boolean argAccessLogExtended) {
		this.requestLogConfiguration.setAccessLogExtended(argAccessLogExtended);
	}

	public String getAccessLogTimeZone() {
		return this.requestLogConfiguration.getAccessLogTimeZone();
	}

	public void setAccessLogTimeZone(String argAccessLogTimeZone) {
		this.requestLogConfiguration.setAccessLogTimeZone(argAccessLogTimeZone);
	}

	public boolean isAccessLogServerName() {
		return this.requestLogConfiguration.isAccessLogServerName();
	}

	public void setAccessLogServerName(boolean argAccessLogServerName) {
		this.requestLogConfiguration
				.setAccessLogServerName(argAccessLogServerName);
	}

	public boolean isAccessLogCookies() {
		return this.requestLogConfiguration.isAccessLogCookies();
	}

	public void setAccessLogCookies(boolean argAccessLogCookies) {
		this.requestLogConfiguration.setAccessLogCookies(argAccessLogCookies);
	}

	public boolean isAccessLogEnableLatency() {
		return this.requestLogConfiguration.isAccessLogEnableLatency();
	}

	public void setAccessLogEnableLatency(boolean argAccessLogEnableLatency) {
		this.requestLogConfiguration
				.setAccessLogEnableLatency(argAccessLogEnableLatency);
	}

	public boolean isStatsOn() {
		return this.requestLogConfiguration.isStatsOn();
	}

	public void setStatsOn(boolean argStatsOn) {
		this.requestLogConfiguration.setStatsOn(argStatsOn);
	}

}
