package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServiceBasicRestDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ServiceBasicRestMapperTest {
    @InjectMocks
    private ServiceBasicRestMapper serviceBasicRestMapper;

    @Mock
    private ModelMapper modelMapper;

    // No usamos serviceRepository en los métodos que vamos a testear, así que no mockeamos aquí.

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_shouldUseCustomLabel_whenServiceIsCustom() {
        // Arrange
        HairServiceEntity entity = new HairServiceEntity();
        entity.setHairService(HairService.CUSTOM);
        entity.setCustomLabel("Etiqueta Personalizada");

        ServiceBasicRestDTO dtoFromMapper = new ServiceBasicRestDTO();
        // Simulamos que modelMapper mapea algo básico (puedes ajustar si hay campos)
        when(modelMapper.map(entity, ServiceBasicRestDTO.class)).thenReturn(dtoFromMapper);

        // Act
        ServiceBasicRestDTO result = serviceBasicRestMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals("Etiqueta Personalizada", result.getServiceName());
    }

    @Test
    void toDTO_shouldUseEnumName_whenServiceIsNotCustom() {
        // Arrange
        HairServiceEntity entity = new HairServiceEntity();
        entity.setHairService(HairService.CUT); // Suponiendo que BASIC es otro valor válido

        ServiceBasicRestDTO dtoFromMapper = new ServiceBasicRestDTO();
        when(modelMapper.map(entity, ServiceBasicRestDTO.class)).thenReturn(dtoFromMapper);

        // Act
        ServiceBasicRestDTO result = serviceBasicRestMapper.toDTO(entity);

        // Assert
        assertNotNull(result);
        assertEquals(String.valueOf(HairService.CUT), result.getServiceName());
    }

    @Test
    void toEntity_shouldMapDtoToEntity() {
        // Arrange
        ServiceBasicRestDTO dto = new ServiceBasicRestDTO();
        HairServiceEntity entityMapped = new HairServiceEntity();
        when(modelMapper.map(dto, HairServiceEntity.class)).thenReturn(entityMapped);

        // Act
        HairServiceEntity result = serviceBasicRestMapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(entityMapped, result);
    }
}

