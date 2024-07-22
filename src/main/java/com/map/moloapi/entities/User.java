package com.map.moloapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.map.moloapi.utils.Utilities;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

/**
 * @author DIOMANDE Souleymane 
 * @project molo-api
 * @Date 03/11/2023 10:37
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "login", unique = true)
    private String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phoneNumber", unique = true)
    private String phoneNumber;

    private String expiredAt;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "updatedAt")
    private String updatedAt;

    @Column(name = "updatedBy")
    private String updatedBy;

    @Column(name = "deletedAt")
    private String deletedAt;

    @Column(name = "deletedBy")
    private String deletedBy;

    @JsonIgnore
    @Column(name = "activatedAt")
    private String activatedAt;

    @Column(name = "activatedBy")
    private String activatedBy;

    @Column(name = "active")
    private boolean active;

    @Column(name = "firstConnexion")
    private Boolean firstConnexion;

    @Column(name = "attempt", columnDefinition = "int default 0")
    private Integer attempt;

    @Column(name = "accountLocked")
    private Boolean locked = false;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "entreprise_id")
    private Entreprise entreprise;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void cAudit() {
        this.setCreatedAt(Utilities.now());
        this.setCreatedBy(Utilities.userConnectedID());
        this.setUpdatedAt(Utilities.now());
        this.setUpdatedBy(Utilities.userConnectedID());
        this.setAttempt(0);
        this.setLocked(false);
        this.setFirstConnexion(true);
        this.setActive(true);
    }

    public void uAudit() {
        this.setUpdatedAt(Utilities.now());
        this.setUpdatedBy(Utilities.userConnectedID());
//        this.setAttempt(0);
//        this.setFirstConnexion(false);
//        this.setLocked(false);
    }

    public void dAudit() {
        this.setDeletedAt(Utilities.now());
        this.setDeletedBy(Utilities.userConnectedID());
        this.setLogin("###".concat(this.getLogin()));
        this.setPhoneNumber("###".concat(this.getPhoneNumber()));
        this.setEmail("###".concat(this.getEmail()));
        uAudit();
    }

    public void activatedAudit() {
        this.setActivatedAt(Utilities.now());
        this.setActivatedBy(Utilities.userConnectedID());
    }

    public boolean passwordExpired() {
        if (this.expiredAt == null){
            return true;
        }
        return !Utilities.after(this.expiredAt);
    }
}
