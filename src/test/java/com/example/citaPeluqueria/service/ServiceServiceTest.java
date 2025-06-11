package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.dtos.ServiceFullDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.HairServiceService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.ServiceServiceImpl;
import com.example.citaPeluqueria.services.SlotService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HairServiceService hairServiceService;
    @Mock
    private SlotService slotService;
    @Mock
    private HairServiceRepository hairServiceRepository;
    @Mock
    private MultipartFile photo;
    @InjectMocks
    private ServiceServiceImpl serviceService;
    @Mock
    private ServiceService service;
    @BeforeEach
    void setUp() {
        serviceService = new ServiceServiceImpl(objectMapper, serviceRepository);
        ReflectionTestUtils.setField(serviceService, "modelMapper", modelMapper);
        ReflectionTestUtils.setField(serviceService, "hairServiceService", hairServiceService);
        ReflectionTestUtils.setField(serviceService, "slotService", slotService);
    }
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
    @Test
    void savePhoto_shouldSaveFileCorrectly() throws IOException {
        String name = "Pack Prueba";
        MultipartFile photo = mock(MultipartFile.class);
        File expectedFile = new File("C:\\Users\\lyuba\\OneDrive\\Escritorio\\SPRING ADVANCED\\FINAL PROJECT\\citaPeluquería\\src\\main\\resources\\static\\img\\", name + ".png");

        serviceService.savePhoto(photo, name);

        verify(photo).transferTo(eq(expectedFile));
    }
    @Test
    void saveServicePack_shouldSaveMappedEntity_whenUsingEnum() {
        // Datos de entrada
        List<Map<String, String>> selected = List.of(Map.of("label", "Corte"));
        String name = "Pack Corte";
        double price = 30.0;
        int duration = 45;

        // Mock del ServiceEntity que se va a guardar
        ServiceEntity fakeServiceEntity = new ServiceEntity();

        // Cuando modelMapper haga map, devuelvo la fakeServiceEntity
        when(modelMapper.map(any(ServiceFullDTO.class), eq(ServiceEntity.class))).thenReturn(fakeServiceEntity);

        // Cuando hairServiceService busque por label, devuelvo el enum HairService.CUT
        when(hairServiceService.fromLabel("Corte")).thenReturn(HairService.CUT);

        // Ejecutamos el método a testear
        serviceService.saveServicePack(selected, name, price, duration);

        // Capturamos el ServiceEntity que se ha guardado en el repositorio
        ArgumentCaptor<ServiceEntity> captor = ArgumentCaptor.forClass(ServiceEntity.class);
        verify(serviceRepository).save(captor.capture());

        ServiceEntity saved = captor.getValue();

        // Comprobaciones
        assertNotNull(saved.getServices());
        assertEquals(1, saved.getServices().size());
        assertEquals(HairService.CUT, saved.getServices().get(0));  // Aquí la lista es de enums HairService
    }

    @Test
    void saveServicePack_shouldSaveCustomService() {
        List<Map<String, String>> selected = List.of(Map.of("label", "Custom Label"));
        String name = "Pack Custom";
        double price = 50.0;
        int duration = 30;

        ServiceEntity fakeServiceEntity = new ServiceEntity();

        when(modelMapper.map(any(ServiceFullDTO.class), eq(ServiceEntity.class))).thenReturn(fakeServiceEntity);
        // Simulamos que devuelve HairService.CUSTOM para etiqueta personalizada
        when(hairServiceService.fromLabel("Custom Label")).thenReturn(HairService.CUSTOM);

        serviceService.saveServicePack(selected, name, price, duration);

        ArgumentCaptor<ServiceEntity> captor = ArgumentCaptor.forClass(ServiceEntity.class);
        verify(serviceRepository).save(captor.capture());

        ServiceEntity saved = captor.getValue();

        assertNotNull(saved.getServices());
        assertEquals(1, saved.getServices().size());
        assertEquals(HairService.CUSTOM, saved.getServices().get(0));
    }
    @Test
    void savePhoto_shouldThrowIOException_whenTransferFails() throws IOException {
        MultipartFile photo = mock(MultipartFile.class);
        String servicePackName = "TestPhoto";

        doThrow(new IOException("Error al guardar archivo")).when(photo).transferTo(any(File.class));

        IOException thrown = assertThrows(IOException.class, () -> {
            serviceService.savePhoto(photo, servicePackName);
        });

        assertEquals("Error al guardar archivo", thrown.getMessage());
    }

    @Test
    void saveServicePack_shouldHandleMultipleServices() {
        List<Map<String, String>> selected = List.of(
                Map.of("label", "Corte"),
                Map.of("label", "Color")
        );
        String name = "Pack Corte + Color";
        double price = 70.0;
        int duration = 90;

        ServiceEntity fakeServiceEntity = new ServiceEntity();

        when(modelMapper.map(any(ServiceFullDTO.class), eq(ServiceEntity.class))).thenReturn(fakeServiceEntity);
        when(hairServiceService.fromLabel("Corte")).thenReturn(HairService.CUT);
        when(hairServiceService.fromLabel("Color")).thenReturn(HairService.COLOR);

        serviceService.saveServicePack(selected, name, price, duration);

        ArgumentCaptor<ServiceEntity> captor = ArgumentCaptor.forClass(ServiceEntity.class);
        verify(serviceRepository).save(captor.capture());

        ServiceEntity saved = captor.getValue();

        assertNotNull(saved.getServices());
        assertEquals(2, saved.getServices().size());
        assertTrue(saved.getServices().contains(HairService.CUT));
        assertTrue(saved.getServices().contains(HairService.COLOR));
    }

    @Test
    void saveServicePack_shouldIgnoreNullLabels() {
        List<Map<String, String>> selected = new ArrayList<>();

        Map<String, String> map1 = new HashMap<>();
        map1.put("label", "Corte");

        Map<String, String> map2 = new HashMap<>();
        map2.put("label", null); // Valor nulo permitido

        Map<String, String> map3 = new HashMap<>(); // Mapa vacío

        selected.add(map1);
        selected.add(map2);
        selected.add(map3);

        String name = "Pack Parcial";
        double price = 30.0;
        int duration = 45;

        ServiceEntity fakeServiceEntity = new ServiceEntity();
        when(modelMapper.map(any(ServiceFullDTO.class), eq(ServiceEntity.class))).thenReturn(fakeServiceEntity);
        when(hairServiceService.fromLabel("Corte")).thenReturn(HairService.CUT);

        serviceService.saveServicePack(selected, name, price, duration);

        ArgumentCaptor<ServiceEntity> captor = ArgumentCaptor.forClass(ServiceEntity.class);
        verify(serviceRepository).save(captor.capture());

        ServiceEntity saved = captor.getValue();

        assertNotNull(saved.getServices());
        assertEquals(1, saved.getServices().size());
        assertEquals(HairService.CUT, saved.getServices().get(0));
    }




}
