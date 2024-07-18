package com.map.moloapi.entities;

import com.map.moloapi.utils.Utilities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-partner-api
 * @Date 02/11/2023 10:36
 */
@Entity
@Table(name = "partners")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partner {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String description;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String partnerCode;
    private String reloadLane;
    private String staffCode;
    private String publicKeyFile;
    private String passageLane;
    //    @Column(nullable = false, columnDefinition = "varchar(20) default '0.0.0.0'")
    private String remoteIp;
    //    @Column(nullable = false)
    private String grantToken;
    @Column(columnDefinition = "boolean default false")
    private Boolean revoke;

    private String scopes;
    @Column(columnDefinition = "varchar(15) default '0'")
    private String firstCustomerId;
    @Column(columnDefinition = "varchar(15) default '0'")
    private String lastCustomerId;

    @Column(columnDefinition = "TEXT")
    private String publicKeyContent;
    private String publicKeyPath;
    @Column(columnDefinition = "boolean default true")
    private Boolean active;

    @Column(nullable = false)
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

    public void cAudit() {
        this.setCreatedAt(Utilities.now());
        this.setCreatedBy(Utilities.userConnectedID());
        this.setUpdatedAt(Utilities.now());
        this.setUpdatedBy(Utilities.userConnectedID());
        this.setRevoke(false);
        this.setActive(true);
    }

    public void uAudit() {
        this.setUpdatedAt(Utilities.now());
        this.setUpdatedBy(Utilities.userConnectedID());
    }

    public String fileContentFiltered() {
        if (this.publicKeyContent != null) {
            return publicKeyContent
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replaceAll("\\n", "")
                    .replace("-----END PUBLIC KEY-----", "");
        }
        return null;
    }

    public void activate() {
        if (this.getActive() != null) {
            this.setActive(!this.getActive());
            this.uAudit();
        }
    }
}
