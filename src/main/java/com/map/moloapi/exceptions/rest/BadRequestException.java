package com.map.moloapi.exceptions.rest;

import java.util.Formatter;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 21/07/2024 16:25
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Object ...args) {
        super(new Formatter().format(message, args).toString());
    }

}
