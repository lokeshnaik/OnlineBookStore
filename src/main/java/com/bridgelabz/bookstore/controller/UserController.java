package com.bridgelabz.bookstore.controller;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.bridgelabz.bookstore.dto.ForgotPasswordDto;
import com.bridgelabz.bookstore.dto.LoginDto;
import com.bridgelabz.bookstore.dto.RegistrationDto;
import com.bridgelabz.bookstore.dto.ResetPasswordDto;
import com.bridgelabz.bookstore.dto.UserDetailsDTO;
import com.bridgelabz.bookstore.exception.BookException;
import com.bridgelabz.bookstore.exception.UserException;
import com.bridgelabz.bookstore.exception.UserNotFoundException;
import com.bridgelabz.bookstore.model.BookModel;
import com.bridgelabz.bookstore.model.CartModel;
import com.bridgelabz.bookstore.response.Response;
import com.bridgelabz.bookstore.response.UserAddressDetailsResponse;
import com.bridgelabz.bookstore.response.UserDetailsResponse;
import com.bridgelabz.bookstore.service.AmazonS3ClientService;
import com.bridgelabz.bookstore.service.ElasticSearchService;
import com.bridgelabz.bookstore.service.UserService;
import com.bridgelabz.bookstore.serviceimplementation.AmazonS3ClientServiceImpl;
import com.bridgelabz.bookstore.utility.JwtGenerator;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
@CrossOrigin(allowedHeaders = "*", origins = "*")
@PropertySource(name = "user", value = { "classpath:response.properties" })
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private Environment environment;

	@Autowired
	private ElasticSearchService elasticSearchService;

	@Autowired
	private AmazonS3ClientServiceImpl amazonS3ClientService;
//// ***************User Authentication******************\\\\\\\

	@ApiOperation(value = "To Register")
	@PostMapping("/register")
	public ResponseEntity<Response> register(@RequestBody @Valid RegistrationDto registrationDto, BindingResult result)
			throws UserException {

		if (result.hasErrors())
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response(result.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
							"Invalid Credentials"));

		if (userService.register(registrationDto))
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response(HttpStatus.OK.value(), environment.getProperty("user.register.successful")));

		return ResponseEntity.status(HttpStatus.OK).body(new Response(HttpStatus.BAD_REQUEST.value(),
				environment.getProperty("user.register.emailalreadyinuse")));
	}

	@ApiOperation(value = "To Verfiy EmailId")
	@GetMapping("/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) {

		if (userService.verify(token))
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response(HttpStatus.OK.value(), environment.getProperty("user.verified.successful")));

		return ResponseEntity.status(HttpStatus.OK).body(
				new Response(HttpStatus.BAD_REQUEST.value(), environment.getProperty("user.verified.unsuccessfull")));
	}

	@ApiOperation(value = "Sending Mail to Reset Password")
	@PostMapping("/forgotpassword")
	public ResponseEntity<UserDetailsResponse> forgotPassword(@RequestBody @Valid ForgotPasswordDto emailId) {

		UserDetailsResponse response = userService.forgetPassword(emailId);
		return new ResponseEntity<UserDetailsResponse>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "To Reset Password")
	@PutMapping("/resetpassword")
	public ResponseEntity<Response> resetPassword(@RequestBody @Valid ResetPasswordDto resetPassword,
			@RequestParam("token") String token) throws UserNotFoundException {

		if (userService.resetPassword(resetPassword, token))
			return ResponseEntity.status(HttpStatus.OK).body(
					new Response(HttpStatus.OK.value(), environment.getProperty("user.resetpassword.successfull")));

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				new Response(HttpStatus.BAD_REQUEST.value(), environment.getProperty("user.resetpassword.failed")));
	}

	@ApiOperation(value = "To login")
	@PostMapping("/login")
	public ResponseEntity<Response> login(@RequestBody LoginDto loginDTO) throws UserNotFoundException, UserException {
		Response response = userService.login(loginDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response("login successfull", HttpStatus.OK.value(), response));

		// return new ResponseEntity<Response>(response, HttpStatus.OK);//G
	}

	/////// **********Cart Operations*************\\\\\\\\\\\\

	@ApiOperation(value = "Add Books to Cart")
	@PostMapping("/AddToCart")
	public ResponseEntity<Response> AddToCart(@RequestParam Long bookId) throws BookException {
		Response response = userService.addToCart(bookId);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response(environment.getProperty("book.added.to.cart.successfully"), HttpStatus.OK.value(), response));
		// return new ResponseEntity<Response>(response, HttpStatus.OK);//G
	}

	@ApiOperation(value = "Adding More Items To Cart")
	@PostMapping("/addMoreItems")
	public ResponseEntity<Response> addMoreItems(@RequestParam Long bookId) throws BookException {
		Response response = userService.addMoreItems(bookId);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}


	@ApiOperation(value = "Adding More Items To Cart by user input")
	@PostMapping("/addItems/{bookId}/{quantity}")
	public ResponseEntity<Response> addItems(@PathVariable Long bookId, @PathVariable int quantity)
			throws BookException {
		Response response = userService.addItems(bookId, quantity);
		return ResponseEntity.status(HttpStatus.OK).body(new Response(
				environment.getProperty("book.added.tocart.byquantity"), HttpStatus.OK.value(), response));
		// return new ResponseEntity<Response>(response, HttpStatus.OK);//G
	}

	@ApiOperation(value = "Remove a book from Cart")
	@DeleteMapping("/removeFromCart")
	public ResponseEntity<Response> removeFromCart(@RequestParam Long bookId) throws BookException {
		Response response = userService.removeItem(bookId);

		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response(environment.getProperty("book.removed.fromcart"), HttpStatus.OK.value(), response));
		// return new ResponseEntity<Response>(response, HttpStatus.OK);//G
	}

	@ApiOperation(value = "Remove book from Cart ")
	@DeleteMapping("/removeAllFromCart/{bookId}")
	public ResponseEntity<Response> removeAllFromCart(@PathVariable Long bookId) throws BookException {
		Response cart = userService.removeByBookId(bookId);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Book is removed successfully", 200, cart));
	  //	return ResponseEntity.status(HttpStatus.OK)
		//		.body(new Response(environment.getProperty("items.removed.success"), HttpStatus.OK.value(), cart));


	}

	@ApiOperation(value = "Remove All Items from Cart")
	@DeleteMapping("/removeAll")
	public ResponseEntity<Response> removeAll() {
		Response response = userService.removeAll();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new Response(environment.getProperty("quantity.removed.success"), HttpStatus.OK.value(), response));
		// return new ResponseEntity<Response>(response, HttpStatus.OK);//G
	}

	@ApiOperation(value = "Get All Items from Cart")
	@GetMapping("/getAllFromCart")
	public List<CartModel> getAllItemsFromCart() throws BookException {
		return userService.getAllItemFromCart();
	}




	
	/////////////// ********User Place Order Operations******\\\\\\\\

	@ApiOperation(value = "Get User details to place order")
	@GetMapping("/getUserDetails")
	public ResponseEntity<UserAddressDetailsResponse> getUserDetails(@RequestParam long id) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetails(id));
	}

	@ApiOperation(value = "Add User details to place order")
	@PostMapping("/addUserDetails")
	public ResponseEntity<Response> addUserDetails(@RequestBody UserDetailsDTO userDetailsDTO,
			@RequestParam String token) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(userService.addUserDetails(userDetailsDTO, JwtGenerator.decodeJWT(token)));
	}

	@ApiOperation(value = "Delete User details Before Placing Order")
	@DeleteMapping("/deleteUserDetails")
	public ResponseEntity<Response> deleteUserDetails(@RequestBody UserDetailsDTO userDetailsDTO,
			@RequestParam long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUserDetails(userDetailsDTO, userId));
	}

	@ApiOperation(value = "To get all Verified and Unverified Books")
	@GetMapping("/getallBooks")
	public ResponseEntity<Response> getAllBooks() throws UserException {
		List<BookModel> book = userService.getAllBooks();
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Getting all the books", 200, book));
	}




	////// ******* Book Operations****\\\\\\\

	@ApiOperation(value = "To get all Verified Books")
	@GetMapping("/getAllVerifiedBooks")
	public ResponseEntity<Response> getAllVerifiedBooks() throws UserException {
		List<BookModel> book = userService.getAllVerifiedBooks();
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(new Response("Getting all the books which are verified", 200, book));
	}

	@ApiOperation(value = "To Get Details of a Particular Book")
	@GetMapping("/getbookdetails/{bookId}")
	public ResponseEntity<Response> getBookDetails(@PathVariable Long bookId) throws UserException {
		BookModel book = userService.getBookDetails(bookId);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Getting book details", 200, book));
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<Response> uploadFile(@RequestParam("file") MultipartFile file) {
		String url = amazonS3ClientService.uploadFile(file);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Uploaded successfully", 200, url));
	}

	@DeleteMapping("/deleteFile")
	public String deleteFile(@RequestPart(value = "url") String fileUrl) {
		return amazonS3ClientService.deleteFileFromS3Bucket(fileUrl);
	}

	@GetMapping("/searchByBookName/{bookName}")
	public ResponseEntity<Response> searchBookByName(@PathVariable String bookName) {
		Optional<BookModel> book = userService.searchBookByName(bookName);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Got the Book you wanted", 200, book));
	}

	@GetMapping("/searchByBookAuthor/{authorname}")
	public ResponseEntity<Response> searchBookByAuthor(@PathVariable String authorname) {
		Optional<BookModel> book = userService.searchBookByAuthor(authorname);
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("Got the Book you wanted", 200, book));
	}

	/////// ******* Searching Using Elastic Search*****\\\\\\\

	@ApiOperation(value = "Add Book to Elastic Search")
	@GetMapping("/search")
	public List<BookModel> search(@RequestParam String searchItem) {
		return elasticSearchService.searchByTitle(searchItem);
	}

	/////// ******** Sorting Operations****\\\\\\\

	@ApiOperation(value = "Sort Books By Price in Ascending order")
	@GetMapping("/getBooksByPriceAsc")
	public ResponseEntity<Response> sortBookByPriceAsc() {
		List<BookModel> sortBookByPriceAsc = userService.sortBookByAsc();
		if (!sortBookByPriceAsc.isEmpty())
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response(environment.getProperty("user.bookDisplayed.lowToHigh"), HttpStatus.OK.value(),
							sortBookByPriceAsc));
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response(HttpStatus.NOT_FOUND.value(), environment.getProperty("user.bookDisplayed.failed")));
	}

	@ApiOperation(value = "Sort Books By Price in descending order")
	@GetMapping("/getBooksByPriceDesc")
	public ResponseEntity<Response> sortBookByPriceDesc() {
		List<BookModel> sortBookByPriceDesc = userService.sortBookByDesc();
		if (!sortBookByPriceDesc.isEmpty())
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response(environment.getProperty("user.bookDisplayed.highToLow"), HttpStatus.OK.value(),
							sortBookByPriceDesc));
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(new Response(HttpStatus.NOT_FOUND.value(), environment.getProperty("user.bookDisplayed.failed")));
	}

	/* OrderIDGeneratorMethod */
	@GetMapping("/orderId")
	public long getOrderId() {
		return userService.getOrderId();
	}

	////////// WhisList Operations****\\\\\\\\\\\

	@ApiOperation(value = "Add Book to Whislist")
	@PostMapping("/addToWishlist")
	public Response addToWishList(@RequestParam Long bookId, @RequestParam String token) {
		return userService.addToWishList(bookId, token);
	}

	@ApiOperation(value = "Delete Book From Whislist")
	@DeleteMapping("/deleteFromWishlist")
	public Response deleteFromWishlist(@RequestParam Long bookId, @RequestParam String token) {
		return userService.deleteFromWishlist(bookId, token);
	}

	@ApiOperation(value = "Add Book from Whislist to Cart")
	@PutMapping("/addFromWishlistToCart")
	public Response addFromWishlistToCart(@RequestParam Long bookId, @RequestParam String token) {
		return userService.addFromWishlistToCart(bookId, token);
	}

	@ApiOperation(value = "Get all WishList Book")
	@GetMapping("/getWishListBooks")
	public List<CartModel> getWishListBooks(@RequestParam String token) throws BookException {
		return userService.getAllItemFromWishList(token);
	}

	@ApiOperation(value = "Getting Whislist Status")
	@GetMapping("/wishListStatus")
	public List<Long> getWishListStatus(@RequestParam String token) {
		return userService.getWishListStatus(token);
	}

	@ApiOperation(value = "Getting Cartlist Status")
	@GetMapping("/cartListStatus")
	public List<Long> getCartListStatus(@RequestParam String token) {
		return userService.getCartListStatus(token);
	}

}