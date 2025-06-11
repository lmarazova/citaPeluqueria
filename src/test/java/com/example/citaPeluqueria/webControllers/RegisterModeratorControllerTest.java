package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.dtos.HairdresserRegisterDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.services.HairdresserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(SpringExtension.class)
@WebMvcTest(RegisterModeratorController.class)
@AutoConfigureMockMvc
public class RegisterModeratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HairdresserService hairdresserService;

    @MockBean
    private HairdresserRepository hairdresserRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private HairdresserRegisterDTO hairdresserRegisterDTO; // necesario aunque no se use en test directo

    @Test
    @WithMockUser
    void showRegisterModerator_shouldReturnFormWithoutUserDataIfSelfFalse() throws Exception {
        mockMvc.perform(get("/register-moderator"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-moderator"))
                .andExpect(model().attributeExists("hairdresserRegisterDTO"));
    }
    @Test
    @WithMockUser(username = "user1")
    void showRegisterModerator_shouldPreloadUserDataIfSelfTrue() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setUsername("user1");
        client.setEmail("user1@example.com");
        client.setPhone("123456789");

        when(clientRepository.findByUsername("user1")).thenReturn(client);

        mockMvc.perform(get("/register-moderator").param("self", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("register-moderator"))
                .andExpect(model().attributeExists("hairdresserRegisterDTO"));
    }
    @Test
    @WithMockUser
    void registerUser_shouldReturnFormIfValidationErrors() throws Exception {
        mockMvc.perform(post("/register-moderator")
                        .param("email", "") // inválido: vacío
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-moderator"))
                .andExpect(model().attributeExists("hairdresserRegisterDTO"));
    }
    @Test
    @WithMockUser
    void registerUser_shouldReturnFormIfEmailExists() throws Exception {
        when(hairdresserRepository.existsByEmail("existing@example.com")).thenReturn(true);

        mockMvc.perform(post("/register-moderator")
                        .param("email", "existing@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-moderator"))
                .andExpect(model().attribute("error", "Este correo ya está registrado"));
    }
    @Test
    @WithMockUser
    void registerUser_shouldReturnFormIfPasswordsDoNotMatch() throws Exception {
        when(hairdresserRepository.existsByEmail("new@example.com")).thenReturn(false);

        mockMvc.perform(post("/register-moderator")
                        .param("email", "new@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register-moderator"))
                .andExpect(model().attribute("error", "las contraseñas elegidas no coinciden"));
    }
    @Test
    @WithMockUser
    void registerUser_shouldRedirectToAdminIfSuccess() throws Exception {
        when(hairdresserRepository.existsByEmail("new@example.com")).thenReturn(false);

        mockMvc.perform(post("/register-moderator")
                        .param("email", "new@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(hairdresserService, times(1)).registerHairdresser(any());
    }

}
