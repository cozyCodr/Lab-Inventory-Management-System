package com.cbulims.cbulims.controller;

import com.cbulims.cbulims.model.IDList;
import com.cbulims.cbulims.model.Notification;
import com.cbulims.cbulims.model.chemical;
import com.cbulims.cbulims.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
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

import java.util.List;

@Slf4j
@Controller
public class InstrumentController {

	private final InstrumentRepository instrumentRepository;
	private final IDListRepository idlistRepository;
	private final NotificationRepository notificationRepository;

	public InstrumentController(InstrumentRepository instrumentRepository, IDListRepository idlistRepository, NotificationRepository notificationRepository) {
		super();
		this.instrumentRepository = instrumentRepository;
		this.idlistRepository = idlistRepository;
		this.notificationRepository = notificationRepository;
	}
	
	@GetMapping("/instruments/all")
	public String showAllInstruments(Model model) {
		model.addAttribute("allinstruments", instrumentRepository.findAll());
		model.addAttribute("deleteinstrument", new Instrument());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Instruments/instrumentsall";
	}
	
	@GetMapping("/instruments/damaged")
	public String showDamagedInstruments(Model model) {
		model.addAttribute("damaged", instrumentRepository.findByDamagedTrue());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Instruments/damagedins";
	}

	//todo: Add Instruments running out to lists of stock running out
	@GetMapping("/instruments/addnew")
	public String addNewInstrument(Model model) {
		model.addAttribute("idlist", idlistRepository.findAll());
		model.addAttribute("newinstrument", new Instrument());
		model.addAttribute("notscount", (long) notificationRepository.findAll().size());
		return "Instruments/addnewinstrument";
	}

	@PostMapping("/saveedit")
	public String updateEditedIntrument(@ModelAttribute("newinstrument")Instrument instrument){
		if("Not Damaged".equals(instrument.getInsCondition())) {
			instrument.setDamaged(false);
		}
		if ("Damaged".equals(instrument.getInsCondition())) {
			instrument.setDamaged(true);
		}
		Notification notification = newNotification(instrument, "updated from edit");
		instrumentRepository.save(instrument);
		notificationRepository.save(notification);
		return "redirect:/instrument/all";
	}
	
	@PostMapping("saveinstrument")
	public String saveNewInstrument(@ModelAttribute("newinstrument")Instrument instrument) {

		//Check if Instrument Exists
		Instrument inst = instrumentRepository.findById(instrument.getId()).orElse(null);
		IDList instId = idlistRepository.findByProductName(instrument.getInsName());
		int minimum = instId.getMinimum();

		// If not null, add to existing quantity
		if("Not Damaged".equals(instrument.getInsCondition())) {
			instrument.setDamaged(false);
		}
		if ("Damaged".equals(instrument.getInsCondition())) {
			instrument.setDamaged(true);
		}
		if (inst != null){
			//save previous quantity
			int prevQty = inst.getInsQuantity();

			//Assign to new instrument
			inst = instrument;
			inst.setInsQuantity(instrument.getInsQuantity() + prevQty);
			instrumentRepository.save(instrument);
			Notification notification = newNotification(inst, "has been updated.");
			notificationRepository.save(notification);

			//Check if less quantity is less than minimum
			if (inst.getInstMin() <= minimum){
				Notification notification1 = newNotification(inst, "stock is below the required minimum");
				notificationRepository.save(notification1);
			}

		}
		else {
			instrumentRepository.save(instrument);
			Notification notification = newNotification(instrument, "has been added to inventory (new).");
			notificationRepository.save(notification);

			//Check if quantity is less than minimum
			if (instrument.getInstMin() <= minimum){
				Notification notification1 = newNotification(instrument, "stock is below the required minimum");
				notificationRepository.save(notification1);
			}
		}

		return "redirect:/instruments/all";
	}

	private Notification newNotification(Instrument inst, String message) {
		Notification notification = new Notification();
		notification.setMessage(inst.getInsName() + " " + message);
		notification.setOpened(false);
		return notification;
	}

	@RequestMapping(value = "/deleteinstrument")
	private String deleteRow(@RequestParam Integer id){
	    instrumentRepository.deleteById(id);
	    return "redirect:/instruments/all";
	}
	
	@RequestMapping(value = "/editinstrument")
	private String editRow(@RequestParam Integer id, Model model) {
		model.addAttribute("editrecord", instrumentRepository.findById(id));
		return "Instruments/editins";
	}

//	Checks if instrument is damaged every 24 hours at 11:06
	@Scheduled(cron="0 06 11 * * *")
	private void checkDamaged24hrs() {
		List<Instrument> allInstruments = instrumentRepository.findAll();
		for (Instrument instrument : allInstruments) {
			if ("Damaged".equals(instrument.getInsCondition())){
				Notification notification = newNotification(instrument, "damaged. Please replace.");
				notificationRepository.save(notification);
			}
		}
		log.info("Daily Damaged Instrument check complete");
	}

	//	Checks if instrument is below required minimum every 24 hours at 11:07
	@Scheduled(cron="0 07 11 * * *")
	private void checkIfLow24hrs() {
		List<Instrument> allInstruments = instrumentRepository.findAll();
		for (Instrument instrument : allInstruments) {
			IDList instId = idlistRepository.findByProductName(instrument.getInsName());
			int min = instId.getMinimum();
			if (instrument.getInstMin() <= min){
				Notification notification = newNotification(instrument,"stock is below required minimum");
			}
		}
		log.info("Daily Instrument stock check complete");
	}
}
