package com.bridgelabz.bookstore.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Component
@Data
public class BookDto implements Serializable
{
	@NotBlank(message="BookName is mandatory")
	private String bookName;
	@Min(1)
	private int quantity;
	@Min(1)
	private Double price;
	@Size(min=3,max=30)
	private String authorName;
	@Size(min=3,max=30)
	private String bookDetails;
	private String bookImgUrl;
}
