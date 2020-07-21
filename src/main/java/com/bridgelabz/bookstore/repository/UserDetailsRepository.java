package com.bridgelabz.bookstore.repository;

import com.bridgelabz.bookstore.model.UserDetailsDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsDAO,Long> {
    UserDetailsDAO findByAddressAndUserId(String address,long id);

}
