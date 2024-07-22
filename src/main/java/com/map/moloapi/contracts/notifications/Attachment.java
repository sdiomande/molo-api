package com.map.moloapi.contracts.notifications;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 20/02/2024 12:59
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    private String name;
    private File file;
}
