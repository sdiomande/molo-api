package com.map.moloapi.contracts.reports;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 25/04/2024 10:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tag {
    private String date;
    private String tagId;
//    private String voie;
//    private String dhms;
//    private Long ffAmount;
//    private Long amount;
    private String dateCreation;
    private String etat;
}
