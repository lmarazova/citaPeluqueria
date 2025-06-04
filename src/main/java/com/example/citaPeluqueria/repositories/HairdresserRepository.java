package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface HairdresserRepository extends JpaRepository<HairdresserEntity, Long> {
    boolean existsByEmail(String email);

    HairdresserEntity findByEmail(String email);

    HairdresserEntity findByUsername(String name);


}
