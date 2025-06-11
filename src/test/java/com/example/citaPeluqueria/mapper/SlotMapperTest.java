package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SlotMapperTest {

    @InjectMocks
    private SlotMapper slotMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_shouldMapSlotEntityToSlotRestDTO() {
        // Arrange
        SlotEntity slot = new SlotEntity();
        slot.setId(1L);
        slot.setSlotStatus(SlotStatus.FREE);
        slot.setStartHour(LocalDateTime.of(2025, 6, 11, 10, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 11, 11, 0));

        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setUsername("Peluquero1");
        slot.setHairdresser(hairdresser);

        ClientEntity client = new ClientEntity();
        client.setUsername("Cliente1");
        slot.setClient(client);

        ServiceEntity service = new ServiceEntity();
        service.setDescription("Corte de pelo");

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setService(service);
        slot.setAppointment(appointment);

        // Act
        SlotRestDTO dto = slotMapper.toDTO(slot);

        // Assert
        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("FREE", dto.getSlotStatus());
        assertEquals("2025-06-11T10:00", dto.getStartHour());
        assertEquals("2025-06-11T11:00", dto.getFinalHour());
        assertEquals("Peluquero1", dto.getHairdresserName());
        assertEquals("Cliente1", dto.getClientName());
        assertEquals("Corte de pelo", dto.getServicePackName());
    }

    @Test
    void toDTO_shouldHandleNullsGracefully() {
        // Arrange
        SlotEntity slot = new SlotEntity();
        slot.setId(2L);
        slot.setSlotStatus(SlotStatus.BLOCKED);
        slot.setStartHour(LocalDateTime.of(2025, 6, 11, 14, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 11, 15, 0));
        // No hairdresser, client or appointment

        // Act
        SlotRestDTO dto = slotMapper.toDTO(slot);

        // Assert
        assertNotNull(dto);
        assertEquals(2L, dto.getId());
        assertEquals("BLOCKED", dto.getSlotStatus());
        assertEquals("2025-06-11T14:00", dto.getStartHour());
        assertEquals("2025-06-11T15:00", dto.getFinalHour());
        assertNull(dto.getHairdresserName());
        assertNull(dto.getClientName());
        assertNull(dto.getServicePackName());
    }

    @Test
    void toEntity_shouldMapDtoToEntityUsingModelMapper() {
        // Arrange
        SlotRestDTO dto = new SlotRestDTO();
        dto.setId(3L);

        SlotEntity expectedEntity = new SlotEntity();
        expectedEntity.setId(3L);

        when(modelMapper.map(dto, SlotEntity.class)).thenReturn(expectedEntity);

        // Act
        SlotEntity result = slotMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
        verify(modelMapper).map(dto, SlotEntity.class);
    }
}