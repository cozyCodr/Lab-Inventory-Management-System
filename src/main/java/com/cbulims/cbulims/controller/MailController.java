package com.cbulims.cbulims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cbulims.cbulims.model.SentOrder;
import com.cbulims.cbulims.repository.SentOrderRepository;
import com.cbulims.cbulims.service.MailSenderService;

@Controller
public class MailController {
	
	private final MailSenderService mailSenderService;
	private final SentOrderRepository sentOrderRepository;
	
	public MailController(MailSenderService mailSenderService, SentOrderRepository sentOrderRepository) {
		super();
		this.mailSenderService = mailSenderService;
		this.sentOrderRepository = sentOrderRepository;
	}

	@PostMapping("/sendorder")
	public String sendtopurchasing(@ModelAttribute("order")SentOrder order, Model model) {
		//Collected Model Attributes
		String productId = order.getProduct();
		String issue = order.getIssue();
		String comments = order.getComments();
		String quantity;
		
		//Set chemical or instrument quantity
		if(order.getQuantitychem() != 0) {
			quantity = Integer.toString(order.getQuantitychem());
		} else {
			quantity = Integer.toString(order.getQuantityins());
		}
		
		//send email
		String toEmail = "purchasing.dept.office@gmail.com";
		String subject = "Lab Order -> Issue: " + issue 
				+ " Product: " + productId 
				+ " Quantity: " + quantity + ".";
		String body = comments;
		mailSenderService.sendEmail(toEmail, subject, body);
		
		//add to database
		sentOrderRepository.save(order);
		
		model.addAttribute("orderlist", sentOrderRepository.findAll());
		
		return "Dashboard/ordered";
	}	
}
