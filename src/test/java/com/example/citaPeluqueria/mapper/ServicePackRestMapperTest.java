package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServicePackRestDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ServicePackRestMapperTest { @Mock
private ModelMapper modelMapper;

    @InjectMocks
    private ServicePackRestMapper servicePackRestMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void toDTO_shouldMapEntityToDTO() {
        ServiceEntity serviceEntity = new ServiceEntity();
        ServicePackRestDTO expectedDTO = new ServicePackRestDTO();

        when(modelMapper.map(serviceEntity, ServicePackRestDTO.class)).thenReturn(expectedDTO);

        ServicePackRestDTO result = servicePackRestMapper.toDTO(serviceEntity);

        verify(modelMapper).map(serviceEntity, ServicePackRestDTO.class);
        assertSame(expectedDTO, result);
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        ServicePackRestDTO dto = new ServicePackRestDTO();
        ServiceEntity expectedEntity = new ServiceEntity();

        when(modelMapper.map(dto, ServiceEntity.class)).thenReturn(expectedEntity);

        ServiceEntity result = servicePackRestMapper.toEntity(dto);

        verify(modelMapper).map(dto, ServiceEntity.class);
        assertSame(expectedEntity, result);
    }
}