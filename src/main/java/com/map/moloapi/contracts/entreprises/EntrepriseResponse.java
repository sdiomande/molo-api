package com.map.moloapi.contracts.entreprises;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 13/02/2024 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepriseResponse {
    private String name;
}
