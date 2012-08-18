package com.startupoxygen.craft.server;

public class ProcessConfiguration {
	private String userName;
	private String groupName;
	private String umask;
	private boolean startAsPrivileged;

	public ProcessConfiguration() {
		// Default Constructor
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String argUserName) {
		this.userName = argUserName;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String argGroupName) {
		this.groupName = argGroupName;
	}

	public String getUmask() {
		return this.umask;
	}

	public void setUmask(String argUmask) {
		this.umask = argUmask;
	}

	public boolean isStartAsPrivileged() {
		return this.startAsPrivileged;
	}

	public void setStartAsPrivileged(boolean argStartAsPrivileged) {
		this.startAsPrivileged = argStartAsPrivileged;
	}

}