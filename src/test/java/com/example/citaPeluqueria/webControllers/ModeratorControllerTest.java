package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import com.example.citaPeluqueria.services.ClientService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.SlotService;
import com.example.citaPeluqueria.util.SlotConverter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModeratorController.class)

public class ModeratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HairdresserRepository hairdresserRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private ServiceService serviceService;

    @MockBean
    private SlotService slotService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private SlotConverter slotConverter;

    @MockBean
    private AppointmentService appointmentService;
    @WithMockUser(username = "moderator@example.com", roles = "MODERATOR")
    @Test
    void showModeratorPage_shouldReturnModeratorView() throws Exception {
        Principal principal = () -> "moderator@example.com";

        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setEmail("moderator@example.com");
        hairdresser.setSlots(new HashSet<>());

        when(hairdresserRepository.findByEmail("moderator@example.com")).thenReturn(hairdresser);
        when(serviceRepository.findAll()).thenReturn(Collections.emptyList());
        when(slotService.slotOrdering(hairdresser)).thenReturn(Collections.emptyMap());

        mockMvc.perform(get("/moderator").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator"));
    }
    @WithMockUser(username = "moderator@example.com", roles = "MODERATOR")
    @Test
    void refreshHoursForGuest_shouldReturnModeratorViewWithUpdatedSlots() throws Exception {
        Principal principal = () -> "moderator@example.com";

        ClientEntity guest = new ClientEntity();
        when(clientService.findOrCreateGuest("John Doe", "123456789")).thenReturn(guest);
        doNothing().when(appointmentService).prepareHourSelectionView(any(), anyLong(), anyString(), eq(guest));

        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setEmail("moderator@example.com");
        hairdresser.setSlots(new HashSet<>());

        when(hairdresserRepository.findByEmail("moderator@example.com")).thenReturn(hairdresser);
        when(slotService.slotOrdering(hairdresser)).thenReturn(Collections.emptyMap());
        when(serviceRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/moderator/refresh-hours")
                        .param("username", "John Doe")
                        .param("phone", "123456789")
                        .param("packageId", "1")
                        .param("date", "2025-06-12")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("moderator"));
    }
    @Test
    @WithMockUser(username = "moderator@example.com", roles = {"MODERATOR"})
    void createGuestAppointment_shouldRedirectToModerator_whenSuccess() throws Exception {
        Principal principal = () -> "moderator@example.com";
        ClientEntity guest = new ClientEntity();
        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setEmail("moderator@example.com");

        when(clientService.findOrCreateGuest("John Doe", "123456789")).thenReturn(guest);
        when(hairdresserRepository.findByEmail("moderator@example.com")).thenReturn(hairdresser);
        when(appointmentService.createAppointment("10:00-10:30", 1L, "2025-06-12", guest)).thenReturn(true);

        mockMvc.perform(post("/moderator/refresh-hours")
                        .param("username", "John Doe")
                        .param("phone", "123456789")
                        .param("packageId", "1")
                        .param("date", "2025-06-12")
                        .with(csrf())
                        .param("selectedHourRange", "10:00-10:30")
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/moderator"));
    }

    @Test
    @WithMockUser(username = "moderator@example.com", roles = {"MODERATOR"})
    void createGuestAppointment_shouldReturnErrorView_whenSlotNotFound() throws Exception {
        Principal principal = () -> "moderator@example.com";
        ClientEntity guest = new ClientEntity();
        HairdresserEntity hairdresser = new HairdresserEntity();
        hairdresser.setEmail("moderator@example.com");

        when(clientService.findOrCreateGuest("John Doe", "123456789")).thenReturn(guest);
        when(hairdresserRepository.findByEmail("moderator@example.com")).thenReturn(hairdresser);
        when(appointmentService.createAppointment("10:00-10:30", 1L, "2025-06-12", guest)).thenReturn(false);

        mockMvc.perform(post("/moderator/refresh-hours")
                        .param("username", "John Doe")
                        .param("phone", "123456789")
                        .param("packageId", "1")
                        .param("date", "2025-06-12")
                        .param("selectedHourRange", "10:00-10:30")
                        .with(csrf())
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = "moderator", roles = {"MODERATOR"})
    void checkClient_shouldReturnUsername_whenClientExists() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setUsername("John Doe");
        when(clientService.findByPhone("123456789")).thenReturn(client);

        mockMvc.perform(get("/moderator/check-client")
                        .param("phone", "123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("John Doe"));
    }

    @Test
    void checkClient_shouldReturnNotFound_whenClientDoesNotExist() throws Exception {
        when(clientService.findByPhone("123456789")).thenReturn(null);

        mockMvc.perform(get("/moderator/check-client")
                        .with(user("moderator").roles("MODERATOR"))
                        .param("phone", "123456789"))
                .andExpect(status().isNotFound());
    }
}

