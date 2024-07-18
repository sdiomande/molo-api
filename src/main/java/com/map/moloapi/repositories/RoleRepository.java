package com.map.moloapi.repositories;

import com.map.moloapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByLibelle(String roleName);

    boolean existsByLibelle(String libelle);

    @Query("select (count(r) > 0) from Role r where r.id <> ?1 and r.libelle = ?2")
    boolean libelleNotUpdatable(String id, String libelle);

}