package com.map.moloapi.contracts.users.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.map.moloapi.dtos.UserDto;
import com.map.moloapi.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author DIOMANDE Souleymane
 * @Date 13/01/2023 17:30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthResponse {
    //    private String token;
    private String id;
    private String refreshToken;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String login;
    private Boolean firstConnexion;
    private Boolean locked;
    private Integer attempt;
//    private User user;
    private String sub;
    private String role;
    private Long sessionTimeout;
}
