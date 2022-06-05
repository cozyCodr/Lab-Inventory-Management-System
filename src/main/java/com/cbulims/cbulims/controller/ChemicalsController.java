package com.cbulims.cbulims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cbulims.cbulims.model.chemical;
import com.cbulims.cbulims.repository.ChemicalRepository;
import com.cbulims.cbulims.repository.IDListRepository;

@Controller
public class ChemicalsController {

	private final ChemicalRepository chemicalRepository;
	private final IDListRepository idlistRepository;
	public ChemicalsController(ChemicalRepository chemicalRepository, IDListRepository idlistRepository) {
		super();
		this.chemicalRepository = chemicalRepository;
		this.idlistRepository = idlistRepository;
	}
	
	@GetMapping("/chemicals/all")
	public String showAllChemicals(Model model) {
		model.addAttribute("allchemicals", chemicalRepository.findAll());
		model.addAttribute("deletechem", new chemical());
		return "Chemicals/chemicalsall";
	}
	
	@GetMapping("/chemicals/toexpire")
	public String showExpiredChemicals(Model model) {
		model.addAttribute("soontoexp",chemicalRepository.findByOrderByExpiryDateAsc());
		return "Chemicals/chemicalsexpire";
	}
	
	@GetMapping("/chemicals/addnew")
	public String showOrderedChemicals(Model model) {
		model.addAttribute("idlist", idlistRepository.findAll());
		model.addAttribute("newchemical", new chemical());
		return "Chemicals/addnewchem";
	}
	
	@PostMapping("addchemical")
	public String addNewChemical(@ModelAttribute("newchemical")chemical chem) {
		chemicalRepository.save(chem);
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
	
}
