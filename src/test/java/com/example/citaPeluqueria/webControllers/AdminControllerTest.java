package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.Role;
import com.example.citaPeluqueria.repositories.*;
import com.example.citaPeluqueria.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import com.example.citaPeluqueria.repositories.HolidayRepository;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;



@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private HairdresserRepository hairdresserRepository;

    @MockBean
    private SlotRepository slotRepository;

    @MockBean
    private ServiceRepository serviceRepository;

    @MockBean
    private HolidayRepository holidayRepository;

    private Principal principal;

    @BeforeEach
    void setup() {
        principal = () -> "adminUser";
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAdminPanel() throws Exception {
        List<HairdresserEntity> hairdressers = List.of(new HairdresserEntity());
        List<ClientEntity> clients = List.of(new ClientEntity());
        List<ServiceEntity> services = List.of(new ServiceEntity());

        when(hairdresserRepository.findAll()).thenReturn(hairdressers);
        when(clientRepository.findAll()).thenReturn(clients);
        when(serviceRepository.findAll()).thenReturn(services);
        when(hairdresserRepository.findByUsername("adminUser")).thenReturn(null);

        mockMvc.perform(get("/admin").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("admin"))
                .andExpect(model().attribute("moderators", hairdressers))
                .andExpect(model().attribute("clients", clients))
                .andExpect(model().attribute("packages", services))
                .andExpect(model().attribute("isAdminHairdresserRegistered", false));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateAdmin_WhenNotExists() throws Exception {
        when(clientRepository.findByUsername(Constants.ADMIN_NAME)).thenReturn(null);
        when(passwordEncoder.encode(Constants.ADMIN_PASSWORD)).thenReturn("encryptedPassword");

        mockMvc.perform(get("/admin/createAdmin"))
                .andExpect(status().isOk())
                .andExpect(content().string("Admin creado correctamente."));

        verify(clientRepository).save(argThat(admin ->
                Constants.ADMIN_NAME.equals(admin.getUsername()) &&
                        Constants.ADMIN_EMAIL.equals(admin.getEmail()) &&
                        Constants.ADMIN_PHONE.equals(admin.getPhone()) &&
                        "encryptedPassword".equals(admin.getPassword()) &&
                        admin.getRoles().containsAll(Set.of(Role.ADMIN, Role.MODERATOR))
        ));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateAdmin_WhenExists() throws Exception {
        ClientEntity existingAdmin = new ClientEntity();
        when(clientRepository.findByUsername(Constants.ADMIN_NAME)).thenReturn(existingAdmin);

        mockMvc.perform(get("/admin/createAdmin"))
                .andExpect(status().isOk())
                .andExpect(content().string("El usuario administrador ya existe."));

        verify(clientRepository, never()).save(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteModerator() throws Exception {
        Long id = 1L;
        HairdresserEntity hairdresser = mock(HairdresserEntity.class);
        Set<SlotEntity> slots = new HashSet<>();
        when(hairdresserRepository.findById(id)).thenReturn(Optional.of(hairdresser));
        when(hairdresser.getSlots()).thenReturn(slots);

        mockMvc.perform(post("/admin/deleteModerator/{id}", id)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        verify(slotRepository).deleteAll(slots);
        verify(hairdresserRepository).delete(hairdresser);
    }

    @Test
    void testAddHoliday() throws Exception {
        // Realiza el POST autenticado como usuario con rol ADMIN
        mockMvc.perform(post("/admin/holidays/add")
                        .param("date", "2025-12-25")
                        .param("description", "Navidad")
                        .with(csrf()) // Incluye token CSRF
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin"));

        // Verificar que se haya guardado un HolidayEntity con esos datos
        ArgumentCaptor<HolidayEntity> holidayCaptor = ArgumentCaptor.forClass(HolidayEntity.class);
        verify(holidayRepository).save(holidayCaptor.capture());

        HolidayEntity savedHoliday = holidayCaptor.getValue();
        assertThat(savedHoliday.getDate()).isEqualTo(LocalDate.of(2025, 12, 25));
        assertThat(savedHoliday.getDescription()).isEqualTo("Navidad");
    }



}
