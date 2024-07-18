package com.map.moloapi.contracts.users.requests;

import com.map.moloapi.annotations.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DIOMANDE Souleymane
 * @Date 08/10/2022 13:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {
    private String phoneNumber;
    private String token;
    private String password;
//    @ValidPassword
    private String newPassword;
//    @ValidPassword
    private String confirmPassword;
}
