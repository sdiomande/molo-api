package com.map.moloapi.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 15/02/2024 11:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntrepriseDto {
    private String id;
    private String name;
    private String description;
    private String code;
    private String address;
    private String email;
    private String website;
    private String phone1;
    private String phone2;
    private String rccm;
    private String logo;
    private Boolean active;
//    @JsonIgnore
    private String createdAt;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String updatedAt;
    @JsonIgnore
    private String updatedBy;
}
