package com.bridgelabz.bookstore.exception;

public class UserVerificationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private int status;

	public UserVerificationException(String message) {
		super(message);	
	}

	public UserVerificationException(int status, String message) {
		super(message);	
		this.status= status;
	}

	public int getStatus() {
		return status;
	}
}
