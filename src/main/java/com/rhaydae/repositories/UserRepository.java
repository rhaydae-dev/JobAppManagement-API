package com.rhaydae.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rhaydae.entities.User;
import com.rhaydae.enums.Role;

import jakarta.validation.constraints.NotBlank;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByEmail(String email);
	
	Boolean existsByEmail(String email);
	
	boolean existsByRole(Role role);

	Optional<User> findByActivationToken(String activationToken);
}
