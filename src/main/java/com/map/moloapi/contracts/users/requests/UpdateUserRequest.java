package com.map.moloapi.contracts.users.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author DIOMANDE Souleymane
 * @Date 08/10/2022 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {
    @NotBlank(message = "Le prenom doit etre renseigné")
    private String firstName;
    @NotBlank(message = "Le nom doit etre renseigné")
    private String lastName;
    @NotBlank(message = "Le login doit etre renseigné")
    private String login;
    @Email(message = "L'adresse mail n'est pas valide")
    private String email;
    @Size(min = 8)
    @NotBlank(message = "Le numero de telephone doit etre renseigné")
    private String phoneNumber;
}
