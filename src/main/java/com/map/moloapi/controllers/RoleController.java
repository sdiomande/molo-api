package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.dtos.RoleDto;
import com.map.moloapi.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 16/02/2024 16:51
 */
@RestController
@RequestMapping("/roles")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
//@PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
@Tag(name = "Gestion des roles")
public class RoleController {
    @Autowired
    RoleService roleService;

    @GetMapping()
    @Operation(summary = "Recuperation des roles")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Response all(){
        return roleService.all();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recuperation d'un role")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    public Response findById(@PathVariable String id){
        return roleService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Creation d'un role")
    public Response create(@RequestBody RoleDto request) {
        return roleService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modification d'un role")
    public Response update(@PathVariable String id, @RequestBody RoleDto request) {
        return roleService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Suppression d'un role")
    public Response delete(@PathVariable String id) {
        return roleService.delete(id);
    }
}
