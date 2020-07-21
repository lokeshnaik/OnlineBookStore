package com.bridgelabz.bookstore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.bridgelabz.bookstore.model.BookModel;


@Repository
public interface BookRepository extends JpaRepository<BookModel, Long>  {
	

	@Query(value = "SELECT * from  bookstorebackendapi.book where is_verfied =0", nativeQuery = true)
	List<BookModel> getAllUnverfiedBooks();
	
	@Query(value = "select * from book where is_verfied = 1 order by price asc", nativeQuery = true)
	List<BookModel> sortBookAsc();
	
	@Query(value = "select * from book where is_verfied = 1 order by price desc", nativeQuery = true)

	List<BookModel>  sortBookDesc();
	
	@Query(value="Select * from book where  is_verfied=1",nativeQuery = true)
	List<BookModel>getAllVerifiedBooks();

	@Query(value="Select * from book where  is_verfied=1 or is_send_for_approval=1",nativeQuery = true)
	List<BookModel>getAllBooks();
	
	@Query(value="Select * from book where  book_id=?1",nativeQuery = true)
	BookModel getBookDetail(Long bookid);

	BookModel findByBookId(Long bookId);
	
	@Query(value="select * from book where book_name=?1",nativeQuery = true)
	Optional<BookModel> searchBookByName(String bookName);
	
	@Query(value="select * from book where author_name =?1",nativeQuery = true)
	Optional<BookModel> searchBookByAuthor(String authorName);

}
