package com.map.moloapi.repositories;

import com.map.moloapi.entities.Menu;
import com.map.moloapi.entities.Profil;
import com.map.moloapi.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ProfilRepository extends JpaRepository<Profil, String> {
    List<Profil> findByRole(Role role);

    List<Profil> findByChild_ChildIn(Collection<String> children);
    List<Profil> findByChildIn(Collection<Menu> children);

}