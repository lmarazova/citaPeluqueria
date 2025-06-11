package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.mapper.AppointmentMapper;
import com.example.citaPeluqueria.mapper.ClientMapper;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ClientRestControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientRestController clientRestController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        clientRestController = new ClientRestController(clientRepository);
        // inyectar mocks manualmente en campos que no se inyectan por constructor
        clientRestController.clientMapper = clientMapper;
        clientRestController.appointMapper = appointmentMapper;
        clientRestController.appointmentService = appointmentService;
        clientRestController.modelMapper = modelMapper;

        mockMvc = MockMvcBuilders.standaloneSetup(clientRestController).build();
    }

    @Test
    void getUserById_whenUserExists_shouldReturnUserDTO() throws Exception {
        Long userId = 1L;
        ClientEntity client = new ClientEntity();
        client.setId(userId);
        client.setUsername("jose");

        UserRestDTO dto = new UserRestDTO();
        dto.setId(userId);
        dto.setUsername("jose");

        when(clientRepository.findById(userId)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(dto);

        mockMvc.perform(get("/api/clients/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("jose"));
    }

    @Test
    void getUserById_whenUserNotFound_shouldReturn404() throws Exception {
        Long userId = 1L;
        when(clientRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/{id}", userId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_shouldReturnListOfUsers() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setId(1L);
        client.setUsername("jose");
        client.setEmail("jose@mail.com");
        client.setPhone("123456789");
        client.setComments("comentarios");
        client.setGuest(false);
        client.setRoles(Set.of()); // asume roles vacíos para el test
        client.setAppointments(Collections.emptySet());

        List<ClientEntity> clients = List.of(client);

        when(clientRepository.findAll()).thenReturn(clients);
        when(appointmentMapper.toDTOList(anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/clients")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("jose"))
                .andExpect(jsonPath("$[0].email").value("jose@mail.com"))
                .andExpect(jsonPath("$[0].phone").value("123456789"))
                .andExpect(jsonPath("$[0].comments").value("comentarios"))
                .andExpect(jsonPath("$[0].guest").value(false));
    }

    @Test
    void deleteAppointment_shouldCallServiceAndReturnRedirectString() throws Exception {
        mockMvc.perform(post("/api/clients/moderator/delete-appointment")
                        .param("userId", "1")
                        .param("date", "2025-06-11")
                        .param("selectedHourRange", "10:00-10:15"))
                .andExpect(status().isOk())  // Espera 200 OK
                .andExpect(content().string("redirect:/moderator"));  // El contenido que devuelve tu método

        verify(appointmentService, times(1))
                .deleteByClientDateAndHourRange(1L, "2025-06-11", "10:00-10:15");
    }

    @Test
    void updateComments_whenUserExists_shouldSaveCommentsAndReturnRedirectString() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setId(1L);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(post("/api/clients/moderator/update-comments")
                        .param("userId", "1")
                        .param("comments", "Nuevos comentarios"))
                .andExpect(status().isOk())
                .andExpect(content().string("redirect:/moderator"));

        verify(clientRepository, times(1)).save(argThat(c -> "Nuevos comentarios".equals(c.getComments())));
    }


    @Test
    void updateComments_whenUserNotFound_shouldRedirectWithError() throws Exception {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/clients/moderator/update-comments")
                        .param("userId", "1")
                        .param("comments", "Algo"))
                .andExpect(status().isOk())  // status 200 porque no se procesó redirección
                .andExpect(content().string("redirect:/moderator"));
        verify(clientRepository, never()).save(any());
    }

}
