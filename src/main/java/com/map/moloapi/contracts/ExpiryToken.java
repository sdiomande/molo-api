package com.map.moloapi.contracts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @Date 27/04/2023 07:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpiryToken {
    private boolean expiry;
    private String message;
}
