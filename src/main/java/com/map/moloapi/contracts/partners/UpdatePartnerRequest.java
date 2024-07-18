package com.map.moloapi.contracts.partners;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 16/02/2024 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePartnerRequest {
    private String id;
    private MultipartFile file;
}
