package com.example.citaPeluqueria.repositories;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<SlotEntity, Long> {
    List<SlotEntity> findAvailableSlotsByStartHourBetweenAndService(LocalDateTime startDate, LocalDateTime endDate, ServiceEntity service);



    List<SlotEntity> findByStartHourBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);


    @Query("SELECT s FROM SlotEntity s WHERE FUNCTION('DATE', s.startHour) = :date ORDER BY s.startHour ASC")
    List<SlotEntity> findAllByStartHourDateOrderByStartHourAsc(@Param("date") LocalDate date);


    List<SlotEntity> findByClientAndService(UserEntity user, ServiceEntity service);

    void deleteByStartHourBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
