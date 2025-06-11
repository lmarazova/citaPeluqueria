package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.HairServiceService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.ServiceServiceImpl;
import com.example.citaPeluqueria.services.SlotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreateServiceController.class)
@AutoConfigureMockMvc
public class CreateServiceControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private CreateServiceController controller;
    @Mock
    private ObjectMapper objectMapper;
    @MockBean
    private SlotService slotService;
    @MockBean
    private ServiceService serviceService;

    @MockBean
    private HairServiceRepository hairServiceRepository;
    @MockBean
    private ServiceRepository serviceRepository;
    @MockBean
    private HairServiceService hairServiceService;
    @MockBean
    private ModelMapper modelMapper;

    @Mock
    private Model model;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void showCreateService_shouldReturnCreateServiceView() {
        List<HairServiceEntity> mockServices = List.of(new HairServiceEntity());
        when(hairServiceRepository.findAll()).thenReturn(mockServices);

        String viewName = controller.showCreateService("{}", model);

        verify(model).addAttribute("basicServices", mockServices);
        verify(model).addAttribute("serviceJson", "{}");
        assertEquals("create-service", viewName);
    }

    @Test
    void showBlocks_shouldRedirectToCreateService() throws Exception {
        // JSON de ejemplo con bloques
        String json = "[{\"type\": \"start\"}, {\"type\": \"end\"}]";
        List<Map<String, String>> blocks = new ObjectMapper().readValue(json, List.class);

        when(slotService.convertBlockTypesToSlotStatuses(blocks)).thenReturn(new ArrayList<>());

        String result = controller.showBlocks(json);

        assertEquals("redirect:/create-service", result);
    }

    @Test
    void showServices_shouldReturnCreateServiceView() throws Exception {
        String json = "[{\"id\": \"1\", \"label\": \"Corte\"}]";
        List<Map<String, String>> services = new ObjectMapper().readValue(json, List.class);
        when(serviceService.buildLabelFromSelectedServices(any())).thenReturn("Corte");

        String view = controller.showServices(json, model);

        verify(model).addAttribute("packName", "Corte");
        verify(model).addAttribute("serviceJson", json);
        assertEquals("create-service", view);
    }

    @Test
    void addServiceCustom_shouldRedirectToCreateService_whenLabelIsValid() {
        String label = "Nuevo servicio";
        HairServiceEntity entity = new HairServiceEntity();
        when(hairServiceRepository.findAll()).thenReturn(List.of(entity));

        String view = controller.addServiceCustom(label, model);

        verify(hairServiceRepository).save(any());
        verify(model).addAttribute(eq("basicServices"), any());
        assertEquals("redirect:/create-service", view);
    }

    @Test
    void deleteCustomService_shouldReturnOk_whenExistsAndIsCustom() {
        HairServiceEntity custom = new HairServiceEntity();
        custom.setHairService(HairService.CUSTOM);

        when(hairServiceRepository.findById(1L)).thenReturn(Optional.of(custom));

        var response = controller.deleteCustomService(1L);
        assertEquals(200, response.getStatusCodeValue());
        verify(hairServiceRepository).deleteById(1L);
    }

    @Test
    void deleteCustomService_shouldReturnNotFound_whenNotFound() {
        when(hairServiceRepository.findById(999L)).thenReturn(Optional.empty());

        var response = controller.deleteCustomService(999L);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void submitPrice_shouldRedirect() {
        String result = controller.submitPrice("49.99");
        assertEquals("redirect:/create-service", result);
    }

    @Test
    void handlePhotoUpload_shouldReturnCreateServiceView() throws IOException {
        MultipartFile mockPhoto = mock(MultipartFile.class);
        String json = "[{\"id\": \"1\", \"label\": \"Peinado\"}]";
        List<Map<String, String>> services = new ObjectMapper().readValue(json, List.class);

        when(serviceService.buildLabelFromSelectedServices(any())).thenReturn("PaquetePeinado");
        when(hairServiceRepository.findAll()).thenReturn(List.of(new HairServiceEntity()));

        String result = controller.handlePhotoUpload(mockPhoto, json, model);

        verify(model).addAttribute(eq("basicServices"), any());
        verify(model).addAttribute(eq("serviceJson"), eq(json));
        assertEquals("create-service", result);
    }

    @Test
    void submitAll_shouldProcessServicePackAndRenderCreateServiceView() throws Exception {
        // Simular JSON de servicios y bloques
        String serviceJson = "[{\"label\":\"Corte\"}]";
        String blocksJson = "[\"9:00-10:00\"]";
        double price = 25.0;

        // Simular archivo de imagen
        MockMultipartFile photo = new MockMultipartFile(
                "photo", "test.png", "image/png", "fake-image-content".getBytes()
        );

        // Simular HairServiceEntity con enum CUSTOM
        HairServiceEntity mockCustomService = new HairServiceEntity();
        mockCustomService.setHairService(HairService.CUSTOM); // el enum
        mockCustomService.setCustomLabel("Corte especial");

        // Simular HairServiceEntity con enum normal
        HairServiceEntity mockNormalService = new HairServiceEntity();
        mockNormalService.setHairService(HairService.WASH); // enum con label por defecto
        mockNormalService.setCustomLabel(null); // no usado

        List<HairServiceEntity> mockServices = List.of(mockCustomService, mockNormalService);
        Mockito.when(hairServiceRepository.findAll()).thenReturn(mockServices);

        // Ejecutar la petición
        mockMvc.perform(MockMvcRequestBuilders.multipart("/submit-all")
                        .file(photo)
                        .param("serviceJson", serviceJson)
                        .param("blocksJson", blocksJson)
                        .param("price", String.valueOf(price))
                        .with(csrf())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk())
                .andExpect(view().name("create-service"))
                .andExpect(model().attributeExists("basicServices"))
                .andExpect(model().attribute("serviceJson", serviceJson));

        // Verifica que se llama al servicio
        Mockito.verify(serviceService).processServicePack(serviceJson, blocksJson, price, photo);
    }


}
