package com.bridgelabz.bookstore.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Entity
@Table(name = "Book")
@Data
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bookId;

	@NotBlank(message= "BookName is mandatory")
	private String bookName;

	private int quantity;

	@Min(1)
	private Double price;

	@Size(min=2,max=30)
	private String authorName;

	@CreationTimestamp
	private LocalDateTime createdDateAndTime;

	@UpdateTimestamp
	private LocalDateTime UpdatedDateAndTime;

	@Column
	@NotNull
	private String bookDetails;

	@Column(nullable = false)
	private boolean isVerfied;

	private Boolean isDisApproved;
    private Boolean isSendForApproval;
	private String imageUrl;
	private Long sellerId;

//	
//	@ManyToMany(cascade = CascadeType.ALL)
//	//@JoinColumn(name = "user_id")
//	private List<UserModel> users;

//	@ManyToMany(cascade = CascadeType.ALL)
//	//@JoinColumn(name = "seller_id")
//	private List<SellerModel> sellers;
    @Column
    private String seller;

	@Column
	private String bookImgUrl;

}