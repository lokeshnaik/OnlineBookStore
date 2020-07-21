package com.bridgelabz.bookstore.exception;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bridgelabz.bookstore.response.ExceptionResponse;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(UserException.class)
	public final ResponseEntity<ExceptionResponse> userException(UserException ex) {
		
		ExceptionResponse exp = new ExceptionResponse();
		exp.setMessage(ex.getMessage());
		exp.setCode(ex.getStatus());
		exp.setTime(LocalDateTime.now());
	
		return ResponseEntity.status(exp.getCode()).body(new ExceptionResponse(exp.getMessage(), exp.getCode(),exp.getTime()));

	}
	@ExceptionHandler(BookException.class)
	public final ResponseEntity<ExceptionResponse> bookException(BookException ex) {

		ExceptionResponse exp = new ExceptionResponse();
		exp.setMessage(ex.getMessage());
		exp.setCode(ex.getStatus());
		exp.setTime(LocalDateTime.now());

		return ResponseEntity.status(exp.getCode()).body(new ExceptionResponse(exp.getMessage(), exp.getCode(),exp.getTime()));

	}
}
