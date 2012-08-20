package com.startupoxygen.craft.server;


public class CraftServerDefaults {

	public static final String DEFAULT_HTTP_HOST = "0.0.0.0";
	public static final int DEFAULT_HTTP_PORT = 80;
	public static final int DEFAULT_HTTPS_PORT = 443;
	public static final int DEFAULT_THREAD_POOL_SIZE = 100;
	public static final int DEFAULT_ACCEPTOR_THREAD_SIZE = 4;
	public static final int DEFAULT_ACCEPTOR_QUEUE_SIZE = 256;
	public static final int DEFAULT_MAX_IDLE_TIME = 300000;
	public static final String DEFAULT_KEYSTORE_FILE = "/keystore.jks";
	public static final String DEFAULT_KEYSTORE_PASSWORD = "changeit";
	public static final String DEFAULT_KEY_PASSOWRD = "changeit";
	public static final boolean DEFAULT_STATS_ON = false;
	public static final String[] DEFAULT_INCLUDED_CIPHER_SUITES = null;
	public static final String[] DEFAULT_EXCLUDED_CIPHER_SUITES = new String[] {
			"SSL_RSA_WITH_DES_CBC_SHA", "SSL_DHE_RSA_WITH_DES_CBC_SHA",
			"SSL_DHE_DSS_WITH_DES_CBC_SHA", "SSL_RSA_EXPORT_WITH_RC4_40_MD5",
			"SSL_RSA_EXPORT_WITH_DES40_CBC_SHA",
			"SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA",
			"SSL_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA" };
	public static final int DEFAULT_HTTP_REQUEST_HEADER_SIZE = 10240;
	public static final int DEFAULT_HTTPS_REQUEST_HEADER_SIZE = 10240;
	public static final int DEFAULT_HTTP_RESPONSE_HEADER_SIZE = 10240;
	public static final int DEFAULT_HTTPS_RESPONSE_HEADER_SIZE = 10240;
	public static final String DEFAULT_PROCESS_USERNAME = null;
	public static final String DEFAULT_PROCESS_GROUPNAME = null;
	public static final String DEFAULT_PROCESS_UMASK = null;
	public static final boolean DEFAULT_PROCESS_START_AS_PRIVILEGED = false;
	public static final boolean DEFAULT_ACCESS_LOG_ENABLED = true;
	public static final String DEFAULT_ACCESS_LOG_DIRECTORY = "./logs";
	public static final String DEFAULT_ACCESS_LOG_FILENAME = "access-yyyy_mm_dd.log";
	public static final int DEFAULT_ACCESS_LOG_RETAIN_DAYS = 30;
	public static final boolean DEFAULT_ACCESS_LOG_APPEND = true;
	public static final boolean DEFAULT_ACCESS_LOG_EXTENDED = false;
	public static final String DEFAULT_ACCESS_LOG_TIME_ZONE = "GMT";
	public static final boolean DEFAULT_ACCESS_LOG_SERVER_NAME = true;
	public static final boolean DEFAULT_ACCESS_LOG_COOKIES = false;
	public static final boolean DEFAULT_ACCESS_LOG_ENABLE_LATENCY = true;

}
