package com.bridgelabz.bookstore.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
@Data
public class BookException extends Exception {
    private String message;
    HttpStatus status;
    LocalDateTime time;

    public BookException(String message,HttpStatus status) {
        this.message = message;
        this.status=status;
    }
}
