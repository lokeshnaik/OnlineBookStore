package com.bridgelabz.bookstore.exception;

public class EmailSendingException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	private final int status;

	/**
	 * Constructor takes message and Status code as input parameter and fetch
	 * message from its superclass.
	 * 
	 * @param message as String input parameter
	 * @param status  as Integer input parameter
	 */
	public EmailSendingException(String message, int status) {
		super(message);
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
