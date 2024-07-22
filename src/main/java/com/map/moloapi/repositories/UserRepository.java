package com.map.moloapi.repositories;

import com.map.moloapi.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    String softDelete = " u.deletedAt is null";
    String ordered = " order by u.updatedAt desc";
    String filter = softDelete + ordered;

    @Query("select u from User u where" + filter)
    @Override
    List<User> findAll();

    User findByLogin(String login);
    Boolean existsByLogin(String login);
    Boolean existsByPhoneNumber(String phoneNumber);
    Boolean existsByEmail(String email);

    @Query("select u from User u where u.locked = true and"+ filter)
    List<User> findLocked();

    @Query("select (count(u) > 0) from User u where u.role.id = ?1")
    boolean existsByRoleId(String id);

    @Query("select (count(u) > 0) from User u where u.id <> ?1 and u.login = ?2")
    boolean loginNotUpdatable(String id, String login);

    @Query("select (count(u) > 0) from User u where u.id <> ?1 and u.phoneNumber = ?2")
    boolean phoneNumberNotUpdatable(String id, String phoneNumber);

    @Query("select (count(u) > 0) from User u where u.id <> ?1 and u.email = ?2")
    boolean emailNotUpdatable(String id, String email);

    @Query("select u from User u where u.entreprise.id = ?1 and" + filter)
    List<User> findByPartnerId(String id);

    @Transactional
    @Modifying
    @Query("update User u set u.firstConnexion = ?1 where u.id = ?2")
    int updateFirstConnection(Boolean firstConnexion, String id);

}