package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    ClientEntity findByUsername(String username);


    boolean existsByEmail(String email);

    ClientEntity findByEmail(String email);

    ClientEntity findByPhone(String phone);


    boolean existsByPhone(String phone);
}
