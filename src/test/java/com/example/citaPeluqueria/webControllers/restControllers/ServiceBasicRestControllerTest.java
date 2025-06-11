package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServiceBasicRestDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.mapper.ServiceBasicRestMapper;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServiceBasicRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class ServiceBasicRestControllerTest {

        @MockBean
        private HairServiceRepository hairServiceRepository;

        @MockBean
        private ServiceBasicRestMapper serviceBasicRestMapper;

        @Autowired
        private MockMvc mockMvc;

        // No necesitas @BeforeEach ni crear el controlador manualmente ni MockMvcBuilders

    @Test
    void getBasicServiceById_whenExists_shouldReturnServiceDTO() throws Exception {
        Long id = 1L;
        HairServiceEntity entity = new HairServiceEntity();
        ReflectionTestUtils.setField(entity, "id", id);
        entity.setCustomLabel("Corte personalizado");
        entity.setHairService(HairService.CUT);

        ServiceBasicRestDTO dto = new ServiceBasicRestDTO();
        dto.setId(id);
        dto.setServiceName("Corte personalizado");

        when(hairServiceRepository.findById(id)).thenReturn(Optional.of(entity));
        when(serviceBasicRestMapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/api/services-basic/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.serviceName").value("Corte personalizado"));
    }
    @Test
    void getAllBasicServices_shouldReturnListOfServices() throws Exception {
        HairServiceEntity e1 = new HairServiceEntity();
        ReflectionTestUtils.setField(e1, "id", 1L);
        e1.setCustomLabel("Corte");
        e1.setHairService(HairService.CUT);

        HairServiceEntity e2 = new HairServiceEntity();
        ReflectionTestUtils.setField(e2, "id", 2L);
        e2.setCustomLabel("Color");
        e2.setHairService(HairService.COLOR);

        when(hairServiceRepository.findAll()).thenReturn(List.of(e1, e2));

        ServiceBasicRestDTO dto1 = new ServiceBasicRestDTO();
        dto1.setId(1L);
        dto1.setServiceName("Corte");

        ServiceBasicRestDTO dto2 = new ServiceBasicRestDTO();
        dto2.setId(2L);
        dto2.setServiceName("Color");

        when(serviceBasicRestMapper.toDTO(e1)).thenReturn(dto1);
        when(serviceBasicRestMapper.toDTO(e2)).thenReturn(dto2);

        mockMvc.perform(get("/api/services-basic")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].serviceName").value("Corte"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].serviceName").value("Color"));
    }

}
