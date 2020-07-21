package com.bridgelabz.bookstore.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Cart")
public class CartModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	private int quantity;
	private double totalPrice;

	private String name;
	private String author;
	private String imgUrl;
	private int maxQuantity;
	private boolean isInWishList;
	private long userId;
	private Long bookId;
	//@ManyToMany(cascade = { CascadeType.ALL, CascadeType.MERGE }, fetch = FetchType.LAZY)
	//@JoinTable(name = "Cartbook", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = {
		//	@JoinColumn(name = "bookId") })
	//private List<Long> bookId = new ArrayList<>();
}