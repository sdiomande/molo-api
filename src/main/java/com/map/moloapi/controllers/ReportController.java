package com.map.moloapi.controllers;

import com.map.moloapi.contracts.reports.ReportRequest;
import com.map.moloapi.services.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 21/02/2024 12:59
 */
@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion des rapports")
public class ReportController {

    @Autowired
    ReportService reportService;

    @PostMapping("/tags")
    @Operation(summary = "Liste des badges cree par periode")
    public ResponseEntity<?> tags(@RequestBody ReportRequest request) {
        return reportService.tags(request);
    }

    @PostMapping("/historiquePostpayes")
    @Operation(summary = "Liste des historiques prepayes")
    public ResponseEntity<?> historiquePostpayes(@RequestBody ReportRequest request) {
        return reportService.historiquePostpaye(request);
    }

    @PostMapping("/rechargePartenaires")
    @Operation(summary = "Liste des rechargements par partenaires")
    public ResponseEntity<?> rechargementPartenaire(@RequestBody ReportRequest request) {
        return reportService.rechargementPartenaire(request);
    }

    @PostMapping("/nbrTotalAbonnes")
    @Operation(summary = "Nombre total d'abonnes")
    public ResponseEntity<?> nbrTotalAbonnes(@RequestBody ReportRequest request) {
        return reportService.nbrTotalAbonnes(request);
    }

    @PostMapping("/nbrTotalTransactions")
    @Operation(summary = "Nombre total des transactions")
    public ResponseEntity<?> nbrTotalTransactions(@RequestBody ReportRequest request) {
        return reportService.nbrTotalTransactions(request);
    }

    @PostMapping("/nbrTotalAbonnesRfidBips")
    @Operation(summary = "Nombre total d’abonnés BIP PASS et RFID")
    public ResponseEntity<?> nbrTotalAbonnesRfidBip(@RequestBody ReportRequest request) {
        return reportService.nbrTotalAbonnesRfidBip(request);
    }


}
