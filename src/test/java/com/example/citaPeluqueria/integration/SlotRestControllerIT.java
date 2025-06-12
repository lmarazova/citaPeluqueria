package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
public class SlotRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private HairdresserRepository hairdresserRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    private SlotEntity slot;

    @BeforeEach
    void setUp() {
        // Crear peluquero
        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setUsername("Peluquero1");
        hairdresser.setAdmin(false);
        hairdresser.setSlots(new HashSet<>());
        hairdresser = hairdresserRepository.save(hairdresser);

        // Crear cliente
        ClientEntity client = new ClientEntity();
        client.setUsername("Cliente1");
        client.setPhone("123456789");
        client.setGuest(false);
        client.setEnabled(true);
        client.setComments("Ninguno");
        client.setSlots(new HashSet<>());
        client.setAppointments(new HashSet<>());
        client = clientRepository.save(client);

        // Crear servicio
        ServiceEntity service = new ServiceEntity();
        service.setDescription("Corte de pelo");
        service.setActive(true);
        service.setPrice(20.0);
        service = serviceRepository.save(service);

        // Crear cita
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(client);
        appointment.setService(service);
        appointment = appointmentRepository.save(appointment);

        // Crear slot
        slot = new SlotEntity();
        slot.setStartHour(LocalDateTime.of(2025, 6, 11, 10, 0));
        slot.setFinalHour(LocalDateTime.of(2025, 6, 11, 11, 0));
        slot.setSlotStatus(SlotStatus.FREE);
        slot.setHairdresser(hairdresser);
        slot.setClient(client);
        slot.setService(service);
        slot.setAppointment(appointment);

        slot = slotRepository.save(slot);
    }

    @Test
    void testGetSlotById_shouldReturnSlotWithFullInfo() throws Exception {
        mockMvc.perform(get("/api/slots/{id}", slot.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(slot.getId()))
                .andExpect(jsonPath("$.hairdresserName").value("Peluquero1"))
                .andExpect(jsonPath("$.clientName").value("Cliente1"))
                .andExpect(jsonPath("$.servicePackName").value("Corte de pelo"))
                .andExpect(jsonPath("$.slotStatus").value("FREE"));
    }

    @Test
    void testGetAllSlots_shouldReturnListOfSlots() throws Exception {
        mockMvc.perform(get("/api/slots/{id}", slot.getId())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isOk());
    }
}
