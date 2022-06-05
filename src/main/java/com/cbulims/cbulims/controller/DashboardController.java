package com.cbulims.cbulims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cbulims.cbulims.model.IDList;
import com.cbulims.cbulims.model.SentOrder;
import com.cbulims.cbulims.repository.IDListRepository;
import com.cbulims.cbulims.repository.SentOrderRepository;

@Controller
public class DashboardController {

	private final SentOrderRepository sentOrderRepository;
	private final IDListRepository idListRepository;
	
	public DashboardController(SentOrderRepository sentOrderRepository, IDListRepository idListRepository) {
		super();
		this.sentOrderRepository = sentOrderRepository;
		this.idListRepository = idListRepository;
	}

	@GetMapping("/settings")
	public String showSettings(Model model) {
		model.addAttribute("idlist", idListRepository.findAll());
		model.addAttribute("newid", new IDList());
		return "Dashboard/settings";
	}
	
	@GetMapping("/log")
	public String showLog() {
		return "Dashboard/log";
	}
	
	@GetMapping("/notifications")
	public String showNots() {
		return "Dashboard/notifications";
	}
	
	@GetMapping("/ordered")
	public String showOdered(Model model) {
		model.addAttribute("orderlist", sentOrderRepository.findAll());
		return "Dashboard/ordered";
	}
	
	@GetMapping("/notify")
	public String showNotifyAdmin(Model model) {
		model.addAttribute("order", new SentOrder());
		model.addAttribute("idlist", idListRepository.findAll());
		return "Dashboard/notify";
	}
}
