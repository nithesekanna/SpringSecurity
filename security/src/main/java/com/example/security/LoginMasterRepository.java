package com.example.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginMasterRepository extends JpaRepository<LoginMaster, Long>{
	
	Optional<LoginMaster> findByUsername(String name);
}
