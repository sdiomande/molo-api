package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.services.PasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 21/02/2024 15:58
 */
@RestController
@RequestMapping("/passwords")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Generation de mot de passe")
public class PasswordController {
    @Autowired
    PasswordService passwordService;

    @GetMapping()
    @Operation(summary = "Numerique")
    public Response digit() {
        return passwordService.digit();
    }

    @GetMapping("/randoms")
    @Operation(summary = "Aleatoire")
    public Response random() {
        return passwordService.random();
    }

}
