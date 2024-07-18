package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.services.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 07/03/2024 17:28
 */
@RestController
@RequestMapping("/tests")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Tests")
public class TestController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/mail")
    public Response mail() throws MessagingException {
        notificationService.sendSimpleMail();
        return new Response().success();
    }
}
