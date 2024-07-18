package com.map.moloapi.contracts.partners;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 13/02/2024 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerResponse {
    private String name;
}
