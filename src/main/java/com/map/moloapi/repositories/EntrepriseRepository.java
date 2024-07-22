package com.map.moloapi.repositories;

import com.map.moloapi.entities.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntrepriseRepository extends JpaRepository<Entreprise, String> {

    String ordered = " order by u.updatedAt desc";


    @Query("select u from Entreprise u" + ordered)
    @Override
    List<Entreprise> findAll();

    boolean existsByName(String name);
    boolean existsByWebsite(String website);
    boolean existsByPhone1(String phone1);
    boolean existsByEmail(String email);
    boolean existsByRccm(String rccm);

    @Query("select (count(r) > 0) from Entreprise r where r.id <> ?1 and r.name = ?2")
    boolean nameNotUpdatable(String id, String name);
}