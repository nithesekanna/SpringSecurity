package com.example.security;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionTokenRepository extends JpaRepository<SessionToken, Long> {
    Optional<SessionToken> findByUsernameAndActive(String username,String status);

	void deleteAllByExpiresAtBefore(LocalDateTime now);
}
