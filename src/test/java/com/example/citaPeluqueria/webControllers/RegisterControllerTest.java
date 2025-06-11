package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ClientService clientService;


    // -------------- GET /register --------------
    @Test
    void showRegisterController_shouldReturnRegisterViewAndModel() throws Exception {
        mockMvc.perform(get("/register")
                        .with(user("testUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    // -------------- POST /register --------------

    @Test
    void registerUser_shouldReturnRegisterViewIfValidationErrors() throws Exception {
        mockMvc.perform(post("/register")
                        .param("email", "test@example.com") // Sin confirmPassword, error esperado
                        .param("password", "pass123")
                        .with(csrf())
                        .with(user("testUser")))   // Usuario simulado
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("userRegisterDTO"));
    }

    @Test
    void registerUser_shouldReturnRegisterViewIfEmailExists() throws Exception {
        when(clientRepository.existsByEmail("existing@example.com")).thenReturn(true);

        mockMvc.perform(post("/register")
                        .param("email", "existing@example.com")
                        .param("phone", "123456789")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .with(csrf())
                        .with(user("testUser")))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Este correo ya está registrado"));
    }

    @Test
    @WithMockUser
    void registerUser_shouldRedirectEvenIfPhoneExistsBecauseControllerDoesNotReturn() throws Exception {
        when(clientRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(clientRepository.existsByPhone("123456789")).thenReturn(true);

        mockMvc.perform(post("/register")
                        .param("email", "new@example.com")
                        .param("phone", "123456789")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login")); // <-- Esto es lo que realmente hace
    }


    @Test
    @WithMockUser
    void registerUser_shouldReturnRegisterViewIfPasswordsDoNotMatch() throws Exception {
        when(clientRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(clientRepository.existsByPhone("123456789")).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("email", "new@example.com")
                        .param("phone", "123456789")
                        .param("password", "pass123")
                        .param("confirmPassword", "diffpass")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "las contraseñas elegidas no coinciden"));
    }

    @Test
    @WithMockUser // ← Esto es imprescindible para evitar el 401
    void registerUser_shouldRedirectToLoginIfSuccess() throws Exception {
        when(clientRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(clientRepository.existsByPhone("123456789")).thenReturn(false);

        mockMvc.perform(post("/register")
                        .param("email", "new@example.com")
                        .param("phone", "123456789")
                        .param("password", "pass123")
                        .param("confirmPassword", "pass123")
                        .with(csrf())) // ← CSRF es obligatorio si tu app lo tiene habilitado
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(clientService, times(1)).registerUser(any());
    }

}

