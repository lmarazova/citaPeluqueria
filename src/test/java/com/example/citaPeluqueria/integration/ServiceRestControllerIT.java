package com.example.citaPeluqueria.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetServicePackById_returnsServicePack() throws Exception {
        mockMvc.perform(get("/api/service-pack/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").exists()) // Cambiado 'name' por 'description'
                .andExpect(jsonPath("$.price").exists())
                .andExpect(jsonPath("$.totalDuration").exists())
                .andExpect(jsonPath("$.active").exists());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllServicePacks_returnsListOfServicePacks() throws Exception {
        mockMvc.perform(get("/api/service-pack")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].description").exists())  // Cambiado 'name' por 'description'
                .andExpect(jsonPath("$[0].price").exists())
                .andExpect(jsonPath("$[0].totalDuration").exists())
                .andExpect(jsonPath("$[0].active").exists());
    }
}
