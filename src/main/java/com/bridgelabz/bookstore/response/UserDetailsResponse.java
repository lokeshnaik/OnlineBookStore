package com.bridgelabz.bookstore.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetailsResponse {

	private String message;
	private int status;
	private String token;
	
	public UserDetailsResponse( int status, String message, String token) {
		this.status = status;
		this.message = message;
		this.token = token;
	}
	

	public UserDetailsResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}

}
