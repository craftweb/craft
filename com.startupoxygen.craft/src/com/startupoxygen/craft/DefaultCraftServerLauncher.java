package com.startupoxygen.craft;

import com.startupoxygen.craft.server.CraftServer;
import com.startupoxygen.craft.server.CraftServerConfiguration;

public class DefaultCraftServerLauncher {

	public static void main(String[] args) {
		
		CraftServer server = new CraftServerConfiguration(true).addWebApp("/", "./webapp", "localhost").build();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
