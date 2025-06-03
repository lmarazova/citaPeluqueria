package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, Long> {
    List<AppointmentEntity> findByUser(UserEntity user);

    Optional<AppointmentEntity> findByUserIdAndDateAndSelectedHourRange(Long userId, String date, String selectedHourRange);
}
