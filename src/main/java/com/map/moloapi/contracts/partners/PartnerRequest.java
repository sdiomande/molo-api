package com.map.moloapi.contracts.partners;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 13/02/2024 11:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PartnerRequest {
    private String id;
//    @NotBlank
    private String name;
    private String passageLane;
    private String reloadLane;
    private String remoteIp;
    private String staffCode;
//    private String publicKeyFile;
//    @NotBlank
    private Long firstCustomerId;
//    @NotBlank
    private Long lastCustomerId;
//    private MultipartFile file;
}
