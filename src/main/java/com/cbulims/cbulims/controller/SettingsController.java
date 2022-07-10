package com.cbulims.cbulims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cbulims.cbulims.model.IDList;
import com.cbulims.cbulims.repository.IDListRepository;

@Controller
public class SettingsController {
	
private final IDListRepository idListRepository;
	
	public SettingsController(IDListRepository idListRepository) {
		this.idListRepository = idListRepository;
	}
	
	@PostMapping("/addid")
	public String addNewId(@ModelAttribute("idlist")IDList idlist, Model model) {		
		IDList prod = idListRepository.findByProductName(idlist.getProductName());
		if (prod == null) {
			idListRepository.save(idlist);     //Add New Id to Database If and Only if it does not already exist.
		}
		model.addAttribute("idlist", idListRepository.findAll());
		return "redirect:/settings";
	}
	
	@RequestMapping(value = "/delete/{id}")
	private String deleteStudent(@PathVariable(name = "id") String id){
	    idListRepository.deleteById(Integer.parseInt(id));
	    return "redirect:/settings";
	}
}