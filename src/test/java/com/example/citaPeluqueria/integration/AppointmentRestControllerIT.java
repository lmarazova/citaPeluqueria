package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.mapper.AppointmentRestMapper;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AppointmentRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    // IMPORTANTE: usar @MockBean para que Spring inyecte un mock en vez del bean real
    @MockBean
    private AppointmentRestMapper appointmentRestMapper;

    private AppointmentEntity appointment;

    @BeforeEach
    void setUp() {
        ClientEntity client = new ClientEntity();
        client.setUsername("clienteTest");
        client.setPassword("dummy");
        client.setEnabled(true);
        client.setPhone("123456789");
        clientRepository.save(client);

        ServiceEntity service = new ServiceEntity();
        service.setDescription("Servicio Test");
        service.setActive(true);
        serviceRepository.save(service);

        appointment = new AppointmentEntity();
        appointment.setUser(client);
        appointment.setService(service);
        appointment.setSelectedHourRange("10:00 - 10:45");
        appointment.setDate("2025-06-03");
        appointmentRepository.save(appointment);

        AppointmentRestDTO dto = new AppointmentRestDTO();
        dto.setId(appointment.getId());
        dto.setClientUsername("clienteTest");
        dto.setDescription("Servicio Test");
        dto.setSelectedHourRange("10:00 - 10:45");
        dto.setDate("2025-06-03");

        // Ahora sí funciona porque appointmentRestMapper es un mock
        when(appointmentRestMapper.toDTO(any(AppointmentEntity.class))).thenReturn(dto);
    }

    @Test
    @WithMockUser(username = "clienteTest", roles = {"USER"})
    void testGetAppointmentById_shouldReturnAppointmentWithFullInfo() throws Exception {
        mockMvc.perform(get("/api/appointments/{id}", appointment.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointment.getId()))
                .andExpect(jsonPath("$.clientUsername").value("clienteTest"))
                .andExpect(jsonPath("$.description").value("Servicio Test"))
                .andExpect(jsonPath("$.selectedHourRange").value("10:00 - 10:45"))
                .andExpect(jsonPath("$.date").value("2025-06-03"));
    }



    @Test
    @WithMockUser(username = "clienteTest", roles = {"USER"})
    void testGetAllAppointments_shouldReturnListOfAppointments() throws Exception {
        mockMvc.perform(get("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                // Busca el objeto que tenga el id que esperamos
                .andExpect(jsonPath("$[?(@.id == %d)].clientUsername", appointment.getId()).value("clienteTest"))
                .andExpect(jsonPath("$[?(@.id == %d)].description", appointment.getId()).value("Servicio Test"));
    }
}