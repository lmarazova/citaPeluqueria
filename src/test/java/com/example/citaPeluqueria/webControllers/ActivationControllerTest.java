package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.VerificationToken;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;



@WebMvcTest(ActivationController.class)
class ActivationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VerificationTokenRepository tokenRepository;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    @WithMockUser(username="user", roles={"USER"})
    void testActivateUser_Success() throws Exception {
        VerificationToken token = new VerificationToken();
        token.setToken("valid-token");
        token.setExpiryDate(LocalDateTime.now().plusDays(1));

        ClientEntity client = new ClientEntity();
        client.setEmail("test@example.com");
        client.setEnabled(false);

        token.setClient(client);

        when(tokenRepository.findByToken("valid-token")).thenReturn(token);

        mockMvc.perform(get("/activate")
                        .param("token", "valid-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("success", "Cuenta activada correctamente"));

        verify(clientRepository).save(client);
        verify(tokenRepository).delete(token);
    }

    @Test
    @WithMockUser
    void testActivateUser_InvalidToken() throws Exception {
        when(tokenRepository.findByToken("invalid-token")).thenReturn(null);

        mockMvc.perform(get("/activate")
                        .param("token", "invalid-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Token inválido o caducado"));

        verify(clientRepository, never()).save(any());
    }

    @Test
    @WithMockUser
    void testActivateUser_ExpiredToken() throws Exception {
        VerificationToken token = new VerificationToken();
        token.setToken("expired-token");
        token.setExpiryDate(LocalDateTime.now().minusDays(1)); // ya caducado

        when(tokenRepository.findByToken("expired-token")).thenReturn(token);

        mockMvc.perform(get("/activate")
                        .param("token", "expired-token"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("error", "Token inválido o caducado"));

        verify(clientRepository, never()).save(any());
    }
}
