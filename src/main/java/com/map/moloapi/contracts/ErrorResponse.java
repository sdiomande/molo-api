package com.map.moloapi.contracts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DIOMANDE Souleymane
 * @Date 02/10/2022 00:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private String exception;
    private String field;
    private String message;
    private List<String> errors;
    private String rejectedValue;
}
