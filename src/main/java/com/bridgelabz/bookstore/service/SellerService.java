package com.bridgelabz.bookstore.service;

import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.bookstore.dto.BookDto;
import com.bridgelabz.bookstore.dto.UpdateBookDto;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.SellerModel;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.response.Response;

import java.util.List;

public interface SellerService {
	Response addBook(BookDto newBook, String token) throws UserException;

	Response updateBook(UpdateBookDto newBook, String token,Long BookId) throws UserException;

	Response deleteBook(String token, Long bookId);

	Response sendRequestForApproval(Long bookId,String token);

	List<BookModel> getNewlyAddedBooks(String token);

	List<BookModel> getDisapprovedBooks(String token);

	List<BookModel> getApprovedBooks(String token);

	List<BookModel> getAllBooks(String token) throws UserException;

	List<BookModel> getUnverfiedBooks();

	public List<SellerModel> getAllSellers();
}
