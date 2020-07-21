package com.bridgelabz.bookstore.repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.CartModel;

@Repository
@Transactional
public interface CartRepository extends JpaRepository<CartModel, Long> {

	@Query(value = "select * from Cart where book_id=?", nativeQuery = true)
	Optional<CartModel> findByBookId(Long bookId);
	
	void deleteAllByBookId(Long bookId);
	
	void deleteAllByUserId(long userId);

	void deleteByUserIdAndBookId(long id, Long bookId);

	CartModel findByUserIdAndBookId(long id, Long bookId);

	boolean existsByUserIdAndBookId(long id, Long bookId);

	void deleteAllByUserIdAndBookId(long id, Long bookId);

	List<CartModel> findAllByUserIdAndBookId(long id, Long bookId);

	List<CartModel> findAllByUserId(long userId);

}
