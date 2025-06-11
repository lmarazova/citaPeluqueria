package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.HolidayEntity;
import com.example.citaPeluqueria.repositories.HolidayRepository;
import com.example.citaPeluqueria.util.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class AddHolidayControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HolidayRepository holidayRepository;
    @Test
    @WithMockUser
    void testShowAddHolidayForm() throws Exception {
        List<HolidayEntity> holidays = List.of(new HolidayEntity()); // ejemplo dummy
        when(holidayRepository.findAll()).thenReturn(holidays);

        mockMvc.perform(get("/add-holidays"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-holidays"))
                .andExpect(model().attributeExists("newHoliday"))
                .andExpect(model().attribute("holidays", holidays))
                .andExpect(model().attribute("startHour", Constants.START_HOUR))
                .andExpect(model().attribute("workedHours", Constants.WORKED_HOURS));
    }
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddHoliday_Success() throws Exception {
        HolidayEntity holiday = new HolidayEntity();
        holiday.setDate(LocalDate.of(2025, 12, 25)); // ejemplo

        when(holidayRepository.findByDate(holiday.getDate())).thenReturn(Optional.empty());

        mockMvc.perform(post("/add-holidays")
                        .with(csrf()) // <-- esto es lo clave para evitar el 403
                        .flashAttr("newHoliday", holiday))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-holidays"))
                .andExpect(flash().attribute("success", "Festivo añadido correctamente"));

        verify(holidayRepository).save(holiday);
    }
    @Test
    @WithMockUser(roles = "ADMIN")  // Importante para simular usuario con rol ADMIN si usas seguridad
    void testAddHoliday_AlreadyExists() throws Exception {
        HolidayEntity holiday = new HolidayEntity();
        holiday.setDate(LocalDate.of(2025, 12, 25));

        when(holidayRepository.findByDate(holiday.getDate())).thenReturn(Optional.of(holiday));

        mockMvc.perform(post("/add-holidays")
                        .with(csrf())  // <- aquí agregas el token CSRF
                        .flashAttr("newHoliday", holiday))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-holidays"))
                .andExpect(flash().attribute("error", "Ya existe un festivo para esa fecha"));

        verify(holidayRepository, never()).save(any());
    }
    @Test
    @WithMockUser(roles = "ADMIN")  // Añade usuario con rol admin, o el rol que necesites
    void testDeleteHoliday() throws Exception {
        Long id = 1L;

        mockMvc.perform(post("/delete/{id}", id)
                        .with(csrf()))  // Añade token CSRF
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/add-holidays"));

        verify(holidayRepository).deleteById(id);
    }
}
