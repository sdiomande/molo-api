package com.map.moloapi.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 21/02/2024 12:59
 */
@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion des alertes")
public class AlertController {
}
