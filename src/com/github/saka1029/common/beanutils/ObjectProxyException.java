package com.github.saka1029.common.beanutils;

public class ObjectProxyException extends Exception {

	private static final long serialVersionUID = 1L;

	public ObjectProxyException(Throwable cause) {
		super(cause.getMessage());
	}

	public ObjectProxyException(String message) {
		super(message);
	}

}
