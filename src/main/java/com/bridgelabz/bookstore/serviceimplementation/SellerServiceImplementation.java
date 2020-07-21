package com.bridgelabz.bookstore.serviceimplementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bridgelabz.bookstore.model.SellerModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.bookstore.dto.BookDto;
import com.bridgelabz.bookstore.dto.UpdateBookDto;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.UserModel;
import com.bridgelabz.bookstore.repository.BookRepository;
import com.bridgelabz.bookstore.repository.SellerRepository;
import com.bridgelabz.bookstore.repository.UserRepository;
import com.bridgelabz.bookstore.response.Response;
import com.bridgelabz.bookstore.service.ElasticSearchService;
import com.bridgelabz.bookstore.service.SellerService;
import com.bridgelabz.bookstore.utility.JwtGenerator;

@Service
public class SellerServiceImplementation implements SellerService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private SellerRepository sellerRepository;

	@Autowired
	private Environment environment;
	
	@Autowired
	JwtGenerator jwtop;

	@Override

	public Response addBook(BookDto newBook, String token) throws UserException {

		Long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		Optional<UserModel> user = userRepository.findById(id);
		if (role.equals("SELLER")) {
			BookModel book = new BookModel();
			BeanUtils.copyProperties(newBook, book);
			book.setBookImgUrl(newBook.getBookImgUrl());
			book.setSellerId(id);
			book.setIsDisApproved(false);
			book.setIsSendForApproval(false);
			SellerModel seller = sellerRepository.getSellerByEmailId(user.get().getEmailId()).get();
			BookModel books = bookRepository.save(book);
			seller.getBook().add(books);
			sellerRepository.save(seller);
			//elasticSearchService.addBook(book);
			return new Response(environment.getProperty("book.verification.status"), HttpStatus.OK.value(), book);

		} else {
			throw new UserException(environment.getProperty("book.unauthorised.status"));
		}

	}

	@Override
	public Response updateBook(UpdateBookDto newBook, String token, Long bookId) throws UserException {
		long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if (role.equals("SELLER")) {
			Optional<BookModel> book = bookRepository.findById(bookId);
			BeanUtils.copyProperties(newBook, book.get());
			if(book.get().isVerfied() && (newBook.getQuantity()!=book.get().getQuantity()) &&
			   newBook.getBookName().equals(book.get().getBookName()) && newBook.getAuthorName().equals(book.get().getAuthorName()) &&
			   newBook.getPrice()==book.get().getPrice() && newBook.getBookDetails().equals(book.get().getBookDetails())){
				book.get().setIsSendForApproval(true);
			}else {
                book.get().setIsSendForApproval(false);
                book.get().setIsDisApproved(false);
                book.get().setVerfied(false);
            }
			book.get().setUpdatedDateAndTime(LocalDateTime.now());
			bookRepository.save(book.get());
			SellerModel seller = new SellerModel();
			seller.getBook().add(book.get());
			//	elasticSearchService.updateBook(book.get());
			return new Response(HttpStatus.OK.value(), "Book update Successfully Need to Verify");

		}
		return new Response(HttpStatus.OK.value(), "Book Not updated Becoz Not Authoriized to add Book");
	}

	@Override
	public Response deleteBook(String token, Long bookId) {
		long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if (role.equals("SELLER") || role.equals("ADMIN")) {
			bookRepository.deleteById(bookId);
			//elasticSearchService.deleteNote(bookId);
			return new Response(HttpStatus.OK.value(), "Book deleted Successfully ");

		}
		return new Response(HttpStatus.OK.value(), "Book Not deleted Becoz Not Authoriized to delete Book");
	}
	@Override
	public Response sendRequestForApproval(Long bookId,String token)
	{   long id = JwtGenerator.decodeJWT(token);
		String role = userRepository.checkRole(id);
		if (role.equals("SELLER")) {
			Optional<BookModel> book = bookRepository.findById(bookId);
			book.get().setIsSendForApproval(true);
			bookRepository.save(book.get());
			return new Response(HttpStatus.OK.value(), "Book Approval request is send Successfully ");
		}
		return new Response(HttpStatus.OK.value(), "Unauthorized User");
	}
	@Override
	public List<BookModel> getNewlyAddedBooks(String token)
	{   long id = JwtGenerator.decodeJWT(token);
		SellerModel seller =  sellerRepository.getSeller(id).get();
		List<BookModel> bookList =  seller.getBook();
		List<BookModel> newlyAddedBooksList = new ArrayList<>();
		for(BookModel books: bookList){
			if(books.getIsSendForApproval()==false){
				newlyAddedBooksList.add(books);
			}
		}
		return newlyAddedBooksList;
	}
	@Override
	public List<BookModel> getDisapprovedBooks(String token)
	{   long id = JwtGenerator.decodeJWT(token);
		SellerModel seller =  sellerRepository.getSeller(id).get();
		List<BookModel> bookList =  seller.getBook();
		List<BookModel> disapprovedBooksList = new ArrayList<>();
		for(BookModel books: bookList){
			if(books.getIsDisApproved()==true && books.getIsSendForApproval()==true){
				disapprovedBooksList.add(books);
			}
		}
		return disapprovedBooksList;
	}
	@Override
	public List<BookModel> getApprovedBooks(String token)
	{   long id = JwtGenerator.decodeJWT(token);
		SellerModel seller =  sellerRepository.getSeller(id).get();
		List<BookModel> bookList =  seller.getBook();
		List<BookModel> approvedBooksList = new ArrayList<>();
		for(BookModel books: bookList){
			if(books.isVerfied()==true){
				approvedBooksList.add(books);
			}
		}
		return approvedBooksList;
	}

	@Override
	public List<BookModel> getAllBooks(String token) throws UserException
	{   long id = JwtGenerator.decodeJWT(token);
		SellerModel seller =  sellerRepository.getSeller(id).get();
		return seller.getBook();
	}

	@Override
	public List<BookModel> getUnverfiedBooks()
	{
		List<BookModel> book=bookRepository.getAllUnverfiedBooks();
		return book;
	}

	@Override
	public List<SellerModel> getAllSellers() {
		List<SellerModel> sellers = sellerRepository.findAll();
		return sellers;
	}
}
