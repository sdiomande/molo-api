package com.map.moloapi.contracts.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @project socoprim-internal-api
 * @Date 22/04/2024 13:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponse {
    private String date;
    private String dateCreation;
    //
    private String tagId;
    private String voie;
    private String dhms;
    private Long ffAmount;
    private Long amount;
    private String etat;
    //
    private String actionId;
    private String partnerDhms;
    private String badge;
    private String reference;
    private String clientInfo;
    private String status;
}
