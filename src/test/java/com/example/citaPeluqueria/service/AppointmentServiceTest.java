package com.example.citaPeluqueria.service;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.services.AppointmentServiceImpl;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.SlotService;
import com.example.citaPeluqueria.util.HourFormatter;
import com.example.citaPeluqueria.util.SlotConverter;
import com.example.citaPeluqueria.util.SlotProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.Model;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.security.Principal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private SlotService slotService;

    @Mock
    private ServiceService serviceService;

    @Mock
    private SlotConverter slotConverter;

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private ServiceRepository serviceRepository;
    @Mock
    private SlotRepository slotRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        // Inyectar los mocks manualmente en los campos privados con @Autowired
        ReflectionTestUtils.setField(appointmentService, "slotService", slotService);
        ReflectionTestUtils.setField(appointmentService, "serviceService", serviceService);
        ReflectionTestUtils.setField(appointmentService, "slotConverter", slotConverter);
    }

    @Test
    public void testGetAppointmentsByUser() {
        ClientEntity user = new ClientEntity();
        List<AppointmentEntity> expected = List.of(new AppointmentEntity());
        Mockito.when(appointmentRepository.findByUser(user)).thenReturn(expected);

        List<AppointmentEntity> result = appointmentService.getAppointmentsByUser(user);
        assertEquals(expected, result);
    }

    // Test createAppointment when service is not found
    @Test
    public void testCreateAppointment_ServiceNotFound() {
        Mockito.when(serviceRepository.findById(1L)).thenReturn(Optional.empty());
        boolean result = appointmentService.createAppointment("10:00-11:00", 1L, "2025-06-10", new ClientEntity());
        assertFalse(result);
    }

    // Test createAppointment with valid input
    @Test
    public void testCreateAppointment_Successful() {
        // Setup de datos de entrada
        String date = "2025-06-10";
        String hourRange = "10:00-10:45";
        Long packageId = 1L;
        ClientEntity user = new ClientEntity();

        // Usamos un valor REAL del enum
        ServicePackageEnum serviceEnum = ServicePackageEnum.CUT_WASH_BLOWDRY;

        // Creamos un ServiceEntity con datos reales
        ServiceEntity service = new ServiceEntity();
        service.setTotalDuration(serviceEnum.getDurationMinutes());
        service.setPackageEnum(serviceEnum);

        // Creamos los SlotEntity necesarios
        List<SlotEntity> slotCombination = new ArrayList<>();
        for (SlotStatus status : serviceEnum.getSlotPattern()) {
            SlotEntity slot = new SlotEntity();
            slot.setSlotStatus(status);
            slot.setStartHour(LocalDateTime.of(2025, 6, 10, 10, 0));
            slot.setFinalHour(LocalDateTime.of(2025, 6, 10, 10, 15));
            slotCombination.add(slot);
        }

        // Configuramos mocks
        when(serviceRepository.findById(packageId)).thenReturn(Optional.of(service));
        when(slotService.getFirstAvailableSlotCombinationForHour(
                LocalDate.parse(date), packageId, hourRange)).thenReturn(slotCombination);

        // Ejecutamos
        boolean result = appointmentService.createAppointment(hourRange, packageId, date, user);

        // Verificamos
        assertTrue(result);
        verify(appointmentRepository).save(any(AppointmentEntity.class));
        verify(slotRepository, atLeastOnce()).save(any(SlotEntity.class));
    }

    // Test deleteClientAppointment with correct user
    @Test
    public void testDeleteClientAppointment_ValidUser() {
        Long appointmentId = 1L;
        Principal principal = () -> "test@example.com";
        ClientEntity user = new ClientEntity();
        user.setEmail("test@example.com");

        ServiceEntity service = new ServiceEntity();
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(user);
        appointment.setService(service);

        List<SlotEntity> slots = List.of(new SlotEntity());

        Mockito.when(clientRepository.findByEmail("test@example.com")).thenReturn(user);
        Mockito.when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        Mockito.when(slotRepository.findByClientAndService(user, service)).thenReturn(slots);

        appointmentService.deleteClientAppointment(appointmentId, principal);

        Mockito.verify(slotRepository, atLeastOnce()).save(any(SlotEntity.class));
        Mockito.verify(appointmentRepository).delete(appointment);
    }

    // Test deleteByClientDateAndHourRange when appointment not found
    @Test
    public void testDeleteByClientDateAndHourRange_NotFound() {
        Mockito.when(appointmentRepository.findByUserIdAndDateAndSelectedHourRange(1L, "2025-06-10", "10:00-10:30"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                appointmentService.deleteByClientDateAndHourRange(1L, "2025-06-10", "10:00-10:30"));
    }

    @Test
    public void testCreateAppointment_EmptySlotCombination() {
        String date = "2025-06-10";
        String hourRange = "10:00-10:45";
        Long packageId = 1L;
        ClientEntity user = new ClientEntity();

        ServiceEntity service = new ServiceEntity();
        service.setTotalDuration(60);
        service.setPackageEnum(ServicePackageEnum.CUT_WASH_BLOWDRY);

        when(serviceRepository.findById(packageId)).thenReturn(Optional.of(service));
        when(slotService.getFirstAvailableSlotCombinationForHour(LocalDate.parse(date), packageId, hourRange))
                .thenReturn(Collections.emptyList());

        boolean result = appointmentService.createAppointment(hourRange, packageId, date, user);

        assertFalse(result);
        verify(appointmentRepository, never()).save(any());
    }
    @Test
    public void testPrepareHourSelectionView() {
        Long packageId = 1L;
        String date = "2025-06-10";
        ClientEntity user = new ClientEntity();

        ServiceEntity service = new ServiceEntity();
        service.setPackageEnum(ServicePackageEnum.CUT_WASH_BLOWDRY);
        service.setTotalDuration(service.getPackageEnum().getDurationMinutes());

        when(serviceService.getById(packageId)).thenReturn(service);
        when(slotService.calculateTotalSlots(service.getTotalDuration())).thenReturn(4);
        List<SlotEntity> availableSlots = List.of(new SlotEntity());
        when(slotService.getAvailableSlots(LocalDate.parse(date), packageId)).thenReturn(availableSlots);
        List<List<SlotEntity>> slotCombinations = List.of(List.of(new SlotEntity()));
        when(slotService.getAvailableSlotCombinations(LocalDate.parse(date), packageId)).thenReturn(slotCombinations);
        when(slotConverter.convertToDto(any())).thenReturn(new SlotOutputDTO());

        appointmentService.prepareHourSelectionView(model, packageId, date, user);

        verify(model).addAttribute(eq("serviceName"), any());
        verify(model).addAttribute(eq("totalSlots"), eq(4));
        verify(model).addAttribute(eq("availableSlots"), eq(availableSlots));
        verify(model).addAttribute(eq("availableSlotCombinations"), eq(slotCombinations));
        verify(model).addAttribute(eq("appointments"), any());
    }
    @Test
    public void testDeleteClientAppointment_AppointmentNotFound() {
        Long appointmentId = 1L;
        Principal principal = () -> "test@example.com";

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

        // No debería lanzar excepción ni hacer nada
        appointmentService.deleteClientAppointment(appointmentId, principal);

        verify(appointmentRepository, never()).delete(any());
        verify(slotRepository, never()).save(any());
    }
    @Test
    public void testDeleteClientAppointment_NotOwner() {
        Long appointmentId = 1L;
        Principal principal = () -> "test@example.com";

        ClientEntity loggedUser = new ClientEntity();
        loggedUser.setEmail("test@example.com");

        ClientEntity appointmentOwner = new ClientEntity();
        appointmentOwner.setEmail("other@example.com");

        ServiceEntity service = new ServiceEntity();
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(appointmentOwner);
        appointment.setService(service);

        when(clientRepository.findByEmail("test@example.com")).thenReturn(loggedUser);
        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        appointmentService.deleteClientAppointment(appointmentId, principal);

        // No debe borrar la cita ni liberar slots porque el usuario no es propietario
        verify(appointmentRepository, never()).delete(any());
        verify(slotRepository, never()).save(any());
    }

}


