package com.example.security;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;

@Component
public class TokenCleanupScheduler {
	
	@Autowired
    private SessionTokenRepository repo;
	
	@Scheduled(fixedRate = 500000)
	@Transactional
	public void cleanupExpiredTokens() {
        repo.deleteAllByExpiresAtBefore(LocalDateTime.now());
        System.out.println("cleanup Expired Tokens is called");
    }
}
