package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = PasswordResetController.class)
public class PasswordResetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private VerificationTokenRepository tokenRepository;

    @MockBean
    private JavaMailSender mailSender;
    @WithMockUser
    @Test
    void showForgotPasswordPage_shouldReturnForgotPasswordView() throws Exception {
        mockMvc.perform(get("/forgot-password"))
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"));
    }

    @Test
    void processForgotPassword_shouldRedirectWithError_ifEmailNotFound() throws Exception {
        when(clientRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        mockMvc.perform(post("/forgot-password")
                        .param("email", "nonexistent@example.com")
                        .with(csrf())
                        .with(user("testUser").roles("USER")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgot-password"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void processForgotPassword_shouldRedirectWithSuccess_ifEmailExists() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setEmail("test@example.com");

        when(clientRepository.findByEmail("test@example.com")).thenReturn(client);

        mockMvc.perform(post("/forgot-password")
                        .param("email", "test@example.com")
                        .with(csrf())
                        .with(user("testUser").roles("USER"))) // añade esto
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("success"));

        verify(tokenRepository, times(1)).save(any(VerificationToken.class));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void showResetForm_shouldShowForm_ifTokenValid() throws Exception {
        VerificationToken token = new VerificationToken();
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));

        when(tokenRepository.findByToken("validToken")).thenReturn(token);

        mockMvc.perform(get("/reset-password").param("token", "validToken")
                .with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attribute("token", "validToken"));
    }

    @Test
    void showResetForm_shouldShowError_ifTokenInvalidOrExpired() throws Exception {
        when(tokenRepository.findByToken("invalidToken")).thenReturn(null);

        mockMvc.perform(get("/reset-password").param("token", "invalidToken")
                .with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(view().name("reset-password"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    void handlePasswordReset_shouldRedirectWithError_ifPasswordsDoNotMatch() throws Exception {
        mockMvc.perform(post("/reset-password")
                        .param("token", "someToken")
                        .param("password", "abc123")
                        .param("confirmPassword", "xyz456")
                        .with(user("testUser").roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reset-password?token=someToken"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void handlePasswordReset_shouldRedirectWithError_ifTokenInvalid() throws Exception {
        when(tokenRepository.findByToken("invalidToken")).thenReturn(null);

        mockMvc.perform(post("/reset-password")
                        .param("token", "invalidToken")
                        .param("password", "abc123")
                        .param("confirmPassword", "abc123")
                        .with(user("testUser").roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgot-password"))
                .andExpect(flash().attributeExists("error"));
    }

    @Test
    void handlePasswordReset_shouldUpdatePassword_ifTokenValidAndPasswordsMatch() throws Exception {
        ClientEntity client = new ClientEntity();
        client.setEmail("client@example.com");

        VerificationToken vt = new VerificationToken();
        vt.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        vt.setClient(client);

        when(tokenRepository.findByToken("validToken")).thenReturn(vt);

        mockMvc.perform(post("/reset-password")
                        .param("token", "validToken")
                        .param("password", "newPassword123")
                        .param("confirmPassword", "newPassword123")
                        .with(user("testUser").roles("USER"))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("success"));

        verify(clientRepository, times(1)).save(any(ClientEntity.class));
        verify(tokenRepository, times(1)).delete(vt);
    }
}
