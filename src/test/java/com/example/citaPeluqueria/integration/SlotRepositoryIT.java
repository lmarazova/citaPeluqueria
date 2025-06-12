package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class SlotRepositoryIT {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testGuardarYBuscarSlot() {
        SlotEntity slot = new SlotEntity();
        slot.setStartHour(LocalDateTime.of(2025, 6, 12, 10, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 12, 10, 30));
        slot.setSlotStatus(SlotStatus.FREE);

        slot = slotRepository.save(slot);

        Optional<SlotEntity> encontrado = slotRepository.findById(slot.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getStartHour()).isEqualTo(slot.getStartHour());
    }

    @Test
    void testObtenerTodosLosSlots() {
        SlotEntity slot1 = new SlotEntity();
        slot1.setStartHour(LocalDateTime.of(2025, 6, 12, 10, 0));
        slot1.setFinalHour(LocalDateTime.of(2025, 6, 12, 10, 30));
        slot1.setSlotStatus(SlotStatus.FREE);
        slotRepository.save(slot1);

        List<SlotEntity> slots = slotRepository.findAll();

        assertThat(slots).isNotNull();
        assertThat(slots.size()).isGreaterThan(0);
    }
}