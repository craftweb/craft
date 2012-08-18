package com.startupoxygen.craft.server;

public class HttpConfiguration {
	private int httpRequestHeaderSize;
	private int httpsRequestHeaderSize;
	private int httpResponseHeaderSize;
	private int httpsResponseHeaderSize;

	public HttpConfiguration() {
		// Default Constructor
	}

	public int getHttpRequestHeaderSize() {
		return this.httpRequestHeaderSize;
	}

	public void setHttpRequestHeaderSize(int argHttpRequestHeaderSize) {
		this.httpRequestHeaderSize = argHttpRequestHeaderSize;
	}

	public int getHttpsRequestHeaderSize() {
		return this.httpsRequestHeaderSize;
	}

	public void setHttpsRequestHeaderSize(int argHttpsRequestHeaderSize) {
		this.httpsRequestHeaderSize = argHttpsRequestHeaderSize;
	}

	public int getHttpResponseHeaderSize() {
		return this.httpResponseHeaderSize;
	}

	public void setHttpResponseHeaderSize(int argHttpResponseHeaderSize) {
		this.httpResponseHeaderSize = argHttpResponseHeaderSize;
	}

	public int getHttpsResponseHeaderSize() {
		return this.httpsResponseHeaderSize;
	}

	public void setHttpsResponseHeaderSize(int argHttpsResponseHeaderSize) {
		this.httpsResponseHeaderSize = argHttpsResponseHeaderSize;
	}

}