package com.unityTest.courseManagement.exception;

public class UnsupportedVoteActionException extends RuntimeException {
	public UnsupportedVoteActionException(String action) {
		super(String.format("Unsupported vote action %s", action));
	}
}
