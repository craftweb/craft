package com.startupoxygen.craft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.server.session.AbstractSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.mortbay.setuid.SetUIDServer;

public class CraftServer {
	private CraftServerConfiguration configuration;
	private List<CraftServer.WebApp> webApps;
	private Server server;

	public CraftServer() {
		// Default Constructor
	}

	private void init() {
		Server newServer = new Server(this.getHttpPort());
		if (this.validateProcessConfiguration()) {
			SetUIDServer setUidServer = new SetUIDServer();
			if (this.getUserName() != null && !this.getUserName().isEmpty()) {
				setUidServer.setUsername(this.getUserName());
			}
			if (this.getGroupName() != null && !this.getGroupName().isEmpty()) {
				setUidServer.setGroupname(this.getGroupName());
			}
			if (this.getUmask() != null && !this.getUmask().isEmpty()) {
				setUidServer.setUmaskOctal(this.getUmask());
			}
			setUidServer.setStartServerAsPrivileged(this.isStartAsPrivileged());
			newServer = setUidServer;
		}
		
		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setMinThreads(threadPoolSize);
		threadPool.setMaxThreads(threadPoolSize);
		newServer.setThreadPool(threadPool);

		SelectChannelConnector connector = new SelectChannelConnector();
		if (this.httpHost != null && !this.httpHost.isEmpty()) {
			connector.setHost(this.httpHost);
		}
		connector.setAcceptors(this.acceptorThreadSize);
		connector.setAcceptQueueSize(this.acceptorQueueSize);
		connector.setPort(this.httpPort);
		connector.setMaxIdleTime(this.maxIdleTime);
		connector.setConfidentialPort(this.httpsPort);
		connector.setStatsOn(statsOn);
		connector.setName("httpConnector");
		connector.setRequestHeaderSize(this.httpRequestHeaderSize);
		connector.setResponseHeaderSize(this.httpResponseHeaderSize);

		newServer.setConnectors(new Connector[] { connector });

		if (this.keystoreFile != null && !this.keystoreFile.isEmpty()
				&& this.keystorePassword != null
				&& !this.keystorePassword.isEmpty()) {
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(this.getClass()
					.getResource(this.keystoreFile).toExternalForm());
			sslContextFactory.setKeyStorePassword(this.keystorePassword);
			if (this.excludedCipherSuites != null) {
				if (this.excludedCipherSuites.length > 0) {
					sslContextFactory
							.setExcludeCipherSuites(this.excludedCipherSuites);
				}
			}
			SslSocketConnector sslConnector = new SslSocketConnector(
					sslContextFactory);
			if (this.httpHost != null && !this.httpHost.isEmpty()) {
				sslConnector.setHost(this.httpHost);
			}
			sslConnector.setPort(this.httpsPort);
			sslConnector.setAcceptors(this.acceptorThreadSize);
			sslConnector.setAcceptQueueSize(this.acceptorQueueSize);
			sslConnector.setRequestHeaderSize(this.httpsRequestHeaderSize);
			sslConnector.setResponseHeaderSize(this.httpsResponseHeaderSize);
			if (this.includedCipherSuites != null) {
				if (this.includedCipherSuites.length > 0) {
					sslContextFactory
							.setIncludeCipherSuites(this.includedCipherSuites);
				}
			}
			sslConnector.setName("httpsConnector");

			newServer.addConnector(sslConnector);
		}

		ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
		List<Handler> handlers = new ArrayList<Handler>();
		for (CraftServer.WebApp webApp : this.webApps) {
			if (webApp.customContextHandler) {
				ContextHandler contextHandler = new ContextHandler();
				contextHandler.setContextPath(webApp.contextPath);
				contextHandler.setHandler(webApp.jettyHandler);
				contextHandler.setVirtualHosts(webApp.hostName);
				handlers.add(contextHandler);
			} else {
				WebAppContext webAppContext = new WebAppContext();
				webAppContext.setResourceBase(webApp.appDir);
				webAppContext.setDescriptor(webApp.appDir + "/WEB-INF/web.xml");
				webAppContext.setContextPath(webApp.contextPath);
				webAppContext.setParentLoaderPriority(true);
				// webAppContext.setWar(webApp.appDir);
				webAppContext.setVirtualHosts(webApp.hostName);
				webAppContext.setConnectorNames(new String[] { "httpConnector",
						"httpsConnector" });
				SessionHandler sessionHandler = webAppContext
						.getSessionHandler();
				SessionManager sessionManager = sessionHandler
						.getSessionManager();
				if (sessionManager instanceof AbstractSessionManager) {
					AbstractSessionManager abstractSessionManager = (AbstractSessionManager) sessionManager;
					abstractSessionManager.setUsingCookies(true);
					abstractSessionManager.setHttpOnly(true);
				}
				handlers.add(webAppContext);
			}
		}

		Handler[] handlerArray = new Handler[handlers.size()];
		handlers.toArray(handlerArray);
		contextHandlerCollection.setHandlers(handlerArray);

		RequestLogHandler requestLogHandler = null;
		if (this.accessLogEnabled) {
			requestLogHandler = new RequestLogHandler();
			NCSARequestLog requestLog = new NCSARequestLog(
					this.accessLogDirectory + "/" + this.accessLogFilename);
			requestLog.setRetainDays(this.accessLogRetainDays);
			requestLog.setAppend(this.accessLogAppend);
			requestLog.setExtended(this.accessLogExtended);
			requestLog.setLogTimeZone(this.accessLogTimeZone);
			requestLog.setLogServer(this.accessLogServerName);
			requestLog.setLogCookies(this.accessLogCookies);
			requestLog.setLogLatency(this.accessLogEnableLatency);
			requestLogHandler.setRequestLog(requestLog);
		}
		if (requestLogHandler != null) {
			HandlerCollection handlerCollection = new HandlerCollection();
			handlerCollection.setHandlers(new Handler[] {
					contextHandlerCollection, requestLogHandler });
			newServer.setHandler(handlerCollection);
		} else {
			newServer.setHandler(contextHandlerCollection);
		}

		if (newServer != null) {
			this.server = newServer;
		}

	}

	private boolean validateProcessConfiguration() {
		return (this.getUserName() != null && !this.getUserName().isEmpty())
				|| (this.getGroupName() != null && !this.getGroupName()
						.isEmpty())
				|| (this.getUmask() != null && !this.getUmask().isEmpty());
	}

	public void start() throws Exception {
		this.server.start();
		this.server.join();
	}

	public void stop() throws Exception {
		this.server.stop();
	}

	private static class WebApp {

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

	public static class Builder {

		private String httpHost = "0.0.0.0";
		private int httpPort = 80;
		private int httpsPort = 443;
		private int threadPoolSize = 100;
		private int acceptorThreadSize = 4;
		private int acceptorQueueSize = 256;
		private int maxIdleTime = 300000;
		private String keystoreFile = null;
		private String keystorePassword = null;
		private String keyPassowrd = null;
		private boolean statsOn = false;
		private List<String> includedCipherSuites;
		private List<String> excludedCipherSuites;
		private int httpRequestHeaderSize = 10240;
		private int httpsRequestHeaderSize = 10240;
		private int httpResponseHeaderSize = 10240;
		private int httpsResponseHeaderSize = 10240;
		private String processUserName = null;
		private String processGroupName = null;
		private String processUmask = null;
		private boolean processStartAsPrivileged = false;
		private boolean accessLogEnabled = false;
		private String accessLogDirectory = null;
		private String accessLogFilename = null;
		private int accessLogRetainDays = 0;
		private boolean accessLogAppend = false;
		private boolean accessLogExtended = false;
		private String accessLogTimeZone = null;
		private boolean accessLogServerName = false;
		private boolean accessLogCookies = false;
		private boolean accessLogEnableLatency = false;
		private List<CraftServer.WebApp> webApps = null;

		public Builder() {
			// Default Construstor.
		}

		public Builder setHttpHost(String argHttpHost) {
			this.httpHost = argHttpHost;
			return this;
		}

		public Builder setHttpPort(int argHttpPort) {
			this.httpPort = argHttpPort;
			return this;
		}

		public Builder setHttpsPort(int argHttpsPort) {
			this.httpsPort = argHttpsPort;
			return this;
		}

		public Builder setThreadPoolSize(int argThreadPoolSize) {
			this.threadPoolSize = argThreadPoolSize;
			return this;
		}

		public Builder setAcceptorThreadSize(int argAcceptorThreadSize) {
			this.acceptorThreadSize = argAcceptorThreadSize;
			return this;
		}

		public Builder setAcceptorQueueSize(int argAcceptorQueueSize) {
			this.acceptorQueueSize = argAcceptorQueueSize;
			return this;
		}

		public Builder setMaxIdleTime(int argMaxIdleTime) {
			this.maxIdleTime = argMaxIdleTime;
			return this;
		}

		public Builder setKeystoreFile(String argKeystoreFile) {
			this.keystoreFile = argKeystoreFile;
			return this;
		}

		public Builder setKeystorePassword(String argKeystorePassword) {
			this.keystorePassword = argKeystorePassword;
			return this;
		}

		public Builder setKeyPassword(String argKeyPassword) {
			this.keyPassowrd = argKeyPassword;
			return this;
		}

		public Builder setStatsOn(boolean argStatsOn) {
			this.statsOn = argStatsOn;
			return this;
		}

		public Builder setIncludedCipherSuites(String[] argIncludedCipherSuites) {
			if (this.includedCipherSuites == null) {
				this.includedCipherSuites = new ArrayList<String>(
						argIncludedCipherSuites.length);
			}
			Collections.addAll(this.includedCipherSuites,
					argIncludedCipherSuites);
			return this;
		}

		public Builder setIncludedCipherSuites(
				List<String> argIncludedCipherSuites) {
			if (this.includedCipherSuites != null) {
				this.includedCipherSuites.addAll(argIncludedCipherSuites);
			} else {
				this.includedCipherSuites = argIncludedCipherSuites;
			}
			return this;
		}

		public Builder addIncludedCipherSuite(String argIncludedCipherSuite) {
			if (this.includedCipherSuites == null) {
				this.includedCipherSuites = new ArrayList<String>();
			}
			this.includedCipherSuites.add(argIncludedCipherSuite);
			return this;
		}

		public Builder setExcludedCipherSuites(String[] argExcludedCipherSuites) {
			if (this.excludedCipherSuites == null) {
				this.excludedCipherSuites = new ArrayList<String>(
						argExcludedCipherSuites.length);
			}
			Collections.addAll(this.excludedCipherSuites,
					argExcludedCipherSuites);
			return this;
		}

		public Builder setExcludedCipherSuites(
				List<String> argExcludedCipherSuites) {
			if (this.excludedCipherSuites != null) {
				this.excludedCipherSuites.addAll(argExcludedCipherSuites);
			} else {
				this.excludedCipherSuites = argExcludedCipherSuites;
			}
			return this;
		}

		public Builder addExcludedCipherSuite(String argExcludedCipherSuite) {
			if (this.excludedCipherSuites == null) {
				this.excludedCipherSuites = new ArrayList<String>();
			}
			this.excludedCipherSuites.add(argExcludedCipherSuite);
			return this;
		}

		public Builder setHttpRequestHeaderSize(int argHttpRequestHeaderSize) {
			this.httpRequestHeaderSize = argHttpRequestHeaderSize;
			return this;
		}

		public Builder setHttpsRequestHeaderSize(int argHttpsRequestHeaderSize) {
			this.httpsRequestHeaderSize = argHttpsRequestHeaderSize;
			return this;
		}

		public Builder setHttpResponseHeaderSize(int argHttpResponseHeaderSize) {
			this.httpResponseHeaderSize = argHttpResponseHeaderSize;
			return this;
		}

		public Builder setHttpsResponseHeaderSize(int argHttpsResponseHeaderSize) {
			this.httpsResponseHeaderSize = argHttpsResponseHeaderSize;
			return this;
		}

		public Builder setProcessUserName(String argProcessUserName) {
			this.processUserName = argProcessUserName;
			return this;
		}

		public Builder setProcessGroupName(String argProcessGroupName) {
			this.processGroupName = argProcessGroupName;
			return this;
		}

		public Builder setProcessUmask(String argProcessUmask) {
			this.processUmask = argProcessUmask;
			return this;
		}

		public Builder setProcessStartAsPrivileged(
				boolean argProcessStartAsPrivileged) {
			this.processStartAsPrivileged = argProcessStartAsPrivileged;
			return this;
		}

		public Builder setAccessLogEnabled(boolean argAccessLogEnabled) {
			this.accessLogEnabled = argAccessLogEnabled;
			return this;
		}

		public Builder setAccessLogDirectory(String argAccessLogDirectory) {
			this.accessLogDirectory = argAccessLogDirectory;
			return this;
		}

		public Builder setAccessLogFilename(String argAccessLogFilename) {
			this.accessLogFilename = argAccessLogFilename;
			return this;
		}

		public Builder setAccessLogRetainDays(int argAccessLogRetainDays) {
			this.accessLogRetainDays = argAccessLogRetainDays;
			return this;
		}

		public Builder setAccessLogAppend(boolean argAccessLogAppend) {
			this.accessLogAppend = argAccessLogAppend;
			return this;
		}

		public Builder setAccessLogExtended(boolean argAccessLogExtended) {
			this.accessLogEnabled = argAccessLogExtended;
			return this;
		}

		public Builder setAccessLogTimeZone(String argAccessLogTimeZone) {
			this.accessLogTimeZone = argAccessLogTimeZone;
			return this;
		}

		public Builder setAccessLogServer(boolean argAccessLogServerName) {
			this.accessLogServerName = argAccessLogServerName;
			return this;
		}

		public Builder setAccessLogCookies(boolean argAccessLogCookies) {
			this.accessLogCookies = argAccessLogCookies;
			return this;
		}

		public Builder setAccessLogEnableLatency(
				boolean argAccessLogEnableLatency) {
			this.accessLogEnableLatency = argAccessLogEnableLatency;
			return this;
		}

		public Builder addCustomContextHandler(
				AbstractHandler argCustomContextHandler, String argContextPath,
				String... argHostName) {
			if (this.webApps == null) {
				this.webApps = new ArrayList<CraftServer.WebApp>();
			}
			this.webApps.add(new CraftServer.WebApp(true,
					argCustomContextHandler, argContextPath, argHostName));
			return this;
		}

		public Builder addWebApp(String argContextPath, String argAppDir,
				String... argHostName) {
			if (this.webApps == null) {
				this.webApps = new ArrayList<CraftServer.WebApp>();
			}
			this.webApps.add(new CraftServer.WebApp(false, argContextPath,
					argAppDir, argHostName));
			return this;
		}

		public CraftServer build() {
			String[] includedCipherSuitesArray = null;
			if (this.includedCipherSuites != null
					&& !this.includedCipherSuites.isEmpty()) {
				includedCipherSuitesArray = new String[this.includedCipherSuites
						.size()];
				this.includedCipherSuites.toArray(includedCipherSuitesArray);
			} else {
				// includedCipherSuitesArray = new String[] { "" };
			}
			String[] excludedCipherSuitesArray = null;
			if (this.excludedCipherSuites != null
					&& !this.excludedCipherSuites.isEmpty()) {
				excludedCipherSuitesArray = new String[this.excludedCipherSuites
						.size()];
				this.excludedCipherSuites.toArray(excludedCipherSuitesArray);
			} else {
				excludedCipherSuitesArray = new String[] {
						"SSL_RSA_WITH_DES_CBC_SHA",
						"SSL_DHE_RSA_WITH_DES_CBC_SHA",
						"SSL_DHE_DSS_WITH_DES_CBC_SHA",
						"SSL_RSA_EXPORT_WITH_RC4_40_MD5",
						"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
						"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
						"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA" };
			}
			return new CraftServer(this.httpHost, this.httpPort,
					this.httpsPort, this.threadPoolSize,
					this.acceptorThreadSize, this.acceptorQueueSize,
					this.maxIdleTime, this.keystoreFile, this.keystorePassword,
					this.keyPassowrd, this.statsOn, includedCipherSuitesArray,
					excludedCipherSuitesArray, this.httpRequestHeaderSize,
					this.httpsRequestHeaderSize, this.httpResponseHeaderSize,
					this.httpsResponseHeaderSize, this.processUserName,
					this.processGroupName, this.processUmask,
					this.processStartAsPrivileged, this.accessLogEnabled,
					this.accessLogDirectory, this.accessLogFilename,
					this.accessLogRetainDays, this.accessLogAppend,
					this.accessLogExtended, this.accessLogTimeZone,
					this.accessLogServerName, this.accessLogCookies,
					this.accessLogEnableLatency, this.webApps);
		}

	}

	public PortConfiguration getPortConfiguration() {
		return this.configuration.getPortConfiguration();
	}

	public void setPortConfiguration(PortConfiguration argPortConfiguration) {
		this.configuration.setPortConfiguration(argPortConfiguration);
	}

	public HttpConfiguration getHttpConfiguration() {
		return this.configuration.getHttpConfiguration();
	}

	public void setHttpConfiguration(HttpConfiguration argHttpConfiguration) {
		this.configuration.setHttpConfiguration(argHttpConfiguration);
	}

	public KeystoreConfiguration getKeystoreConfiguration() {
		return this.configuration.getKeystoreConfiguration();
	}

	public void setKeystoreConfiguration(
			KeystoreConfiguration argKeystoreConfiguration) {
		this.configuration.setKeystoreConfiguration(argKeystoreConfiguration);
	}

	public CipherSuitesConfiguration getCipherSuitesConfiguration() {
		return this.configuration.getCipherSuitesConfiguration();
	}

	public void setCipherSuitesConfiguration(
			CipherSuitesConfiguration argCipherSuitesConfiguration) {
		this.configuration
				.setCipherSuitesConfiguration(argCipherSuitesConfiguration);
	}

	public ProcessConfiguration getProcessConfiguration() {
		return this.configuration.getProcessConfiguration();
	}

	public void setProcessConfiguration(
			ProcessConfiguration argProcessConfiguration) {
		this.configuration.setProcessConfiguration(argProcessConfiguration);
	}

	public ThreadPoolConfiguration getThreadPoolConfiguration() {
		return this.configuration.getThreadPoolConfiguration();
	}

	public void setThreadPoolConfiguration(
			ThreadPoolConfiguration argThreadPoolConfiguration) {
		this.configuration
				.setThreadPoolConfiguration(argThreadPoolConfiguration);
	}

	public WebAppConfiguration getWebAppConfiguration() {
		return this.configuration.getWebAppConfiguration();
	}

	public void setWebAppConfiguration(
			WebAppConfiguration argWebAppConfiguration) {
		this.configuration.setWebAppConfiguration(argWebAppConfiguration);
	}

	public LoggingConfiguration getLoggingConfiguration() {
		return this.configuration.getLoggingConfiguration();
	}

	public void setLoggingConfiguration(
			LoggingConfiguration argLogginConfiguration) {
		this.configuration.setLoggingConfiguration(argLogginConfiguration);
	}

	public String getHttpHost() {
		return this.configuration.getHttpHost();
	}

	public void setHttpHost(String argHttpHost) {
		this.configuration.setHttpHost(argHttpHost);
	}

	public int getHttpPort() {
		return this.configuration.getHttpPort();
	}

	public void setHttpPort(int argHttpPort) {
		this.configuration.setHttpPort(argHttpPort);
	}

	public int getHttpsPort() {
		return this.configuration.getHttpsPort();
	}

	public void setHttpsPort(int argHttpsPort) {
		this.configuration.setHttpsPort(argHttpsPort);
	}

	public int getHttpRequestHeaderSize() {
		return this.configuration.getHttpRequestHeaderSize();
	}

	public void setHttpRequestHeaderSize(int argHttpRequestHeaderSize) {
		this.configuration.setHttpRequestHeaderSize(argHttpRequestHeaderSize);
	}

	public int getHttpsRequestHeaderSize() {
		return this.configuration.getHttpsRequestHeaderSize();
	}

	public void setHttpsRequestHeaderSize(int argHttpsRequestHeaderSize) {
		this.configuration.setHttpsRequestHeaderSize(argHttpsRequestHeaderSize);
	}

	public int getHttpResponseHeaderSize() {
		return this.configuration.getHttpResponseHeaderSize();
	}

	public void setHttpResponseHeaderSize(int argHttpResponseHeaderSize) {
		this.configuration.setHttpResponseHeaderSize(argHttpResponseHeaderSize);
	}

	public int getHttpsResponseHeaderSize() {
		return this.configuration.getHttpsResponseHeaderSize();
	}

	public void setHttpsResponseHeaderSize(int argHttpsResponseHeaderSize) {
		this.configuration
				.setHttpsResponseHeaderSize(argHttpsResponseHeaderSize);
	}

	public String getKeystoreFile() {
		return this.configuration.getKeystoreFile();
	}

	public void setKeystoreFile(String argKeystoreFile) {
		this.configuration.setKeystoreFile(argKeystoreFile);
	}

	public String getKeystorePassword() {
		return this.configuration.getKeystorePassword();
	}

	public void setKeystorePassword(String argKeystorePassword) {
		this.configuration.setKeystorePassword(argKeystorePassword);
	}

	public String getKeyPassowrd() {
		return this.configuration.getKeyPassowrd();
	}

	public void setKeyPassowrd(String argKeyPassowrd) {
		this.configuration.setKeyPassowrd(argKeyPassowrd);
	}

	public String[] getIncluded() {
		return this.configuration.getIncluded();
	}

	public void setIncluded(String[] argIncluded) {
		this.configuration.setIncluded(argIncluded);
	}

	public String[] getExcluded() {
		return this.configuration.getExcluded();
	}

	public void setExcluded(String[] argExcluded) {
		this.configuration.setExcluded(argExcluded);
	}

	public String getUserName() {
		return this.configuration.getUserName();
	}

	public void setUserName(String argUserName) {
		this.configuration.setUserName(argUserName);
	}

	public String getGroupName() {
		return this.configuration.getGroupName();
	}

	public void setGroupName(String argGroupName) {
		this.configuration.setGroupName(argGroupName);
	}

	public String getUmask() {
		return this.configuration.getUmask();
	}

	public void setUmask(String argUmask) {
		this.configuration.setUmask(argUmask);
	}

	public boolean isStartAsPrivileged() {
		return this.configuration.isStartAsPrivileged();
	}

	public void setStartAsPrivileged(boolean argStartAsPrivileged) {
		this.configuration.setStartAsPrivileged(argStartAsPrivileged);
	}

	public int getThreadPoolSize() {
		return this.configuration.getThreadPoolSize();
	}

	public void setThreadPoolSize(int argThreadPoolSize) {
		this.configuration.setThreadPoolSize(argThreadPoolSize);
	}

	public int getAcceptorThreadSize() {
		return this.configuration.getAcceptorThreadSize();
	}

	public void setAcceptorThreadSize(int argAcceptorThreadSize) {
		this.configuration.setAcceptorThreadSize(argAcceptorThreadSize);
	}

	public int getAcceptorQueueSize() {
		return this.configuration.getAcceptorQueueSize();
	}

	public void setAcceptorQueueSize(int argAcceptorQueueSize) {
		this.configuration.setAcceptorQueueSize(argAcceptorQueueSize);
	}

	public int getMaxIdleTime() {
		return this.configuration.getMaxIdleTime();
	}

	public void setMaxIdleTime(int argMaxIdleTime) {
		this.configuration.setMaxIdleTime(argMaxIdleTime);
	}

	public boolean isAccessLogEnabled() {
		return this.configuration.isAccessLogEnabled();
	}

	public void setAccessLogEnabled(boolean argAccessLogEnabled) {
		this.configuration.setAccessLogEnabled(argAccessLogEnabled);
	}

	public String getAccessLogDirectory() {
		return this.configuration.getAccessLogDirectory();
	}

	public void setAccessLogDirectory(String argAccessLogDirectory) {
		this.configuration.setAccessLogDirectory(argAccessLogDirectory);
	}

	public String getAccessLogFilename() {
		return this.configuration.getAccessLogFilename();
	}

	public void setAccessLogFilename(String argAccessLogFilename) {
		this.configuration.setAccessLogFilename(argAccessLogFilename);
	}

	public int getAccessLogRetainDays() {
		return this.configuration.getAccessLogRetainDays();
	}

	public void setAccessLogRetainDays(int argAccessLogRetainDays) {
		this.configuration.setAccessLogRetainDays(argAccessLogRetainDays);
	}

	public boolean isAccessLogAppend() {
		return this.configuration.isAccessLogAppend();
	}

	public void setAccessLogAppend(boolean argAccessLogAppend) {
		this.configuration.setAccessLogAppend(argAccessLogAppend);
	}

	public boolean isAccessLogExtended() {
		return this.configuration.isAccessLogExtended();
	}

	public void setAccessLogExtended(boolean argAccessLogExtended) {
		this.configuration.setAccessLogExtended(argAccessLogExtended);
	}

	public String getAccessLogTimeZone() {
		return this.configuration.getAccessLogTimeZone();
	}

	public void setAccessLogTimeZone(String argAccessLogTimeZone) {
		this.configuration.setAccessLogTimeZone(argAccessLogTimeZone);
	}

	public boolean isAccessLogServerName() {
		return this.configuration.isAccessLogServerName();
	}

	public void setAccessLogServerName(boolean argAccessLogServerName) {
		this.configuration.setAccessLogServerName(argAccessLogServerName);
	}

	public boolean isAccessLogCookies() {
		return this.configuration.isAccessLogCookies();
	}

	public void setAccessLogCookies(boolean argAccessLogCookies) {
		this.configuration.setAccessLogCookies(argAccessLogCookies);
	}

	public boolean isAccessLogEnableLatency() {
		return this.configuration.isAccessLogEnableLatency();
	}

	public void setAccessLogEnableLatency(boolean argAccessLogEnableLatency) {
		this.configuration.setAccessLogEnableLatency(argAccessLogEnableLatency);
	}

	public boolean isStatsOn() {
		return this.configuration.isStatsOn();
	}

	public void setStatsOn(boolean argStatsOn) {
		this.configuration.setStatsOn(argStatsOn);
	}

}
