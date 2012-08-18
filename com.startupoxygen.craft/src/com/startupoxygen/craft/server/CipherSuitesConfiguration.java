package com.startupoxygen.craft.server;

public class CipherSuitesConfiguration {
	private String[] included;
	private String[] excluded;

	public CipherSuitesConfiguration() {
		// Default Configuration
	}

	public String[] getIncluded() {
		return this.included;
	}

	public void setIncluded(String[] argIncluded) {
		this.included = argIncluded;
	}

	public String[] getExcluded() {
		return this.excluded;
	}

	public void setExcluded(String[] argExcluded) {
		this.excluded = argExcluded;
	}

}