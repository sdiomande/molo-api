package com.map.moloapi.repositories;

import com.map.moloapi.entities.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, String> {
    PasswordResetToken findByToken(String token);

    @Query("select p from PasswordResetToken p where p.user.id = ?1 and p.expiryDateTimestamp > ?2")
    PasswordResetToken findActiveTokenByUserId(String userId, Long now);

    @Query("select p from PasswordResetToken p where p.token = ?1 and p.expiryDateTimestamp > ?2")
    PasswordResetToken verifyToken(String token, Long now);



}