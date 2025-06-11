package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.springframework.ui.Model;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;






public class AppointmentControllerTest {

    @InjectMocks
    private AppointmentController appointmentController;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private Model model;

    @Mock
    private Principal principal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSelectAnHour_ReturnsHoursAvailableView() {
        // Arrange
        Long packageId = 1L;
        String date = "2025-06-10";
        String email = "user@example.com";

        ClientEntity user = new ClientEntity();
        when(principal.getName()).thenReturn(email);
        when(clientRepository.findByEmail(email)).thenReturn(user);

        // Act
        String viewName = appointmentController.selectAnHour(packageId, date, principal, model);

        // Assert
        verify(appointmentService).prepareHourSelectionView(model, packageId, date, user);
        assertEquals("hours-available", viewName);
    }

    @Test
    public void testSelectSlot_Success_RedirectsToHome() {
        // Arrange
        String selectedHourRange = "10:00-11:00";
        Long packageId = 1L;
        String date = "2025-06-10";
        String email = "user@example.com";

        ClientEntity user = new ClientEntity();
        when(principal.getName()).thenReturn(email);
        when(clientRepository.findByEmail(email)).thenReturn(user);
        when(appointmentService.createAppointment(selectedHourRange, packageId, date, user)).thenReturn(true);

        // Act
        String viewName = appointmentController.selectSlot(selectedHourRange, packageId, date, model, principal);

        // Assert
        assertEquals("redirect:/home", viewName);
        verify(model, never()).addAttribute(eq("errorMessage"), any());
    }

    @Test
    public void testSelectSlot_Failure_ReturnsErrorView() {
        // Arrange
        String selectedHourRange = "10:00-11:00";
        Long packageId = 1L;
        String date = "2025-06-10";
        String email = "user@example.com";

        ClientEntity user = new ClientEntity();
        when(principal.getName()).thenReturn(email);
        when(clientRepository.findByEmail(email)).thenReturn(user);
        when(appointmentService.createAppointment(selectedHourRange, packageId, date, user)).thenReturn(false);

        // Act
        String viewName = appointmentController.selectSlot(selectedHourRange, packageId, date, model, principal);

        // Assert
        assertEquals("error", viewName);
        verify(model).addAttribute(eq("errorMessage"), anyString());
    }
}