package com.map.moloapi.repositories;

import com.map.moloapi.entities.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, String> {

    String ordered = " order by u.updatedAt desc";


    @Query("select u from Partner u" + ordered)
    @Override
    List<Partner> findAll();

    boolean existsByName(String name);

    @Query("select (count(r) > 0) from Partner r where r.id <> ?1 and r.name = ?2")
    boolean nameNotUpdatable(String id, String name);
}