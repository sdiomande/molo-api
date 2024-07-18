package com.map.moloapi.services;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.utils.Utilities;
import com.map.moloapi.utils.constants.TechnicalMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 21/02/2024 15:59
 */
@Slf4j
@Service
public class PasswordService {
    Response response = new Response();

    public Response digit() {
        log.info("-- Debut generation de mot de passe numerique --");
        String password = Utilities.generateDigitPassword();
        log.info("-- Fin generation mot de passe --");
        return response.success(new HashMap() {
            {
                put("password", password);
            }
        });
    }

    public Response random() {
        log.info("-- Debut generation de mot de passe aleatoire --");
        String password = Utilities.generateSecurePassword();
        log.info("-- Fin generation mot de passe --");
        return response.success(new HashMap() {
            {
                put("password", password);
            }
        });
    }

}
