package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.ServiceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ServicePacksController.class)
@AutoConfigureMockMvc
class ServicePacksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private ServiceService serviceService;
    private ServiceEntity createServiceEntity(Long id, double price, boolean active) {
        ServiceEntity service = new ServiceEntity();
        service.setId(id);
        service.setPrice(price);
        service.isActive();

        return service;
    }

    @Test
    @WithMockUser
    void showAllService_shouldReturnServicePacksViewWithPackages() throws Exception {
        List<ServiceEntity> mockServices = List.of(
                createServiceEntity(1L, 15.0, true),
                createServiceEntity(2L, 25.0, false)
        );

        when(serviceRepository.findAll()).thenReturn(mockServices);

        mockMvc.perform(get("/service-packs"))
                .andExpect(status().isOk())
                .andExpect(view().name("service-packs"))
                .andExpect(model().attribute("packages", mockServices));
    }

    @Test
    @WithMockUser
    void updateService_shouldCallServiceWithActiveTrueAndRedirect() throws Exception {
        mockMvc.perform(post("/services/update")
                        .param("id", "1")
                        .param("price", "19.99")
                        .param("active", "true")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-packs"));

        verify(serviceService).updateService(1L, 19.99, true);
    }

    @Test
    @WithMockUser
    void updateService_shouldCallServiceWithActiveFalseWhenCheckboxNotChecked() throws Exception {
        mockMvc.perform(post("/services/update")
                        .param("id", "2")
                        .param("price", "10.00")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/service-packs"));

        verify(serviceService).updateService(2L, 10.00, false);
    }
}
