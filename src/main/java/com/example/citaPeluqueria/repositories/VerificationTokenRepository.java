package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    void deleteByClientId(Long id);
}
