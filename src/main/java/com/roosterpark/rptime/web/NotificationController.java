package com.roosterpark.rptime.web;

import com.roosterpark.rptime.service.EmailNotificationService;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class NotificationController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    @Inject
    private EmailNotificationService service;
    
	@RequestMapping(value = "/notify/weekly", method = GET)
	@ResponseBody
	public Integer weeklyNotification() {
        Integer messagesSent = service.sendWeeklyNotification();
        
        LOGGER.info("Sent out {} notifications.", messagesSent);
        
        return messagesSent;
	}
}
