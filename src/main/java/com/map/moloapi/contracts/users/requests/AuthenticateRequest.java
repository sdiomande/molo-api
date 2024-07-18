package com.map.moloapi.contracts.users.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotEmpty;

/**
 * @author DIOMANDE Souleymane
 * @Date 08/10/2022 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticateRequest {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
}
