package com.map.moloapi.contracts.users.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * @author DIOMANDE Souleymane
 * @Date 08/10/2022 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationRequest {
    @NotEmpty
    @NotNull
    private String paiementId;
    private String userId;
    @NotEmpty
    @NotNull
    private String referenceSib;
    private String referenceRtgs;
}
