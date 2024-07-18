package com.map.moloapi.entities;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 03/11/2023 12:13
 */
@Entity
@Table(name = "menus")
@Getter
@Setter
@ToString
//@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Menu implements Serializable {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false)
    private String id;
    private String title;
    private String parent;
    private String child;
    private String icon;
    private Boolean hasChild;
    private String url;
    private Integer level;
    private Boolean active;

}
