package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
/**
 * Servicio encargado de gestionar franjas horarias disponibles para reservas.
 */
public interface SlotService {
    List<SlotEntity> getAvailableSlots(LocalDate localDate, Long packageId);

    List<List<SlotEntity>> getAvailableSlotCombinations(LocalDate localDate, Long packageId);

    int calculateTotalSlots(int totalDuration);


    List<List<SlotEntity>> getSlotCombinationsForDate(String date);
    List<SlotEntity> getFirstAvailableSlotCombinationForHour(LocalDate localDate, Long packageId, String selectedHourRange);


    Map<LocalDate, List<SlotEntity>> slotOrdering(HairdresserEntity hairdresser);

    List<SlotStatus>convertBlockTypesToSlotStatuses(List<Map<String, String>> blocks);


    /**
     * Elimina las franjas horarias que coincidan con días festivos.
     */
    @Transactional
    void deleteSlotsInHolidays();
}
