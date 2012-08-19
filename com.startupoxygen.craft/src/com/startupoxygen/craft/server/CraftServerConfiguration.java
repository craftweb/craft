package com.startupoxygen.craft.server;

import java.util.List;

import org.eclipse.jetty.server.handler.AbstractHandler;

import static com.startupoxygen.craft.server.CraftServerDefaults.*;

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

	public CraftServerConfiguration(boolean argUseDefaults) {
		super();
		this.hostConfiguration = new HostConfiguration();
		this.httpConfiguration = new HttpConfiguration();
		this.keystoreConfiguration = new KeystoreConfiguration();
		this.cipherSuitesConfiguration = new CipherSuitesConfiguration();
		this.processConfiguration = new ProcessConfiguration();
		this.threadPoolConfiguration = new ThreadPoolConfiguration();
		this.webAppConfiguration = new WebAppConfiguration();
		this.requestLogConfiguration = new RequestLogConfiguration();
		if (argUseDefaults) {
			this.setHttpHost(DEFAULT_HTTP_HOST)
					.setHttpPort(DEFAULT_HTTP_PORT)
					.setHttpsPort(DEFAULT_HTTPS_PORT)
					.setHttpRequestHeaderSize(DEFAULT_HTTP_REQUEST_HEADER_SIZE)
					.setHttpsRequestHeaderSize(
							DEFAULT_HTTPS_REQUEST_HEADER_SIZE)
					.setHttpResponseHeaderSize(
							DEFAULT_HTTP_RESPONSE_HEADER_SIZE)
					.setHttpsResponseHeaderSize(
							DEFAULT_HTTPS_RESPONSE_HEADER_SIZE)
					.setKeystoreFile(DEFAULT_KEYSTORE_FILE)
					.setKeystorePassword(DEFAULT_KEYSTORE_PASSWORD)
					.setKeyPassowrd(DEFAULT_KEY_PASSOWRD)
					.setIncluded(DEFAULT_INCLUDED_CIPHER_SUITES)
					.setExcluded(DEFAULT_EXCLUDED_CIPHER_SUITES)
					.setStatsOn(DEFAULT_STATS_ON)
					.setThreadPoolSize(DEFAULT_THREAD_POOL_SIZE)
					.setAcceptorThreadSize(DEFAULT_ACCEPTOR_THREAD_SIZE)
					.setAcceptorQueueSize(DEFAULT_ACCEPTOR_QUEUE_SIZE)
					.setMaxIdleTime(DEFAULT_MAX_IDLE_TIME)
					.setAccessLogEnabled(DEFAULT_ACCESS_LOG_ENABLED)
					.setAccessLogDirectory(DEFAULT_ACCESS_LOG_DIRECTORY)
					.setAccessLogFilename(DEFAULT_ACCESS_LOG_FILENAME)
					.setAccessLogRetainDays(DEFAULT_ACCESS_LOG_RETAIN_DAYS)
					.setAccessLogAppend(DEFAULT_ACCESS_LOG_APPEND)
					.setAccessLogTimeZone(DEFAULT_ACCESS_LOG_TIME_ZONE)
					.setAccessLogServerName(DEFAULT_ACCESS_LOG_SERVER_NAME)
					.setAccessLogCookies(DEFAULT_ACCESS_LOG_COOKIES)
					.setAccessLogExtended(DEFAULT_ACCESS_LOG_EXTENDED)
					.setAccessLogEnableLatency(
							DEFAULT_ACCESS_LOG_ENABLE_LATENCY);
		}
	}

	public HostConfiguration getHostConfiguration() {
		return this.hostConfiguration;
	}

	public CraftServerConfiguration setHostConfiguration(
			HostConfiguration argHostConfiguration) {
		this.hostConfiguration = argHostConfiguration;
		return this;
	}

	public HttpConfiguration getHttpConfiguration() {
		return this.httpConfiguration;
	}

	public CraftServerConfiguration setHttpConfiguration(
			HttpConfiguration argHttpConfiguration) {
		this.httpConfiguration = argHttpConfiguration;
		return this;
	}

	public KeystoreConfiguration getKeystoreConfiguration() {
		return this.keystoreConfiguration;
	}

	public CraftServerConfiguration setKeystoreConfiguration(
			KeystoreConfiguration argKeystoreConfiguration) {
		this.keystoreConfiguration = argKeystoreConfiguration;
		return this;
	}

	public CipherSuitesConfiguration getCipherSuitesConfiguration() {
		return this.cipherSuitesConfiguration;
	}

	public CraftServerConfiguration setCipherSuitesConfiguration(
			CipherSuitesConfiguration argCipherSuitesConfiguration) {
		this.cipherSuitesConfiguration = argCipherSuitesConfiguration;
		return this;
	}

	public ProcessConfiguration getProcessConfiguration() {
		return this.processConfiguration;
	}

	public CraftServerConfiguration setProcessConfiguration(
			ProcessConfiguration argProcessConfiguration) {
		this.processConfiguration = argProcessConfiguration;
		return this;
	}

	public ThreadPoolConfiguration getThreadPoolConfiguration() {
		return this.threadPoolConfiguration;
	}

	public CraftServerConfiguration setThreadPoolConfiguration(
			ThreadPoolConfiguration argThreadPoolConfiguration) {
		this.threadPoolConfiguration = argThreadPoolConfiguration;
		return this;
	}

	public WebAppConfiguration getWebAppConfiguration() {
		return this.webAppConfiguration;
	}

	public CraftServerConfiguration setWebAppConfiguration(
			WebAppConfiguration argWebAppConfiguration) {
		this.webAppConfiguration = argWebAppConfiguration;
		return this;
	}

	public RequestLogConfiguration getRequestLogConfiguration() {
		return this.requestLogConfiguration;
	}

	public CraftServerConfiguration setRequestLogConfiguration(
			RequestLogConfiguration argLogginConfiguration) {
		this.requestLogConfiguration = argLogginConfiguration;
		return this;
	}

	public String getHttpHost() {
		return this.hostConfiguration.getHttpHost();
	}

	public CraftServerConfiguration setHttpHost(String argHttpHost) {
		this.hostConfiguration.setHttpHost(argHttpHost);
		return this;
	}

	public int getHttpPort() {
		return this.hostConfiguration.getHttpPort();
	}

	public CraftServerConfiguration setHttpPort(int argHttpPort) {
		this.hostConfiguration.setHttpPort(argHttpPort);
		return this;
	}

	public int getHttpsPort() {
		return this.hostConfiguration.getHttpsPort();
	}

	public CraftServerConfiguration setHttpsPort(int argHttpsPort) {
		this.hostConfiguration.setHttpsPort(argHttpsPort);
		return this;
	}

	public int getHttpRequestHeaderSize() {
		return this.httpConfiguration.getHttpRequestHeaderSize();
	}

	public CraftServerConfiguration setHttpRequestHeaderSize(
			int argHttpRequestHeaderSize) {
		this.httpConfiguration
				.setHttpRequestHeaderSize(argHttpRequestHeaderSize);
		return this;
	}

	public int getHttpsRequestHeaderSize() {
		return this.httpConfiguration.getHttpsRequestHeaderSize();
	}

	public CraftServerConfiguration setHttpsRequestHeaderSize(
			int argHttpsRequestHeaderSize) {
		this.httpConfiguration
				.setHttpsRequestHeaderSize(argHttpsRequestHeaderSize);
		return this;
	}

	public int getHttpResponseHeaderSize() {
		return this.httpConfiguration.getHttpResponseHeaderSize();
	}

	public CraftServerConfiguration setHttpResponseHeaderSize(
			int argHttpResponseHeaderSize) {
		this.httpConfiguration
				.setHttpResponseHeaderSize(argHttpResponseHeaderSize);
		return this;
	}

	public int getHttpsResponseHeaderSize() {
		return this.httpConfiguration.getHttpsResponseHeaderSize();
	}

	public CraftServerConfiguration setHttpsResponseHeaderSize(
			int argHttpsResponseHeaderSize) {
		this.httpConfiguration
				.setHttpsResponseHeaderSize(argHttpsResponseHeaderSize);
		return this;
	}

	public String getKeystoreFile() {
		return this.keystoreConfiguration.getKeystoreFile();
	}

	public CraftServerConfiguration setKeystoreFile(String argKeystoreFile) {
		this.keystoreConfiguration.setKeystoreFile(argKeystoreFile);
		return this;
	}

	public String getKeystorePassword() {
		return this.keystoreConfiguration.getKeystorePassword();
	}

	public CraftServerConfiguration setKeystorePassword(
			String argKeystorePassword) {
		this.keystoreConfiguration.setKeystorePassword(argKeystorePassword);
		return this;
	}

	public String getKeyPassowrd() {
		return this.keystoreConfiguration.getKeyPassowrd();
	}

	public CraftServerConfiguration setKeyPassowrd(String argKeyPassowrd) {
		this.keystoreConfiguration.setKeyPassowrd(argKeyPassowrd);
		return this;
	}

	public String[] getIncluded() {
		return this.cipherSuitesConfiguration.getIncluded();
	}

	public CraftServerConfiguration setIncluded(String[] argIncluded) {
		this.cipherSuitesConfiguration.setIncluded(argIncluded);
		return this;
	}

	public String[] getExcluded() {
		return this.cipherSuitesConfiguration.getExcluded();
	}

	public CraftServerConfiguration setExcluded(String[] argExcluded) {
		this.cipherSuitesConfiguration.setExcluded(argExcluded);
		return this;
	}

	public String getUserName() {
		return this.processConfiguration.getUserName();
	}

	public CraftServerConfiguration setUserName(String argUserName) {
		this.processConfiguration.setUserName(argUserName);
		return this;
	}

	public String getGroupName() {
		return this.processConfiguration.getGroupName();
	}

	public CraftServerConfiguration setGroupName(String argGroupName) {
		this.processConfiguration.setGroupName(argGroupName);
		return this;
	}

	public String getUmask() {
		return this.processConfiguration.getUmask();
	}

	public CraftServerConfiguration setUmask(String argUmask) {
		this.processConfiguration.setUmask(argUmask);
		return this;
	}

	public boolean isStartAsPrivileged() {
		return this.processConfiguration.isStartAsPrivileged();
	}

	public CraftServerConfiguration setStartAsPrivileged(
			boolean argStartAsPrivileged) {
		this.processConfiguration.setStartAsPrivileged(argStartAsPrivileged);
		return this;
	}

	public int getThreadPoolSize() {
		return this.threadPoolConfiguration.getThreadPoolSize();
	}

	public CraftServerConfiguration setThreadPoolSize(int argThreadPoolSize) {
		this.threadPoolConfiguration.setThreadPoolSize(argThreadPoolSize);
		return this;
	}

	public int getAcceptorThreadSize() {
		return this.threadPoolConfiguration.getAcceptorThreadSize();
	}

	public CraftServerConfiguration setAcceptorThreadSize(
			int argAcceptorThreadSize) {
		this.threadPoolConfiguration
				.setAcceptorThreadSize(argAcceptorThreadSize);
		return this;
	}

	public int getAcceptorQueueSize() {
		return this.threadPoolConfiguration.getAcceptorQueueSize();
	}

	public CraftServerConfiguration setAcceptorQueueSize(
			int argAcceptorQueueSize) {
		this.threadPoolConfiguration.setAcceptorQueueSize(argAcceptorQueueSize);
		return this;
	}

	public int getMaxIdleTime() {
		return this.threadPoolConfiguration.getMaxIdleTime();
	}

	public CraftServerConfiguration setMaxIdleTime(int argMaxIdleTime) {
		this.threadPoolConfiguration.setMaxIdleTime(argMaxIdleTime);
		return this;
	}

	public boolean isAccessLogEnabled() {
		return this.requestLogConfiguration.isAccessLogEnabled();
	}

	public CraftServerConfiguration setAccessLogEnabled(
			boolean argAccessLogEnabled) {
		this.requestLogConfiguration.setAccessLogEnabled(argAccessLogEnabled);
		return this;
	}

	public String getAccessLogDirectory() {
		return this.requestLogConfiguration.getAccessLogDirectory();
	}

	public CraftServerConfiguration setAccessLogDirectory(
			String argAccessLogDirectory) {
		this.requestLogConfiguration
				.setAccessLogDirectory(argAccessLogDirectory);
		return this;
	}

	public String getAccessLogFilename() {
		return this.requestLogConfiguration.getAccessLogFilename();
	}

	public CraftServerConfiguration setAccessLogFilename(
			String argAccessLogFilename) {
		this.requestLogConfiguration.setAccessLogFilename(argAccessLogFilename);
		return this;
	}

	public int getAccessLogRetainDays() {
		return this.requestLogConfiguration.getAccessLogRetainDays();
	}

	public CraftServerConfiguration setAccessLogRetainDays(
			int argAccessLogRetainDays) {
		this.requestLogConfiguration
				.setAccessLogRetainDays(argAccessLogRetainDays);
		return this;
	}

	public boolean isAccessLogAppend() {
		return this.requestLogConfiguration.isAccessLogAppend();
	}

	public CraftServerConfiguration setAccessLogAppend(
			boolean argAccessLogAppend) {
		this.requestLogConfiguration.setAccessLogAppend(argAccessLogAppend);
		return this;
	}

	public boolean isAccessLogExtended() {
		return this.requestLogConfiguration.isAccessLogExtended();
	}

	public CraftServerConfiguration setAccessLogExtended(
			boolean argAccessLogExtended) {
		this.requestLogConfiguration.setAccessLogExtended(argAccessLogExtended);
		return this;
	}

	public String getAccessLogTimeZone() {
		return this.requestLogConfiguration.getAccessLogTimeZone();
	}

	public CraftServerConfiguration setAccessLogTimeZone(
			String argAccessLogTimeZone) {
		this.requestLogConfiguration.setAccessLogTimeZone(argAccessLogTimeZone);
		return this;
	}

	public boolean isAccessLogServerName() {
		return this.requestLogConfiguration.isAccessLogServerName();
	}

	public CraftServerConfiguration setAccessLogServerName(
			boolean argAccessLogServerName) {
		this.requestLogConfiguration
				.setAccessLogServerName(argAccessLogServerName);
		return this;
	}

	public boolean isAccessLogCookies() {
		return this.requestLogConfiguration.isAccessLogCookies();
	}

	public CraftServerConfiguration setAccessLogCookies(
			boolean argAccessLogCookies) {
		this.requestLogConfiguration.setAccessLogCookies(argAccessLogCookies);
		return this;
	}

	public boolean isAccessLogEnableLatency() {
		return this.requestLogConfiguration.isAccessLogEnableLatency();
	}

	public CraftServerConfiguration setAccessLogEnableLatency(
			boolean argAccessLogEnableLatency) {
		this.requestLogConfiguration
				.setAccessLogEnableLatency(argAccessLogEnableLatency);
		return this;
	}

	public boolean isStatsOn() {
		return this.requestLogConfiguration.isStatsOn();
	}

	public CraftServerConfiguration setStatsOn(boolean argStatsOn) {
		this.requestLogConfiguration.setStatsOn(argStatsOn);
		return this;
	}

	public CraftServerConfiguration setWebApps(List<WebApp> argWebApps) {
		webAppConfiguration.setWebApps(argWebApps);
		return this;
	}

	public CraftServerConfiguration addCustomContextHandler(
			AbstractHandler argCustomContextHandler, String argContextPath,
			String... argHostName) {
		webAppConfiguration.addCustomContextHandler(argCustomContextHandler,
				argContextPath, argHostName);
		return this;
	}

	public CraftServerConfiguration addWebApp(String argContextPath,
			String argAppDir, String... argHostName) {
		webAppConfiguration.addWebApp(argContextPath, argAppDir, argHostName);
		return this;
	}

	public CraftServer build() {
		if (this.getWebAppConfiguration().getWebApps() != null && !this.getWebAppConfiguration().getWebApps().isEmpty()) {
			CraftServer craftServer = new CraftServer();
			craftServer.setConfiguration(this);
			craftServer.init();
			return craftServer;
		} else {
			throw new RuntimeException("At least once WebApp needs to be configured.");
		}
	}

}
