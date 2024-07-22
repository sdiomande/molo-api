package com.map.moloapi.exceptions;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.exceptions.rest.BadRequestException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 19/02/2024 19:08
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Autowired
    Response response;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response handleExceptions(Exception ex) {
        log.error("Une erreur interne s'est produite, raison : {}", ex.getMessage());
        return response.error();
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response validationException(ValidationException ex) {
        log.error("## Erreur de validation mot de passe : {}", ex.getMessage());
        return response.error("Le mot de passe doit respecter les règles de sécurités");
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response badRequestException(BadRequestException ex) {
//        log.error("## Erreur de validation mot de passe : {}", ex.getMessage());
        return response.error(ex.getMessage());
    }
}
