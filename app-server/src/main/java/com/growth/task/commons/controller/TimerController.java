package com.growth.task.commons.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin
@Controller
@Tag(name = "Timer", description = "시간 API")
public class TimerController {
    private final SimpMessagingTemplate messagingTemplate;

    public TimerController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/current-time")
    @Scheduled(fixedRate = 1000)
    @SendTo("/topic/current-time")
    public void sendCurrentTime() {
        ZonedDateTime currentTime = ZonedDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        messagingTemplate.convertAndSend("/topic/current-time", formattedTime);
    }
}
