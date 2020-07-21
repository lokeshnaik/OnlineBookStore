package com.bridgelabz.bookstore.repository;

import com.bridgelabz.bookstore.model.BookModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bridgelabz.bookstore.model.SellerModel;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends JpaRepository<SellerModel,Long> {
    @Query(value = "SELECT * from  bookstorebackendapi.sellerbooks where seller_id =?", nativeQuery = true)
    List<BookModel> getSellerBooks(long seller_id);

    @Query(value = "SELECT * from  bookstorebackendapi.Seller where email_id =?", nativeQuery = true)
    Optional<SellerModel> getSellerByEmailId(String email_id);

    @Query(value = "SELECT seller_id from  bookstorebackendapi.Seller where user_id =?", nativeQuery = true)
    long getSellerId(long user_id);

    @Query(value = "SELECT * from  bookstorebackendapi.Seller where user_id =?", nativeQuery = true)
    Optional<SellerModel> getSeller(long user_id);
	
}
