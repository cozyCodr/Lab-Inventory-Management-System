package com.cbulims.cbulims.controller;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

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

@Controller
public class ChemicalsController {
	
	Logger logger = Logger.getLogger(ClassNameBeanWiringInfoResolver.class.getName());

	private final ChemicalRepository chemicalRepository;
	private final IDListRepository idListRepository;
	private final ExpiryMessagesRepository expMessageRepository;
	
	
	public ChemicalsController( ChemicalRepository chemicalRepository, IDListRepository idlistRepository, ExpiryMessagesRepository expMessageRepository) {
		super(); 
		this.chemicalRepository = chemicalRepository;
		this.idListRepository = idlistRepository;
		this.expMessageRepository = expMessageRepository;
	}
	
	@GetMapping("/chemicals/all")
	public String showAllChemicals(Model model) {
		model.addAttribute("allchemicals", chemicalRepository.findAll());
		model.addAttribute("deletechem", new chemical());
		return "Chemicals/chemicalsall";
	}
	
	@GetMapping("/chemicals/toexpire")
	public String showExpiredChemicals(Model model) {
		model.addAttribute("soontoexp", chemicalRepository.findByToExpireTrue());
		return "Chemicals/chemicalsexpire";
	}
	 
	@GetMapping("/chemicals/addnew")
	public String showOrderedChemicals(Model model) {
		model.addAttribute("idlist", idListRepository.findAll());
		model.addAttribute("newchemical", new chemical());
	
		return "Chemicals/addnewchem";
	}
	
	@PostMapping("addchemical")
	public String addNewChemical(@ModelAttribute("newchemical")chemical chem) {
		chemical subjectChem = chemicalRepository.findByChemName(chem.getChemName());
		IDList subjectId = idListRepository.findByProductname(chem.getChemName());		
		
		//UPDATE if chemical already exists else SAVE
		if (subjectChem != null) 
		{
			checkExpiry(subjectChem);
			updateChemical(chem, subjectChem, subjectId);
			chemicalRepository.save(subjectChem);
		}
		else {
			chem.setToExpire(checkExpiry(chem));
			chemicalRepository.save(chem);
		}
		
		return "redirect:/chemicals/all";
	}
	
	@RequestMapping(value = "/deletechem")
	private String deleteRow(@RequestParam Integer chemId){
	    chemicalRepository.deleteById(chemId);
	    return "redirect:/chemicals/all";
	}
	
	@RequestMapping(value = "/editchem")
	private String editRow(@RequestParam Integer chemId, Model model) {
		model.addAttribute("editrecord", chemicalRepository.findById(chemId));
		return "Chemicals/editchem";
	}
	
	private void updateChemical(chemical chem, chemical subjectChem, IDList subjectId) {
		subjectChem.setChemQuantity(subjectChem.getChemQuantity() + chem.getChemQuantity());
		subjectChem.setChemCondition(chem.getChemCondition());
		subjectChem.setChemMin(subjectId.getMinimum());
		subjectChem.setChemMax(subjectId.getMaximum());
		subjectChem.setExpiryDate(chem.getExpiryDate());
		subjectChem.setChemCondition(checkCondition(subjectChem));
	}

	private String checkCondition(chemical subjectChem) {
		int min = subjectChem.getChemMin();
		int max = subjectChem.getChemMax();
		int range = max - min;
		int qty = subjectChem.getChemQuantity();
		int percent = Math.round( (qty/range) * 100 );
		
		if (min == 0) {
			return ("Empty");
		}
		else if (percent >= 100) {
			return ("Full");
		}
		else if (percent < 35) {
			return ("Low");
		}
		else if (percent < 55) {
			return ("Okay");
		}
		else if (percent < 85) {
			return ("Good");
		} 
		else {
			return ("Excellent");
		}
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
			else if((chemExpiryMonth - currentMonth) <= 1) {
				return true;
			}
		}
		
		return false;
	}

	@Scheduled(cron="0 05 11 * * *")
	private void checkExpiry24hrs() {
		List<chemical> allChemicals = chemicalRepository.findAll();
		for (chemical chem : allChemicals) {
			chem.setToExpire(checkExpiry(chem));
			if(chem.isToExpire()) {
				updateExpiryMessages(chem);
				logger.warning("New chemical expired/ to expire soon.");
			}
			chemicalRepository.save(chem);
		}
		logger.info("Daily chemical expiry check complete");
	}

	private void updateExpiryMessages(chemical chem) {
		String date = chem.getExpiryDate().toGMTString();
		String name = chem.getChemName();
		String condition = chem.getChemCondition();
		ChemicalExpiryMessage expiryMessage = new ChemicalExpiryMessage(date, name, condition);
		expMessageRepository.save(expiryMessage);
	}
}
