package com.map.moloapi.controllers;

import com.map.moloapi.contracts.Response;
import com.map.moloapi.contracts.users.requests.AuthenticateRequest;
import com.map.moloapi.contracts.users.requests.RefreshTokenRequest;
import com.map.moloapi.contracts.users.requests.ResetPasswordRequest;
import com.map.moloapi.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
//@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Gestion de l'authentification")
public class AuthController {

    @Autowired
    AuthService authService;

    @Operation(summary = "Connexion")
    @PostMapping("/login")
    public Response authenticateUser(@Valid @RequestBody AuthenticateRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Rafraichir le token")
    @PostMapping("/refreshtoken")
    public Response refreshtoken(@Valid @RequestBody RefreshTokenRequest request) {
        return  authService.refreshToken(request);
    }

    @Operation(summary = "Reinitialiser le mot de passe")
    @PostMapping("/reset-password")
    public Response resetPassword(@NotNull @RequestBody ResetPasswordRequest request){
        return  authService.resetPassword(request);
    }

    @Operation(summary = "Verification du token, changement du mot de passe")
    @GetMapping("/verify-reset-password-token/{token}")
    public Response verifyResetPasswordToken(@PathVariable String token){
        return  authService.verifyResetPasswordToken(token);
    }
}
