package com.map.moloapi.contracts.reports;

import com.map.moloapi.utils.enums.EtatEnum;
import com.map.moloapi.utils.enums.StatutEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @project molo-api
 * @Date 24/04/2024 09:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportRequest {
    String partenaire;
    String dateDebut;
    String dateFin;
    EtatEnum etat;
    StatutEnum statut;
}
