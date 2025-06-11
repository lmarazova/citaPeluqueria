package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServicePackRestDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.mapper.ServicePackRestMapper;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServicePackRestController.class)
@AutoConfigureMockMvc(addFilters = false) // 👈 Mueve esta línea AQUÍ
public class ServicePackRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private ServicePackRestMapper servicePackRestMapper;

    @Test
    void getServicePackById_shouldReturnDto_whenServiceExists() throws Exception {
        Long id = 1L;
        ServiceEntity serviceEntity = new ServiceEntity();
        ServicePackRestDTO dto = new ServicePackRestDTO();
        dto.setId(id); // ejemplo

        when(serviceRepository.findById(id)).thenReturn(Optional.of(serviceEntity));
        when(servicePackRestMapper.toDTO(serviceEntity)).thenReturn(dto);

        mockMvc.perform(get("/api/service-pack/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    @Test
    void getServicePackById_shouldReturn404_whenServiceNotFound() throws Exception {
        Long id = 99L;

        when(serviceRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/service-pack/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllServicePacks_shouldReturnListOfDtos() throws Exception {
        List<ServiceEntity> services = List.of(new ServiceEntity(), new ServiceEntity());
        List<ServicePackRestDTO> dtos = List.of(new ServicePackRestDTO(), new ServicePackRestDTO());

        when(serviceRepository.findAll()).thenReturn(services);
        when(servicePackRestMapper.toDTO(any(ServiceEntity.class)))
                .thenReturn(dtos.get(0), dtos.get(1));

        mockMvc.perform(get("/api/service-pack")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        // nuevamente, puedes añadir jsonPath para comprobar tamaño del array, etc.
    }
}
