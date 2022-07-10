package com.cbulims.cbulims.controller;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.cbulims.cbulims.model.Notification;
import com.cbulims.cbulims.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.wiring.ClassNameBeanWiringInfoResolver;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cbulims.cbulims.model.ChemicalExpiryMessage;
import com.cbulims.cbulims.model.IDList;
import com.cbulims.cbulims.model.chemical;
import com.cbulims.cbulims.repository.ChemicalRepository;
import com.cbulims.cbulims.repository.ExpiryMessagesRepository;
import com.cbulims.cbulims.repository.IDListRepository;

@Slf4j
@Controller
public class ChemicalsController {
	
	Logger logger = Logger.getLogger(ClassNameBeanWiringInfoResolver.class.getName());

	private final ChemicalRepository chemicalRepository;
	private final IDListRepository idListRepository;
	private final ExpiryMessagesRepository expMessageRepository;
	private final NotificationRepository notificationRepository;
	
	
	public ChemicalsController(ChemicalRepository chemicalRepository, IDListRepository idlistRepository, ExpiryMessagesRepository expMessageRepository, NotificationRepository notificationRepository) {
		super(); 
		this.chemicalRepository = chemicalRepository;
		this.idListRepository = idlistRepository;
		this.expMessageRepository = expMessageRepository;
		this.notificationRepository = notificationRepository;
	}
	
	@GetMapping("/chemicals/all")
	public String showAllChemicals(Model model) {
		model.addAttribute("allchemicals", chemicalRepository.findAll());
		model.addAttribute("deletechem", new chemical());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Chemicals/chemicalsall";
	}
	
	@GetMapping("/chemicals/toexpire")
	public String showExpiredChemicals(Model model) {
		model.addAttribute("soontoexp", chemicalRepository.findByToExpireTrue());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Chemicals/chemicalsexpire";
	}
	 
	@GetMapping("/chemicals/addnew")
	public String showOrderedChemicals(Model model) {
		model.addAttribute("idlist", idListRepository.findAll());
		model.addAttribute("newchemical", new chemical());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Chemicals/addnewchem";
	}

	@PostMapping("/addchemical")
	public String addNewChemical(@ModelAttribute("newchemical")chemical chem) {
		chemical subjectChem = chemicalRepository.findByChemName(chem.getChemName());
		IDList subjectId = idListRepository.findByProductName(chem.getChemName());
		log.info("Id Min:{}, Id Max; {}", subjectId.getMinimum(), subjectId.getMaximum());
		//UPDATE if chemical already exists else SAVE
		if (subjectChem != null)  
		{
			subjectChem.setToExpire(checkExpiry(subjectChem));
			subjectChem.setChemQuantity(subjectChem.getChemQuantity() + chem.getChemQuantity());
			subjectChem.setChemCondition(chem.getChemCondition());
			subjectChem.setExpiryDate(chem.getExpiryDate());
			log.info("subjectchem min:{}, subjectchem max:{}", subjectChem.getChemMin(), subjectChem.getChemMax());

			//Set Condition
			subjectChem.setChemCondition(condition(subjectChem));
			//Delete from expired chemicals list if toExpire is false
			if((!subjectChem.isToExpire()) && (expMessageRepository.findByChemicalName(subjectChem.getChemName()) != null))
				expMessageRepository.deleteByChemicalName(subjectChem.getChemName());
			chemicalRepository.save(subjectChem);

			Notification notification = newNotification(subjectChem, "has been updated");
			notificationRepository.save(notification);
		}
		else {
			chem.setChemMin(subjectId.getMinimum());
			chem.setChemMax(subjectId.getMaximum());
			chem.setChemCondition(condition(chem));
			chem.setToExpire(checkExpiry(chem));
			chemicalRepository.save(chem);


			Notification notification = newNotification(chem, "added to inventory.");
			notificationRepository.save(notification);
		}
		
		return "redirect:/chemicals/all";
	}


	//Updated Via Edit
	@PostMapping("/chemical/update")
	public String Update(@ModelAttribute("newchemical")chemical chem) {
		chemical subjectChem = chemicalRepository.findByChemName(chem.getChemName());
		IDList subjectId = idListRepository.findByProductName(chem.getChemName());

		subjectChem.setToExpire(checkExpiry(subjectChem));
		subjectChem.setChemQuantity(chem.getChemQuantity());
		subjectChem.setChemCondition(chem.getChemCondition());
		subjectChem.setChemMin(subjectId.getMinimum());
		subjectChem.setChemMax(subjectId.getMaximum());
		subjectChem.setExpiryDate(chem.getExpiryDate());
		subjectChem.setChemCondition(condition(subjectChem));

		Notification notification = newNotification(subjectChem, "Has been updated via Edit.");
		notificationRepository.save(notification);

		//Delete from expired chemicals list if toExpire is false
		if((!subjectChem.isToExpire()) && (expMessageRepository.findByChemicalName(subjectChem.getChemName()) != null))
			expMessageRepository.deleteByChemicalName(subjectChem.getChemName());
		chemicalRepository.save(subjectChem);


		return "redirect:/chemicals/all";
	}
	
	@RequestMapping(value = "/deletechem")
	private String deleteRow(@RequestParam Integer chemId){
		chemical chem = chemicalRepository.findById(chemId).orElse(null);
		if (chem != null){
			Notification notification = newNotification(chem, "has been deleted");
			chemicalRepository.deleteById(chemId);
		}
	    return "redirect:/chemicals/all";
	}
	
	@RequestMapping(value = "/editchem")
	private String editRow(@RequestParam Integer chemId, Model model) {
		chemical chem = chemicalRepository.findById(chemId).orElse(null);
		if (chem != null) {
			model.addAttribute("editrecord", chem);
			return "Chemicals/editchem";
		}
		return "redirect:chemicals/all";
	}
	
	private boolean checkExpiry(chemical subjectChem) {
		int chemExpiryYear = subjectChem.getExpiryDate().getYear();
		int chemExpiryMonth = subjectChem.getExpiryDate().getMonth();
		int currentYear = new Date().getYear();
		int currentMonth = new Date().getMonth();
		
		if( chemExpiryYear <= currentYear ) {
			if (chemExpiryMonth <= currentMonth) {
				return true;
			}
			else return (chemExpiryMonth - currentMonth) <= 1;
		}
		
		return false;
	}

//	Checks every 24 hours to see if a chemical has expired
	@Scheduled(cron="0 05 11 * * *")
	private void checkExpiry24hrs() {
		List<chemical> allChemicals = chemicalRepository.findAll();
		for (chemical chem : allChemicals) {
			chem.setToExpire(checkExpiry(chem));
			if(chem.isToExpire()) {
				updateExpiryMessages(chem);
				Notification notification = newNotification(chem, "expires soon/expired. Please check exp date.");
				notificationRepository.save(notification);
				log.warn("New chemical expired/ to expire soon.");
			}
			chemicalRepository.save(chem);
		}
		logger.info("Daily chemical expiry check complete");
	}

//	Checks per 24hours to see if chemicals have quantity less than minimum
//  If less, create new notification
	@Scheduled(cron="0 05 11 * * *")
	private void checkLess24hrs() {
		List<chemical> allChemicals = chemicalRepository.findAll();
		for (chemical chem : allChemicals) {
			int minimum = idListRepository.findByProductName(chem.getChemName()).getMinimum();
			int quantity = chem.getChemQuantity();
			if (quantity <= minimum){
				Notification notification = newNotification(chem, "is close to completion.");
				notificationRepository.save(notification);
			}
		}
		log.info("Daily chemical expiry check complete");
	}

	private void updateExpiryMessages(chemical chem) {
		String date = chem.getExpiryDate().toGMTString();
		String name = chem.getChemName();
		String condition = chem.getChemCondition();
		ChemicalExpiryMessage expiryMessage = new ChemicalExpiryMessage(date, name, condition);
		expMessageRepository.save(expiryMessage);
	}
	private Notification newNotification(chemical chem, String message) {
		Notification notification = new Notification();
		notification.setMessage(chem.getChemName() + " " + message );
		notification.setOpened(false);
		return notification;
	}

	private String condition(chemical subjectChem) {
		double min = subjectChem.getChemMin();
		double max = subjectChem.getChemMax();
		double range = max - min;
		log.info("range: {}, min: {}, max: {}", range, min, max);
		double qty = subjectChem.getChemQuantity();
		double percent = 0;

		log.info("qty: {}, percent-before: {}", qty, percent);
		percent = Math.round((qty / max) * 100);
		log.info("qty: {}, percent-after: {}", qty, percent);

		if (percent == 0) {
			log.info("percent 0 so added empty");
			return ("Empty");
		}
		else if (percent < 35) {
			log.info("percent < 35 so added low");
			return ("Low");
		}
		else if (percent < 55) {
			log.info("percent < 55 so added okay");
			return ("Okay");
		}
		else if (percent < 85) {
			log.info("percent < 85 so added Good");
			return ("Good");
		}
		else {
			log.info("percent > 85 so added full");
			return("Full");
		}
	}
}
