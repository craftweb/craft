package com.startupoxygen.craft;

import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.startupoxygen.craft.server.CraftServer;
import com.startupoxygen.craft.server.CraftServerConfiguration;

public class DefaultCraftServerLauncher {
	
	private static Logger logger = LoggerFactory.getLogger(DefaultCraftServerLauncher.class);

	public static void main(String[] args) {
		
		if (logger.isInfoEnabled()) {
			logger.info("Starting CraftServer");
		}
		Properties craftProps = new Properties();
		FileInputStream fis = null;
		try {
			String craftPropsFile = "./conf/craft.conf";
			fis = new FileInputStream(craftPropsFile);
			craftProps.load(fis);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("Error while initializing CraftServer Config.", e);
			}
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception ignore) {
					// ignore
				}
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Successfully loaded CraftServer Config");
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
