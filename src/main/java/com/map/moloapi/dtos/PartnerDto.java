package com.map.moloapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 15/02/2024 11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PartnerDto {
    private String id;
    private String description;
    private String name;
    private String partnerCode;
    private String reloadLane;
    private String staffCode;
    private String publicKeyFile;
    private String passageLane;
    private String remoteIp;
    private String grantToken;
    private boolean revoke;
    private String scopes;
    private String firstCustomerId;
    private String lastCustomerId;
    private Boolean active;
//    private String publicKeyContent;
//    private String publicKeyPath;
//    @JsonIgnore
    private String createdAt;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String updatedAt;
    @JsonIgnore
    private String updatedBy;
}
