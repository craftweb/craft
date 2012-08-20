package com.startupoxygen.craft;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.startupoxygen.craft.server.CraftServer;
import com.startupoxygen.craft.server.CraftServerConfiguration;

public class DefaultCraftServerLauncher {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultCraftServerLauncher.class);

	public static void main(String[] args) {
		
		if (logger.isInfoEnabled()) {
			logger.info("Starting CraftServer");
			logger.info("Initializing CraftProjectCache");
		}
		try {
			CraftProjectCache.getInstance();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Failed to initialize CraftProjectCache", e);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("CraftProjectCache initialized successfully");
		}
		CraftServer server = new CraftServerConfiguration(true).addWebApp("/", "./webapp", "localhost").build();
		try {
			server.start();
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Failed to start CraftServer", e);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("CraftServer started on port 80 and 443.");
		}

	}

}
