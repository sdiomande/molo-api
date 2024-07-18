package com.map.moloapi.entities;

import com.map.moloapi.utils.Utilities;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 03/11/2023 10:40
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "libelle", unique = true)
    private String libelle;

    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "boolean default false")
    private Boolean isPartner;
//    @Column(nullable = false)
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return id != null && Objects.equals(id, role.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void cAudit() {
        this.createdAt = Utilities.now();
        this.createdBy = Utilities.userConnectedID();
    }

    public void uAudit() {
        this.updatedAt = Utilities.now();
        this.updatedBy = Utilities.userConnectedID();
    }
}
