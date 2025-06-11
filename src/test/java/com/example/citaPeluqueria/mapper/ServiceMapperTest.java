package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServiceMapperTest {
    @InjectMocks
    private ServiceMapper serviceMapper;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_shouldMapEntityToDto() {
        // Arrange
        ServiceEntity entity = new ServiceEntity();
        ServiceDTO expectedDTO = new ServiceDTO();
        when(modelMapper.map(entity, ServiceDTO.class)).thenReturn(expectedDTO);

        // Act
        ServiceDTO result = serviceMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDTO, result);
        verify(modelMapper).map(entity, ServiceDTO.class);
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        // Arrange
        ServiceDTO dto = new ServiceDTO();
        ServiceEntity expectedEntity = new ServiceEntity();
        when(modelMapper.map(dto, ServiceEntity.class)).thenReturn(expectedEntity);

        // Act
        ServiceEntity result = serviceMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(expectedEntity, result);
        verify(modelMapper).map(dto, ServiceEntity.class);
    }
}