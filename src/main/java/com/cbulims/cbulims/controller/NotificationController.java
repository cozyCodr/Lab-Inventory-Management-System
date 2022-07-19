package com.cbulims.cbulims.controller;

import com.cbulims.cbulims.model.Notification;
import com.cbulims.cbulims.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;

    public NotificationController(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @GetMapping
    public String showNotifications(Model model) {
        model.addAttribute("notslist", notificationRepository.findAll());
        model.addAttribute("notscount", (long) notificationRepository.findAllByOpenedFalse().size());
        return "notifications";
    }

    @RequestMapping(value = "/markseen")
    public String markNotificationAsRead(@RequestParam Integer notId){
        log.info("Mark seen triggered");
        Notification notification = notificationRepository.findById(notId).orElse(null);

        if(notification != null){
            log.info("notification not null");
            notification.setOpened(true);
            notificationRepository.save(notification);
        }
        return "redirect:/notifications";
    }
}
