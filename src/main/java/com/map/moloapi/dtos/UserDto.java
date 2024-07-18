package com.map.moloapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String login;
    @JsonIgnore
    private String password;
    private String email;
    private String phoneNumber;
    @JsonIgnore
    private String createdAt;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String updatedAt;
    @JsonIgnore
    private String updatedBy;
    @JsonIgnore
    private String deletedAt;
    @JsonIgnore
    private String deletedBy;
    @JsonIgnore
    private String activatedAt;
    private String activatedBy;
    private boolean active;
    private boolean firstConnexion;
    private RoleDto role;
    private PartnerDto partner;
    private Integer attempt;
    private boolean locked;
    private List<MenuDto> menus;
}
