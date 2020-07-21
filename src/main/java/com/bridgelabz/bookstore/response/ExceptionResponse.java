package com.bridgelabz.bookstore.response;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {

	String message;
	HttpStatus code;
	LocalDateTime time;
	
	
	public ExceptionResponse() {
		
	}
}
