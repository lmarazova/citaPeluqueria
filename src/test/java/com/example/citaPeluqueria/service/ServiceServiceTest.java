package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.ServiceServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ServiceServiceImpl serviceService;
    @Test
    void getById_shouldReturnEntity_whenExists() {
        Long id = 1L;
        ServiceEntity mockEntity = new ServiceEntity();
        when(serviceRepository.findById(id)).thenReturn(Optional.of(mockEntity));

        ServiceEntity result = serviceService.getById(id);

        assertNotNull(result);
        assertEquals(mockEntity, result);
    }
    @Test
    void getById_shouldReturnNull_whenNotExists() {
        Long id = 99L;
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        ServiceEntity result = serviceService.getById(id);

        assertNull(result);
    }

    // Test para buildLabelFromSelectedServices
    @Test
    void buildLabelFromSelectedServices_shouldJoinLabels() {
        List<Map<String, String>> selected = List.of(
                Map.of("label", "Corte"),
                Map.of("label", "Color")
        );

        String result = serviceService.buildLabelFromSelectedServices(selected);

        assertEquals("Corte + Color", result);
    }

    @Test
    void buildLabelFromSelectedServices_shouldReturnEmptyString_whenListIsEmpty() {
        List<Map<String, String>> selected = List.of();

        String result = serviceService.buildLabelFromSelectedServices(selected);

        assertEquals("", result);
    }

    // Test para updateService
    @Test
    void updateService_shouldUpdateFieldsAndSave_whenServiceExists() {
        Long id = 1L;
        double price = 25.0;
        boolean isActive = true;

        ServiceEntity entity = new ServiceEntity();
        when(serviceRepository.findById(id)).thenReturn(Optional.of(entity));

        serviceService.updateService(id, price, isActive);

        assertEquals(price, entity.getPrice());
        assertEquals(isActive, entity.isActive());
        verify(serviceRepository).save(entity);
    }

    @Test
    void updateService_shouldDoNothing_whenServiceNotFound() {
        Long id = 99L;
        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        serviceService.updateService(id, 40.0, false);

        verify(serviceRepository, never()).save(any());
    }
}
