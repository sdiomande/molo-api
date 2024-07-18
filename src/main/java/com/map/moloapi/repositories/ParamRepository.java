package com.map.moloapi.repositories;


import com.map.moloapi.entities.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
@Repository
public interface ParamRepository extends JpaRepository<Param, String> {
    @Transactional
    @Modifying
    @Query("update Param p set p.valeur = ?1 where p.name = ?2")
    void update(String valeur, String name);

    @Transactional
    @Modifying
    @Query("update Param p set p.description = ?1, p.valeur = ?2 where p.name = ?3")
    void update(String description, String valeur, String name);
    Param findByName(String name);
    List<Param> findByNameIn(Collection<String> names);

    @Query("select p.valeur from Param p where p.type = ?1")
    List<String> findValeursByType(String type);
}