package com.map.moloapi.exceptions;

import com.map.moloapi.contracts.Response;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 19/02/2024 19:08
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleExceptions(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(new Response().error(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public ResponseEntity<?> validationException(ValidationException ex) {
        log.error("## Erreur de validation mot de passe : {}", ex.getMessage());
        return new ResponseEntity<>(new Response().error("Le mot de passe doit respecter les règles de sécurités"), HttpStatus.BAD_REQUEST);
    }
}
