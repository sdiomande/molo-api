package com.map.moloapi.contracts.entreprises;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 16/02/2024 11:14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEntrepriseRequest {
    private String id;
    private MultipartFile file;
}
