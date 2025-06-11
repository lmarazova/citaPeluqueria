package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import com.example.citaPeluqueria.services.SlotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.lang.reflect.Field;
import java.security.Principal;
import java.util.List;
@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    private MockMvc mockMvc;
    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private SlotService slotService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private SlotRepository slotRepository;

    private HomeController homeController;

    @BeforeEach
    void setUp() throws Exception {
        // Solo inyectamos lo que va por constructor
        homeController = new HomeController(serviceRepository, appointmentRepository, slotRepository);

        // Pero como clientRepository es @Autowired por campo, lo forzamos con reflexión
        Field field = HomeController.class.getDeclaredField("clientRepository");
        field.setAccessible(true);
        field.set(homeController, clientRepository);

        // Igual para slotService si fuera necesario
        Field slotServiceField = HomeController.class.getDeclaredField("slotService");
        slotServiceField.setAccessible(true);
        slotServiceField.set(homeController, slotService);

        Field appointmentServiceField = HomeController.class.getDeclaredField("appointmentService");
        appointmentServiceField.setAccessible(true);
        appointmentServiceField.set(homeController, appointmentService);

        mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();

    }

    @Test
    void home_shouldReturnHomeViewWithModelAttributes() {
        Principal principal = () -> "test@example.com";

        ClientEntity mockUser = new ClientEntity();
        when(clientRepository.findByEmail("test@example.com")).thenReturn(mockUser);

        List<ServiceEntity> mockServices = List.of(new ServiceEntity());
        List<AppointmentEntity> mockAppointments = List.of(new AppointmentEntity());

        when(serviceRepository.findAll()).thenReturn(mockServices);
        when(appointmentRepository.findByUser(mockUser)).thenReturn(mockAppointments);

        Model model = new ExtendedModelMap();

        String viewName = homeController.home(model, principal);

        assertEquals("home", viewName);
        assertEquals(mockUser, model.getAttribute("user"));
        assertEquals(mockServices, model.getAttribute("packages"));
        assertEquals(mockAppointments, model.getAttribute("appointments"));
    }


    @Test
    void deleteAppointment_shouldCallServiceAndRedirect() throws Exception {
        Principal principal = () -> "user@example.com";
        Long appointmentId = 123L;

        // Solo verificamos llamada y redirect, no necesitamos devolver nada
        doNothing().when(appointmentService).deleteClientAppointment(appointmentId, principal);

        mockMvc.perform(post("/home/delete/{id}", appointmentId).principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(appointmentService, times(1)).deleteClientAppointment(appointmentId, principal);
    }

}