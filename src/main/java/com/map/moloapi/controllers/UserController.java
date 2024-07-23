package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.users.requests.CreateUserRequest;
import com.map.moloapi.contracts.users.requests.UpdatePasswordRequest;
import com.map.moloapi.contracts.users.requests.UpdateUserRequest;
import com.map.moloapi.contracts.users.requests.ValidationRequest;
import com.map.moloapi.dtos.UserDto;
import com.map.moloapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import static com.map.moloapi.utils.constants.TechnicalMessage.*;


@RestController
@RequestMapping("/users")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion des utilisateurs")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Ajout d'un utilisateur")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping
    public Response create(@RequestBody CreateUserRequest request){
        return userService.create(request);
    }

    @Operation(summary = "Modification d'un utilisateur")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public Response update(@RequestBody UpdateUserRequest request, @PathVariable String id){
        return userService.update(request, id);
    }

    @Operation(summary = "Liste des utilisateurs")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Response all(){
       return userService.all();
    }

    @Operation(summary = "Liste des utilisateurs bloqués")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/locked")
    public Response locked(){
        return userService.locked();
    }

    @Operation(summary = "Detail utilisateur")
    @GetMapping("/{id}")
    public Response findById(@PathVariable String id){
        return userService.findById(id);
    }

    @Operation(summary = "Activer un utilisateur")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/activate/{id}")
    public Response activate(@PathVariable String id){
        return userService.activate(id);
    }

//    @Operation(summary = "Creer des clients en uploadant le fichier csv")
//    @PreAuthorize("hasRole('SUPER_ADMIN')")
//    @PostMapping("/bulk-customers")
//    public Response bulkCustomers(@RequestParam("file") MultipartFile file){
//        return userService.bulkCreation(file);
//    }

    @Operation(summary = "Modifier son mot de passe")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PostMapping("/update-password")
    public Response updatePassword(@RequestBody @Valid UpdatePasswordRequest request){
        return userService.updatePassword(request);
    }

    @Operation(summary = "Reinitialiser le mot de passe d'un utilisateur")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/reset-password/{id}")
    public Response resetPassword(@PathVariable String id){
        return userService.resetPassword(id);
    }

    @Operation(summary = "Debloquer un compte")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/unlock/{id}")
    public Response unlock(@PathVariable String id){
        return userService.unlock(id);
    }

    @Operation(summary = "Information profile")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @GetMapping("/me")
    public Response me(){
        return userService.me();
    }

    @Operation(summary = "Modifier le profile")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SUPER_ADMIN')")
    @PermitAll()
    @PostMapping("/update-me")
    public Response updateMe(@Valid @RequestBody UpdateUserRequest request){
        return userService.updateMe(request);
    }

    @Operation(summary = "Deconnexion")
    @GetMapping("/logout")
    @PreAuthorize("permitAll()")
    public Response logout(HttpServletRequest request){
        return userService.logout(request);
    }

    @Operation(summary = "Supprimer un utilisateur")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public Response delete(@PathVariable String id){
        return userService.delete(id);
    }

}
