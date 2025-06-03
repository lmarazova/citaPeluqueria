package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<HolidayEntity, Long> {
    Optional<HolidayEntity> findByDate(LocalDate date);
}
