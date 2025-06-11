package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.mapper.AppointmentRestMapper;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentRestController.class)public class AppointmentRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private AppointmentRestMapper appointmentRestMapper;

    @Test
    void testGetAppointmentById_ReturnsAppointment() throws Exception {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setDate("2025-06-11");
        appointment.setSelectedHourRange("10:00-11:00");

        ClientEntity user = new ClientEntity();
        user.setUsername("johndoe");

        ServiceEntity service = new ServiceEntity();
        service.setDescription("Corte de pelo");

        appointment.setUser(user);
        appointment.setService(service);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(appointmentRestMapper.toDTO(any())).thenReturn(
                new AppointmentRestDTO(1L, "johndoe", "Corte de pelo", "10:00-11:00", "2025-06-11")
        );

        mockMvc.perform(get("/api/appointments/1").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientUsername").value("johndoe"))  // <-- Aquí debe ser clientUsername
                .andExpect(jsonPath("$.description").value("Corte de pelo"))
                .andExpect(jsonPath("$.selectedHourRange").value("10:00-11:00"))
                .andExpect(jsonPath("$.date").value("2025-06-11"));
    }

    @Test
    void getAppointmentById_shouldReturn404IfNotFound() throws Exception {
        Long id = 999L;

        when(appointmentRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/appointments/{id}", id).with(user("admin"))) // <-- Aquí el user
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllAppointments_shouldReturnListOfAppointments() throws Exception {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1L);
        appointment.setDate("2025-06-11");
        ClientEntity user = new ClientEntity();
        user.setUsername("jose");
        ServiceEntity service = new ServiceEntity();
        service.setDescription("Peinado");
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setSelectedHourRange("15:00 - 16:00");

        when(appointmentRepository.findAll()).thenReturn(List.of(appointment));

        mockMvc.perform(get("/api/appointments").with(user("admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].clientUsername").value("jose")) // corregido
                .andExpect(jsonPath("$[0].description").value("Peinado")) // corregido
                .andExpect(jsonPath("$[0].selectedHourRange").value("15:00 - 16:00"))
                .andExpect(jsonPath("$[0].date").value("2025-06-11"));
    }
}
