package com.startupoxygen.craft.server;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
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
	private List<WebApp> webApps;
	private Server server;

	public CraftServer() {
		// Default Constructor
	}

	private void init() {
		Server newServer = new Server(this.getConfiguration().getHttpPort());
		if (this.validateProcessConfiguration()) {
			SetUIDServer setUidServer = new SetUIDServer();
			if (this.getConfiguration().getUserName() != null
					&& !this.getConfiguration().getUserName().isEmpty()) {
				setUidServer.setUsername(this.getConfiguration().getUserName());
			}
			if (this.getConfiguration().getGroupName() != null
					&& !this.getConfiguration().getGroupName().isEmpty()) {
				setUidServer.setGroupname(this.getConfiguration()
						.getGroupName());
			}
			if (this.getConfiguration().getUmask() != null
					&& !this.getConfiguration().getUmask().isEmpty()) {
				setUidServer.setUmaskOctal(this.getConfiguration().getUmask());
			}
			setUidServer.setStartServerAsPrivileged(this.getConfiguration()
					.isStartAsPrivileged());
			newServer = setUidServer;
		}

		QueuedThreadPool qtp = new QueuedThreadPool();
		qtp.setMinThreads(this.getConfiguration().getThreadPoolSize());
		qtp.setMaxThreads(this.getConfiguration().getThreadPoolSize());
		newServer.setThreadPool(qtp);

		SelectChannelConnector connector = new SelectChannelConnector();
		if (this.getConfiguration().getHttpHost() != null
				&& !this.getConfiguration().getHttpHost().isEmpty()) {
			connector.setHost(this.getConfiguration().getHttpHost());
		}
		connector.setAcceptors(this.getConfiguration().getAcceptorThreadSize());
		connector.setAcceptQueueSize(this.getConfiguration()
				.getAcceptorQueueSize());
		connector.setPort(this.getConfiguration().getHttpPort());
		connector.setMaxIdleTime(this.getConfiguration().getMaxIdleTime());
		connector.setConfidentialPort(this.getConfiguration().getHttpsPort());
		connector.setStatsOn(this.getConfiguration().isStatsOn());
		connector.setName("httpConnector");
		connector.setRequestHeaderSize(this.getConfiguration()
				.getHttpRequestHeaderSize());
		connector.setResponseHeaderSize(this.getConfiguration()
				.getHttpResponseHeaderSize());

		newServer.setConnectors(new Connector[] { connector });

		if (this.getConfiguration().getKeystoreFile() != null
				&& !this.getConfiguration().getKeystoreFile().isEmpty()
				&& this.getConfiguration().getKeystorePassword() != null
				&& !this.getConfiguration().getKeystorePassword().isEmpty()) {
			SslContextFactory sslContextFactory = new SslContextFactory();
			sslContextFactory.setKeyStorePath(this.getClass()
					.getResource(this.getConfiguration().getKeystoreFile())
					.toExternalForm());
			sslContextFactory.setKeyStorePassword(this.getConfiguration()
					.getKeystorePassword());
			if (this.getConfiguration().getExcluded() != null) {
				if (this.getConfiguration().getExcluded().length > 0) {
					sslContextFactory.setExcludeCipherSuites(this
							.getConfiguration().getExcluded());
				}
			}
			SslSocketConnector sslConnector = new SslSocketConnector(
					sslContextFactory);
			if (this.getConfiguration().getHttpHost() != null
					&& !this.getConfiguration().getHttpHost().isEmpty()) {
				sslConnector.setHost(this.getConfiguration().getHttpHost());
			}
			sslConnector.setPort(this.getConfiguration().getHttpsPort());
			sslConnector.setAcceptors(this.getConfiguration()
					.getAcceptorThreadSize());
			sslConnector.setAcceptQueueSize(this.getConfiguration()
					.getAcceptorQueueSize());
			sslConnector.setRequestHeaderSize(this.getConfiguration()
					.getHttpsRequestHeaderSize());
			sslConnector.setResponseHeaderSize(this.getConfiguration()
					.getHttpsResponseHeaderSize());
			if (this.getConfiguration().getIncluded() != null) {
				if (this.getConfiguration().getIncluded().length > 0) {
					sslContextFactory.setIncludeCipherSuites(this
							.getConfiguration().getIncluded());
				}
			}
			sslConnector.setName("httpsConnector");

			newServer.addConnector(sslConnector);
		}

		ContextHandlerCollection contextHandlerCollection = new ContextHandlerCollection();
		List<Handler> handlers = new ArrayList<Handler>();
		for (WebApp webApp : this.webApps) {
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
		if (this.getConfiguration().isAccessLogEnabled()) {
			requestLogHandler = new RequestLogHandler();
			NCSARequestLog requestLog = new NCSARequestLog(this
					.getConfiguration().getAccessLogDirectory()
					+ "/"
					+ this.getConfiguration().getAccessLogFilename());
			requestLog.setRetainDays(this.getConfiguration()
					.getAccessLogRetainDays());
			requestLog.setAppend(this.getConfiguration().isAccessLogAppend());
			requestLog.setExtended(this.getConfiguration()
					.isAccessLogExtended());
			requestLog.setLogTimeZone(this.getConfiguration()
					.getAccessLogTimeZone());
			requestLog.setLogServer(this.getConfiguration()
					.isAccessLogServerName());
			requestLog.setLogCookies(this.getConfiguration()
					.isAccessLogCookies());
			requestLog.setLogLatency(this.getConfiguration()
					.isAccessLogEnableLatency());
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
		return (this.getConfiguration().getUserName() != null && !this
				.getConfiguration().getUserName().isEmpty())
				|| (this.getConfiguration().getGroupName() != null && !this
						.getConfiguration().getGroupName().isEmpty())
				|| (this.getConfiguration().getUmask() != null && !this
						.getConfiguration().getUmask().isEmpty());
	}

	public void start() throws Exception {
		this.server.start();
		this.server.join();
	}

	public void stop() throws Exception {
		this.server.stop();
	}

	public CraftServerConfiguration getConfiguration() {
		return this.configuration;
	}

	public void setConfiguration(CraftServerConfiguration argConfiguration) {
		this.configuration = argConfiguration;
	}

}
