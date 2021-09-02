package com.unityTest.courseManagement.exception;

public class UnsupportedActionException extends RuntimeException {
	public UnsupportedActionException(String action) {
		super(String.format("Unsupported action: %s", action));
	}
}
