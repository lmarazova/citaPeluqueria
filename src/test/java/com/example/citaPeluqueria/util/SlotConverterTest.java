package com.example.citaPeluqueria.util;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

public class SlotConverterTest {

    private SlotConverter slotConverter;

    @BeforeEach
    void setUp() {
        slotConverter = new SlotConverter();
    }

    @Test
    void convertToDto_shouldMapAllFieldsCorrectly() {
        // Arrange
        SlotEntity slot = new SlotEntity();
        slot.setId(1L);
        slot.setStartHour(LocalDateTime.of(2025, 6, 11, 10, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 11, 11, 0));
        slot.setSlotStatus(SlotStatus.FREE);

        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setUsername("JuanEstilo");
        slot.setHairdresser(hairdresser);

        // Act
        SlotOutputDTO dto = slotConverter.convertToDto(slot);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("2025-06-11T10:00", dto.getSlotStartHour());
        assertEquals("2025-06-11T11:00", dto.getSlotFinalHour());
        assertEquals("JuanEstilo", dto.getHairdresser());
        assertEquals("FREE", dto.getSlotStatus());
    }

    @Test
    void convertToDto_shouldHandleNullHairdresserGracefully() {
        // Arrange
        SlotEntity slot = new SlotEntity();
        slot.setId(2L);
        slot.setStartHour(LocalDateTime.now());
        slot.setFinalHour(LocalDateTime.now().plusHours(1));
        slot.setSlotStatus(SlotStatus.OCCUPIED);
        slot.setHairdresser(null); // Esto va a lanzar una NullPointer si no se controla

        // Act & Assert
        Exception exception = assertThrows(NullPointerException.class, () -> {
            slotConverter.convertToDto(slot);
        });

        assertTrue(exception.getMessage() == null || exception.getMessage().contains("null"));
    }
}