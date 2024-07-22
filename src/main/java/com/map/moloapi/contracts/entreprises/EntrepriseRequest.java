package com.map.moloapi.contracts.entreprises;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 13/02/2024 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntrepriseRequest {
    @NotBlank(message = "Le nom est requis")
    private String name;
    private String description;
//    private String code;
    private String address;
    @NotBlank(message = "L'email est requis")
    @Email
    private String email;
//    @NotBlank(message = "L est requis")
    private String website;
    @NotBlank(message = "Le numero de telephone est requis")
    private String phone1;
    private String phone2;
    @NotBlank(message = "Le rccm est requis")
    private String rccm;
}
