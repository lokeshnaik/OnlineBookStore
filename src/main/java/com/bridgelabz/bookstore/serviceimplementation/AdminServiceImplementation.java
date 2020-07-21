package com.bridgelabz.bookstore.serviceimplementation;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.bookstore.model.SellerModel;
import com.bridgelabz.bookstore.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bridgelabz.bookstore.exception.UserNotFoundException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.response.Response;
import com.bridgelabz.bookstore.service.AdminService;
import com.bridgelabz.bookstore.utility.JwtGenerator;

@Service
public class AdminServiceImplementation implements AdminService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private Environment environment;

	@Override
	public List<BookModel>  getAllUnVerifiedBooks(String token) throws UserNotFoundException {

		long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if(role.equals("ADMIN")){
			return bookRepository.getAllUnverfiedBooks();
		}
		else {
			throw new UserNotFoundException("User is Not Authorized");
		}
	}

	@Override
	public Response bookVerification(Long bookId, String token) throws UserNotFoundException {
		long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if(role.equals("ADMIN")){
			Optional<BookModel> book= bookRepository.findById(bookId);
			book.get().setVerfied(true);
			bookRepository.save(book.get());
			return new Response(environment.getProperty("book.verified.successfull"),HttpStatus.OK.value(),book);
		}
		else {
			throw new UserNotFoundException("User is Not Authorized");
		}
	}
	
	@Override
	public Response bookUnVerification(Long bookId, String token) throws UserNotFoundException {
		long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if(role.equals("ADMIN")){
			Optional<BookModel> book= bookRepository.findById(bookId);
			book.get().setVerfied(false);
			book.get().setIsDisApproved(true);
			bookRepository.save(book.get());
			return new Response("Book Unverified SuccessFully",HttpStatus.OK.value(),book);
		}
		else {
			throw new UserNotFoundException("User is Not Authorized");
		}
	}
}	