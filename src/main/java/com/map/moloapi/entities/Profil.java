package com.map.moloapi.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 03/11/2023 12:14
 */
@Entity
@Table(name = "profils")
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Profil implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @ManyToOne
    @JoinColumn(name = "child", referencedColumnName = "child")
    private Menu child;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    private Boolean active;
}
