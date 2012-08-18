package com.startupoxygen.craft.server;

public class ThreadPoolConfiguration {
	private int threadPoolSize;
	private int acceptorThreadSize;
	private int acceptorQueueSize;
	private int maxIdleTime;

	public ThreadPoolConfiguration() {
		// Default Constructor
	}

	public int getThreadPoolSize() {
		return this.threadPoolSize;
	}

	public void setThreadPoolSize(int argThreadPoolSize) {
		this.threadPoolSize = argThreadPoolSize;
	}

	public int getAcceptorThreadSize() {
		return this.acceptorThreadSize;
	}

	public void setAcceptorThreadSize(int argAcceptorThreadSize) {
		this.acceptorThreadSize = argAcceptorThreadSize;
	}

	public int getAcceptorQueueSize() {
		return this.acceptorQueueSize;
	}

	public void setAcceptorQueueSize(int argAcceptorQueueSize) {
		this.acceptorQueueSize = argAcceptorQueueSize;
	}

	public int getMaxIdleTime() {
		return this.maxIdleTime;
	}

	public void setMaxIdleTime(int argMaxIdleTime) {
		this.maxIdleTime = argMaxIdleTime;
	}

}