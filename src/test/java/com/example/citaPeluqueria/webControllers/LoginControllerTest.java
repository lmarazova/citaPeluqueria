package com.example.citaPeluqueria.webControllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        LoginController loginController = new LoginController();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers((viewName, locale) -> {
                    // Devuelve una vista ficticia para evitar la resolución real y romper el bucle
                    InternalResourceView view = new InternalResourceView();
                    view.setUrl("/WEB-INF/views/" + viewName + ".jsp");
                    return view;
                })
                .build();
    }

    @Test
    void showLoginForm_shouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}