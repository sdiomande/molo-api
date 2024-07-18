package com.map.moloapi.repositories;

import com.map.moloapi.entities.RefreshToken;
import com.map.moloapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

    @Transactional
    @Modifying
    @Query("delete from RefreshToken r where r.user.id = ?1")
    int deleteByUserId(String id);

}