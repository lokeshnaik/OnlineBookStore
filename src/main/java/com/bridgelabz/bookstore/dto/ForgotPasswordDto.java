package com.bridgelabz.bookstore.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDto {

	@NotEmpty
	@Email(message="Enter the valid emailId")
	private String  emailId;
}
