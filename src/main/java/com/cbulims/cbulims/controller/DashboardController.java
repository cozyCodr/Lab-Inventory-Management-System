package com.cbulims.cbulims.controller;

import com.cbulims.cbulims.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cbulims.cbulims.model.IDList;
import com.cbulims.cbulims.model.SentOrder;

@Controller
public class DashboardController {

	private final SentOrderRepository sentOrderRepository;
	private final IDListRepository idListRepository;
	private final ChemicalRepository chemicalRepository;
	private final InstrumentRepository instrumentRepository;
	private final ExpiryMessagesRepository expiryMessagesRepository;
	private final NotificationRepository notificationRepository;
	
	public DashboardController(SentOrderRepository sentOrderRepository, IDListRepository idListRepository, ChemicalRepository chemicalRepository, InstrumentRepository instrumentRepository, ExpiryMessagesRepository expiryMessagesRepository, NotificationRepository notificationRepository) {
		super();
		this.sentOrderRepository = sentOrderRepository;
		this.idListRepository = idListRepository;
		this.chemicalRepository = chemicalRepository;
		this.instrumentRepository = instrumentRepository;
		this.expiryMessagesRepository = expiryMessagesRepository;
		this.notificationRepository = notificationRepository;
	}

	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		model.addAttribute("chemcount", (long) chemicalRepository.findAll().size());
		model.addAttribute("instcount", (long) instrumentRepository.findAll().size());
		model.addAttribute("expcount", (long) expiryMessagesRepository.findAll().size());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		model.addAttribute("ordcount", (long) sentOrderRepository.findAll().size());
		return "Dashboard/dashboard";
	}

	@GetMapping("/")
	public String home(Model model) {
		return "redirect:/dashboard";
	}

	@GetMapping("/settings")
	public String showSettings(Model model) {
		model.addAttribute("idlist", idListRepository.findAll());
		model.addAttribute("newid", new IDList());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Dashboard/settings";
	}
	
	@GetMapping("/ordered")
	public String showOdered(Model model) {
		model.addAttribute("orderlist", sentOrderRepository.findAll());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Dashboard/ordered";
	}
	
	@GetMapping("/notify")
	public String showNotifyAdmin(Model model) {
		model.addAttribute("order", new SentOrder());
		model.addAttribute("idlist", idListRepository.findAll());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Dashboard/notify";
	}
}
