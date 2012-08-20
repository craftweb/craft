/*
 * Copyright Andrei Goumilevski
 * This file licensed under GPLv3 for non commercial projects
 * GPLv3 text http://www.gnu.org/licenses/gpl-3.0.html
 * For commercial usage please contact me
 * gmlvsk2@gmail.com
 *
 */

package com.startupoxygen.craft.mongo.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

	private static final Logger logger = LoggerFactory.getLogger(Config.class);

	// mongo server
	public static String mongo_servers = "127.0.0.1:27017";
	public static boolean mongo_safeoperations = false;
	public static boolean mongo_remove_idfield = false;

	// main server
	public static int server_threadsno = 50;
	public static int httpPort = 8080;
	public static int httpsPort = 8443;
	public static String hostname = "localhost";

	// ssl
	public static boolean ssl = false;
	public static String ssl_keystore = "/keystore.jks";
	public static String ssl_passwd = "mongodb";

	// ip filter
	public static boolean ip_filter = false;
	public static String ip_white = null;
	public static String ip_black = null;

	// server auth
	public static boolean auth = false;

	// memcached server
	public static boolean memcached = false;
	public static String memcached_servers = null;
	public static int memcached_expire = 0;

	// search
	public static boolean search = false;
	public static String search_index_path = null;
	public static Map<String, ArrayList<String>> search_index_fields = null;
	public static String search_default_field = null;

	// gridfs
	public static boolean gridfs = false;

	private static final int MINTHRNO = 3;

	public static int i(String s, int i) {
		if (s == null)
			return i;
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return i;
		}
	}

	static void init() {

		Properties prop = null;
		InputStream is = null;
		try {
			prop = new Properties();
			is = new FileInputStream("config/mongoser.properties");
			prop.load(is);
		} catch (IOException e) {
			return;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception ignore) {
					// ignore
				}
			}
		}

		// mongodb
		mongo_servers = prop.getProperty("mongo.servers", "127.0.0.1:27017");
		if (mongo_servers == null)
			throw new IllegalArgumentException("mongo.servers missing");
		mongo_safeoperations = Boolean.parseBoolean(prop.getProperty(
				"mongo.safeoperations", "false"));
		mongo_remove_idfield = Boolean.parseBoolean(prop.getProperty(
				"mongo.remove.idfield", "false"));

		// rest server
		server_threadsno = i(prop.getProperty("server.threadsno"), 50);
		if (server_threadsno < MINTHRNO) {
			server_threadsno = MINTHRNO;
			logger.warn("Changed threads no to " + server_threadsno);
		}
		httpPort = i(prop.getProperty("httpPort"), 8080);
		httpsPort = i(prop.getProperty("httpsPort"), 8443);
		hostname = prop.getProperty("server.adr", "localhost");

		// ssl
		ssl = Boolean.parseBoolean(prop.getProperty("ssl", "false"));
		if (ssl) {
			ssl_keystore = prop
					.getProperty("ssl.keystore", "./config/keystore");
			ssl_passwd = prop.getProperty("ssl.passwd", "mongodb");
		}

		// ip filter
		ip_filter = Boolean
				.parseBoolean(prop.getProperty("ip.filter", "false"));
		ip_white = prop.getProperty("ip.filter.white", null);
		ip_black = prop.getProperty("ip.filter.black", null);

		// auth
		auth = Boolean.parseBoolean(prop.getProperty("auth", "false"));

		// memcached
		memcached = Boolean
				.parseBoolean(prop.getProperty("memcached", "false"));
		if (memcached) {
			memcached_servers = prop.getProperty("memcached.servers",
					"127.0.0.1:11211");
			memcached_expire = i(prop.getProperty("memcached.expire"), 0);
		}

		// search
		search = Boolean.parseBoolean(prop.getProperty("search", "false"));
		if (search) {
			search_index_fields = new HashMap<String, ArrayList<String>>();
			search_index_path = prop.getProperty("search.index.path",
					"./searchindex");
			search_default_field = prop.getProperty("search.default.field");
			if (search_default_field == null) {
				throw new IllegalArgumentException(
						"Search option is on but search.default.field not defined");
			}
			String tmp = prop.getProperty("search.index.fields");
			if (tmp == null)
				throw new IllegalArgumentException(
						"search.index.fields not defined");
			tmp.replaceAll(" ", "");
			String ar[] = tmp.split("[,]");
			for (String s : ar) {
				String ar2[] = s.split("[.]");
				if (ar2.length != 3)
					throw new IllegalArgumentException(
							"search field must be in form dbname.colname.fieldname");
				String key = ar2[0] + "." + ar2[1];
				ArrayList<String> fields_ar = search_index_fields.get(key);
				if (fields_ar == null)
					fields_ar = new ArrayList<String>();
				fields_ar.add(ar2[2]);
				search_index_fields.put(ar2[0] + "." + ar2[1], fields_ar);
			}
		}

		// gridfs
		gridfs = Boolean.parseBoolean(prop.getProperty("gridfs", "false"));

	}

}
