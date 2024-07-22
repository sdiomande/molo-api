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
@Table(name = "entreprises")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Entreprise {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String description;
    @Column(unique = true, nullable = false)
    private String name;
    private String code;
    private String address;
    private String email;
    private String website;
    private String phone1;
    private String phone2;
    private String rccm;
    private String logo;
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
        this.setActive(true);
    }

    public void uAudit() {
        this.setUpdatedAt(Utilities.now());
        this.setUpdatedBy(Utilities.userConnectedID());
    }

    public void activate() {
        if (this.getActive() != null) {
            this.setActive(!this.getActive());
            this.uAudit();
        }
    }
}
