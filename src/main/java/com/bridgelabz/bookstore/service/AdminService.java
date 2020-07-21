package com.bridgelabz.bookstore.service;

import java.util.List;

import com.bridgelabz.bookstore.exception.UserNotFoundException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.response.Response;

public interface AdminService {

	List<BookModel> getAllUnVerifiedBooks(String token) throws UserNotFoundException;


    Response bookVerification(Long bookId, String token)throws UserNotFoundException;

    Response bookUnVerification(Long bookId, String token)throws UserNotFoundException;
}
