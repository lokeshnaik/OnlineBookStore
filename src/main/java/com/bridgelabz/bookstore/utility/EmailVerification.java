package com.bridgelabz.bookstore.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailVerification {

	@Autowired
	private JavaMailSender mailsender;
	
	@Value("${spring.mail.username}")
	private String SENDER_EMAIL_ID;
	
	SimpleMailMessage mail = new SimpleMailMessage();
	

	public void sendVerifyMail(String email, String token) throws MailException {
	
		mail.setFrom(SENDER_EMAIL_ID);
		mail.setTo(email);
		mail.setSubject("Verification of user");
		mail.setText("click here..." + token);
		mailsender.send(mail);
	}

	public void sendForgetPasswordMail(String email, String token) throws MailException {
		
		mail.setFrom(SENDER_EMAIL_ID);
		mail.setTo(email);
		mail.setSubject("Forget password link");
		mail.setText("click here..." + token);
		mailsender.send(mail);
	}
}
