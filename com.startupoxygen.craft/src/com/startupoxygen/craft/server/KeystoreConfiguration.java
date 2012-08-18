package com.startupoxygen.craft.server;

public class KeystoreConfiguration {
	private String keystoreFile;
	private String keystorePassword;
	private String keyPassowrd;

	public KeystoreConfiguration() {
		// Default Constructor
	}

	public String getKeystoreFile() {
		return this.keystoreFile;
	}

	public void setKeystoreFile(String argKeystoreFile) {
		this.keystoreFile = argKeystoreFile;
	}

	public String getKeystorePassword() {
		return this.keystorePassword;
	}

	public void setKeystorePassword(String argKeystorePassword) {
		this.keystorePassword = argKeystorePassword;
	}

	public String getKeyPassowrd() {
		return this.keyPassowrd;
	}

	public void setKeyPassowrd(String argKeyPassowrd) {
		this.keyPassowrd = argKeyPassowrd;
	}

}
