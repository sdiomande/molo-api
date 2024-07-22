package com.map.moloapi.exceptions.rest;

import java.util.Formatter;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 21/07/2024 16:25
 */
public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException() {
        super();
    }

    public AlreadyExistException(String message) {
        super(message);
    }

    public AlreadyExistException(String message, Object ...args) {
        super(new Formatter().format(message, args).toString());
    }
}
