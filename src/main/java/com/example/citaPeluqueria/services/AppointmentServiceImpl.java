package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.domain.enums.SlotStatus;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.util.HourFormatter;
import com.example.citaPeluqueria.util.SlotConverter;
import com.example.citaPeluqueria.util.SlotProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;
    @Autowired
    private SlotService slotService;
    @Autowired
    private ServiceService serviceService;
    private final ServiceRepository serviceRepository;
    @Autowired
    private SlotConverter slotConverter;
    private final ClientRepository clientRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  SlotRepository slotRepository,
                                  ServiceRepository serviceRepository,
                                  ClientRepository clientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
        this.serviceRepository = serviceRepository;
        this.clientRepository = clientRepository;
    }


    @Override
    public List<AppointmentEntity> getAppointmentsByUser(ClientEntity user) {
        return appointmentRepository.findByUser(user);
    }

    @Override
    public void prepareHourSelectionView(Model model, Long packageId, String date, ClientEntity user) {
        ServiceEntity service = serviceService.getById(packageId);
        LocalDate localDate = LocalDate.parse(date);

        int totalSlots = slotService.calculateTotalSlots(service.getTotalDuration());
        List<SlotEntity> availableSlots = slotService.getAvailableSlots(localDate, packageId);
        List<List<SlotEntity>> rawCombinations = slotService.getAvailableSlotCombinations(localDate, packageId);

        // Convertir a DTOs usando slotConverter
        List<List<SlotOutputDTO>> slotCombinationsDTO = rawCombinations.stream()
                .map(combination -> combination.stream()
                        .map(slot -> slotConverter.convertToDto(slot)) // Usamos slotConverter aquí
                        .toList())
                .toList();

        Set<String> hourRanges = new TreeSet<>(SlotProcessor.generateHourRanges(slotCombinationsDTO));
        Set<String> hourRangePresentation = HourFormatter.formatToHourOnly(hourRanges);

        model.addAttribute("serviceName", service.getPackageEnum().getDisplayName());
        model.addAttribute("slotPattern", service.getPackageEnum().getSlotPattern().toString());
        model.addAttribute("duration", service.getTotalDuration());
        model.addAttribute("totalSlots", totalSlots);
        model.addAttribute("selectedDate", date);
        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("availableSlotCombinations", rawCombinations);
        model.addAttribute("hourRanges", hourRanges);
        model.addAttribute("hourRangePresentation", hourRangePresentation);
        model.addAttribute("appointments", appointmentRepository.findByUser(user));
        model.addAttribute("packageId", packageId);
    }


    @Override
    public boolean createAppointment(String selectedHourRange, Long packageId, String date, ClientEntity user) {
        LocalDate localDate = LocalDate.parse(date);
        ServiceEntity service = serviceRepository.findById(packageId).orElse(null);

        if (service == null) return false;

        List<SlotEntity> selectedCombination = slotService.getFirstAvailableSlotCombinationForHour(localDate, packageId, selectedHourRange);

        if (selectedCombination.isEmpty()) {
            return false;
        }

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setUser(user);
        appointment.setService(service);
        appointment.setSelectedHourRange(selectedHourRange);
        appointment.setDate(date);
        appointment.setSlots(selectedCombination);
        appointmentRepository.save(appointment);

        List<SlotStatus> slotPattern = service.getPackageEnum().getSlotPattern();
        for (int i = 0; i < selectedCombination.size(); i++) {
            SlotEntity slot = selectedCombination.get(i);
            SlotStatus expectedStatus = slotPattern.get(i);

            if (expectedStatus == SlotStatus.FREE && slot.getSlotStatus() == SlotStatus.FREE) {
                slot.setSlotStatus(SlotStatus.OCCUPIED);
                slot.setClient(user);
                slot.setService(service);
                slot.setAppointment(appointment);
                slotRepository.save(slot);
            }
        }

        return true;
    }

    @Override
    public void deleteClientAppointment(Long appointmentId, Principal principal) {

        UserEntity user = clientRepository.findByEmail(principal.getName());

        // Buscar la cita por su ID y asegurarse de que pertenezca al usuario logueado
        Optional<AppointmentEntity> appointmentOptional = appointmentRepository.findById(appointmentId);

        if (appointmentOptional.isPresent()) {
            AppointmentEntity appointment = appointmentOptional.get();

            // Verificar que la cita pertenece al usuario
            if (appointment.getUser().equals(user)) {
                List<SlotEntity> slots = slotRepository.findByClientAndService(user, appointment.getService());
                for (SlotEntity slot : slots) {
                    slot.setSlotStatus(SlotStatus.FREE);
                    slot.setClient(null);
                    slot.setService(null);
                    slotRepository.save(slot);            // Guarda el cambio en la base de datos
                }

                // Eliminar la cita
                appointmentRepository.delete(appointment);
            } else {
                // Si la cita no pertenece al usuario logueado, podrías mostrar un mensaje de error
                // O manejarlo de otra manera
            }
        }
    }

    @Override
    public void deleteByClientDateAndHourRange(Long userId, String date, String selectedHourRange) {
        //LocalDate parsedDate = LocalDate.parse(date);
        Optional<AppointmentEntity> optional = appointmentRepository
                .findByUserIdAndDateAndSelectedHourRange(userId, date, selectedHourRange);

        if (optional.isPresent()) {
            appointmentRepository.delete(optional.get());
        } else {
            throw new RuntimeException("Cita no encontrada.");
        }
    }
}


