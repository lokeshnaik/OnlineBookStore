package com.bridgelabz.bookstore.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserException extends Exception {
	private static final long serialVersionUID = 1L;
	private String message;
	HttpStatus status;
	LocalDateTime time;

	public UserException(String message,HttpStatus status) {
		this.message = message;
		this.status=status;
	}

	public UserException(String message) {
		this.message = message;
		
	}
	
}
