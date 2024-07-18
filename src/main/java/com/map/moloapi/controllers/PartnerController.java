package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.partners.PartnerRequest;
import com.map.moloapi.contracts.partners.UpdatePartnerRequest;
import com.map.moloapi.services.PartnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 13/02/2024 11:35
 */
@RestController
@RequestMapping("/partners")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion des partenaires")
public class PartnerController {
    @Autowired
    PartnerService partnerService;

    @GetMapping
    @Operation(summary = "Liste des partenaires")
    public Response all() {
        return partnerService.all();
    }

    @PostMapping()
    @Operation(summary = "Creation d'un partenaire")
    private Response create(@RequestBody PartnerRequest request) {
        return partnerService.create(request);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modifier certificat")
    private Response updateCertificat(@ModelAttribute UpdatePartnerRequest request) {
        return partnerService.updateCertificat(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un partenaire")
    private Response update(@NotNull @PathVariable String id, @NotNull @RequestBody PartnerRequest request) {
        return partnerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un partenaire")
    private Response delete(@PathVariable String id) {
        return partnerService.delete(id);
    }

    @GetMapping("/activate/{id}")
    @Operation(summary = "Activer ou desactiver un partenaire")
    private Response activate(@PathVariable String id) {
        return partnerService.activate(id);
    }
}
