package com.cbulims.cbulims.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {
	
	private final JavaMailSender mailSender;
	
	public MailSenderService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	public void sendEmail(String toEmail, String subject, String body) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("brightdev.jobs@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
	}
}
