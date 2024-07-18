package com.map.moloapi.contracts;

import com.map.moloapi.utils.constants.TechnicalMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Response {
    private boolean hasError;
    private String message;
    private Object data;

    public Response() {
    }

    public Response success() {
        this.hasError = false;
        this.message = TechnicalMessage.SUCCESS_MESSAGE;
//        this.data = data;
        this.data = null;
        return this;
    }

    public Response success(Object data) {
        this.hasError = false;
        this.message = TechnicalMessage.SUCCESS_MESSAGE;
        this.data = data;
        return this;
    }

    public Response success(String message) {
        this.hasError = false;
        this.message = message;
//        this.data = data;
        this.data = null;
        return this;
    }


    public Response success(String message, Object data) {
        this.hasError = false;
        this.message = message;
        this.data = data;
        return this;
    }


    public Response error() {
        this.data = null;
        this.hasError = true;
        this.message = TechnicalMessage.ERROR_MESSAGE;
        return this;
    }

    public Response error(Object data) {
        this.hasError = true;
        this.message = TechnicalMessage.ERROR_MESSAGE;
        this.data = data;
        return this;
    }

    public Response error(String message) {
        this.data = null;
        this.hasError = true;
        this.message = message;
        return this;
    }

    public Response error(String message, Object data) {
        this.hasError = true;
        this.message = message;
        this.data = data;
        return this;
    }
}
