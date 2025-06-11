package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.ServicePackRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentRestMapperTest {

    @InjectMocks
    private AppointmentRestMapper appointmentRestMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @Test
    void toDTO_shouldMapEntityToDto_withServiceDescription() {
        // Arrange
        AppointmentEntity entity = new AppointmentEntity();
        ServiceEntity service = new ServiceEntity();
        service.setDescription("Corte de pelo");
        entity.setService(service);

        AppointmentRestDTO dtoMapped = new AppointmentRestDTO();
        dtoMapped.setId(1L);
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setDescription("Corte de pelo");

        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);
        when(modelMapper.map(entity, AppointmentRestDTO.class)).thenReturn(dtoMapped);
        when(serviceMapper.toDTO(service)).thenReturn(serviceDTO);

        // Act
        AppointmentRestDTO result = appointmentRestMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals("Corte de pelo", result.getDescription());
    }

    @Test
    void toDTO_shouldMapEntityToDto_withoutService() {
        // Arrange
        AppointmentEntity entity = new AppointmentEntity();
        entity.setService(null);

        AppointmentRestDTO dtoMapped = new AppointmentRestDTO();
        dtoMapped.setId(2L);

        when(modelMapper.map(entity, AppointmentRestDTO.class)).thenReturn(dtoMapped);

        // Act
        AppointmentRestDTO result = appointmentRestMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertNull(result.getDescription());
    }

    @Test
    void toDTO_shouldReturnNull_whenInputIsNull() {
        // Act
        AppointmentRestDTO result = appointmentRestMapper.toDTO(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        // Arrange
        AppointmentRestDTO dto = new AppointmentRestDTO();
        dto.setId(3L);
        AppointmentEntity mappedEntity = new AppointmentEntity();
        mappedEntity.setId(3L);

        when(modelMapper.map(dto, AppointmentEntity.class)).thenReturn(mappedEntity);

        // Act
        AppointmentEntity result = appointmentRestMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(3L, result.getId());
    }

    @Test
    void toEntity_shouldReturnNull_whenInputIsNull() {
        // Act
        AppointmentEntity result = appointmentRestMapper.toEntity(null);

        // Assert
        assertNull(result);
    }
}