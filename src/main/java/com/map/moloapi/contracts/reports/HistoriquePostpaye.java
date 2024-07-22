package com.map.moloapi.contracts.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 22/04/2024 13:22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoriquePostpaye {
    private String date;
    private String tagId;
    private String voie;
    private String dhms;
    private Long ffAmount;
    private Long amount;
    private String dateCreation;
    private String etat;
}
