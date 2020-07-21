package com.bridgelabz.bookstore.service;

import java.util.List;

import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.UserDetailsDAO;

public interface ElasticSearchService {

	String addBook(BookModel bookModel);
	
	public List<BookModel> searchByTitle(String title);

	String updateBook(BookModel bookModel);

	Long deleteNote(Long bookId);
	
}
