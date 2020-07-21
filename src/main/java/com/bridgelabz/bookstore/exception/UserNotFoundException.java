package com.bridgelabz.bookstore.exception;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

	public enum ExceptionType{
		INVALID_EMAIL_ID, INVALID_PASSWORD;
	}
	
	public ExceptionType type;
	
	@SuppressWarnings("unused")
	private String message;
	
	public UserNotFoundException(String message, ExceptionType type) {
		this.message = message;
		this.type = type;
	}
	
	public UserNotFoundException(String message) {
		super(message);
	}
}
