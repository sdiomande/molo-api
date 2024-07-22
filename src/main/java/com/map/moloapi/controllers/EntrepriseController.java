package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.entreprises.EntrepriseRequest;
import com.map.moloapi.contracts.entreprises.UpdateEntrepriseRequest;
import com.map.moloapi.services.EntrepriseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 13/02/2024 11:35
 */
@RestController
@RequestMapping("/entreprises")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion des entreprises")
public class EntrepriseController {
    @Autowired
    EntrepriseService entrepriseService;

    @GetMapping
    @Operation(summary = "Liste des entreprises")
    public Response all() {
        return entrepriseService.all();
    }

    @PostMapping()
    @Operation(summary = "Creation d'un entreprise")
    @ResponseStatus(HttpStatus.CREATED)
    private Response create(@RequestBody EntrepriseRequest request) {
        return entrepriseService.create(request);
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Modifier certificat")
    private Response updateCertificat(@ModelAttribute UpdateEntrepriseRequest request) {
        return entrepriseService.updateCertificat(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un entreprise")
    private Response update(@NotNull @PathVariable String id, @NotNull @RequestBody EntrepriseRequest request) {
        return entrepriseService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un entreprise")
    private Response delete(@PathVariable String id) {
        return entrepriseService.delete(id);
    }

    @GetMapping("/activate/{id}")
    @Operation(summary = "Activer ou desactiver un entreprise")
    private Response activate(@PathVariable String id) {
        return entrepriseService.activate(id);
    }
}
