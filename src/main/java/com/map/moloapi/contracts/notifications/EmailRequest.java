package com.map.moloapi.contracts.notifications;

import com.map.moloapi.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 20/02/2024 12:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailRequest {
    private String[] to;
    private String from;
    private String reference;
    private String msgBody;
    private String subject;
    private List<Attachment> attachments;
    private User user;
//    private PaiementDto paiementDto;
    private boolean isValidated;
    private int count;
    private String login;
    private String password;
    private String template;
    private String ttl;
    private String resetUrl;
    private String fullname;
    private String footer;
//    private BackupInfo backupInfo;


    public String getFullname() {
        if (user != null) {
            return user.getFirstName() + " "+ user.getLastName();
        }
        return "";
    }
}
