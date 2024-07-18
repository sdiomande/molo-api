package com.map.moloapi.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

/**
 * @author DIOMANDE Souleymane
 * @Date 04/10/2022 19:34
 */
@Entity
@Table(name = "params")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Param {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    @Column(unique = true)
    private String name;
    @Column(columnDefinition = "TEXT")
    private String valeur;
    private String type;
    private String libelle;
    private String description;
}
