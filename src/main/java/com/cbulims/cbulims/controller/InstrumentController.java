package com.cbulims.cbulims.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cbulims.cbulims.model.Instrument;
import com.cbulims.cbulims.repository.IDListRepository;
import com.cbulims.cbulims.repository.InstrumentRepository;

@Controller
public class InstrumentController {

	private final InstrumentRepository instrumentRepository;
	private final IDListRepository idlistRepository;
	public InstrumentController(InstrumentRepository instrumentRepository, IDListRepository idlistRepository) {
		super();
		this.instrumentRepository = instrumentRepository;
		this.idlistRepository = idlistRepository;
	}
	
	@GetMapping("/instruments/all")
	public String showAllInstruments(Model model) {
		model.addAttribute("allinstruments", instrumentRepository.findAll());
		model.addAttribute("deleteinstrument", new Instrument());
		return "Instruments/instrumentsall";
	}
	
	@GetMapping("/instruments/damaged")
	public String showDamagedInstruments() {
		return "instruments/damaged";
	}
	
	@GetMapping("/instruments/addnew")
	public String addNewInstrument(Model model) {
		model.addAttribute("idlist", idlistRepository.findAll());
		model.addAttribute("newinstrument", new Instrument());
		return "Instruments/addnewinstrument";
	}
	
	@PostMapping("saveinstrument")
	public String saveNewInstrument(@ModelAttribute("newinstrument")Instrument instrument) {
		instrumentRepository.save(instrument);
		return "redirect:/instruments/all";
	}
	
	@RequestMapping(value = "/deleteinstrument")
	private String deleteRow(@RequestParam Integer id){
	    instrumentRepository.deleteById(id);
	    return "redirect:/instruments/all";
	}
	
	@RequestMapping(value = "/editinstrument")
	private String editRow(@RequestParam Integer id, Model model) {
		model.addAttribute("editrecord", instrumentRepository.findById(id));
		return "Instruments/editinstrument";
	}
	
}
