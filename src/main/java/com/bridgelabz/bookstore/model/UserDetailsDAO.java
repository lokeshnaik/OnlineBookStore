package com.bridgelabz.bookstore.model;

import com.sun.jna.WString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "userDetails")
public class UserDetailsDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long sequenceNo;

    private long userId;
    private String fullName;
    private String phoneNumber;
    private long pinCode;
    private String locality;
    private String address;
    private String city;
    private String state;
    private String landMark;
    private String locationType;

    @ManyToOne
    public UserModel user;

}
