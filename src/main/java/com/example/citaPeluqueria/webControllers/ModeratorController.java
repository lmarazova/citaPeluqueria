package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.*;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import com.example.citaPeluqueria.services.ClientService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.SlotService;

import com.example.citaPeluqueria.util.SlotConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class ModeratorController {

    private final HairdresserRepository hairdresserRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final AppointmentRepository appointmentRepository;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private SlotService slotService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SlotConverter slotConverter;
    @Autowired
    private AppointmentService appointmentService;

    public ModeratorController(HairdresserRepository hairdresserRepository,
                               ServiceRepository serviceRepository,
                               ClientRepository clientRepository,
                               AppointmentRepository appointmentRepository) {
        this.hairdresserRepository = hairdresserRepository;
        this.serviceRepository = serviceRepository;
        this.clientRepository = clientRepository;
        this.appointmentRepository = appointmentRepository;
    }

    @GetMapping("moderator")
    public String showModeratorPage(Model model, Principal principal){
        String username = principal.getName();

        HairdresserEntity hairdresser = hairdresserRepository.findByEmail(principal.getName());

        List<SlotEntity> slots = new ArrayList<>(hairdresser.getSlots());
        slots.sort(Comparator.comparing(SlotEntity::getStartHour));
        List<ServiceEntity>packages = serviceRepository.findAll();
        Map<LocalDate, List<SlotEntity>> groupedSlots = slotService.slotOrdering(hairdresser);

        // Enviar al modelo para Thymeleaf
        model.addAttribute("groupedSlots", groupedSlots);
        model.addAttribute("hairdresser", hairdresser);
        model.addAttribute("slots", slots);
        model.addAttribute("packages", packages);
        return "moderator";
    }



    @GetMapping("/moderator/refresh-hours")
    public String refreshHoursForGuest(@RequestParam String username,
                                       @RequestParam String phone,
                                       @RequestParam Long packageId,
                                       @RequestParam String date,
                                       Model model,
                                       Principal principal) {

        ClientEntity user = clientService.findOrCreateGuest(username, phone);
        appointmentService.prepareHourSelectionView(model, packageId, date, user);

        HairdresserEntity hairdresser = hairdresserRepository.findByEmail(principal.getName());
        Map<LocalDate, List<SlotEntity>> groupedSlots = slotService.slotOrdering(hairdresser);

        model.addAttribute("groupedSlots", groupedSlots);
        model.addAttribute("hairdresser", hairdresser);
        model.addAttribute("packages", serviceRepository.findAll());
        model.addAttribute("phone", phone);
        model.addAttribute("username", username);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedPackageId", packageId);

        return "moderator";
    }



    // Método para manejar la creación del cliente y la cita
    @PostMapping("/moderator/refresh-hours")
    public String createGuestAppointment(@RequestParam String username,
                                         @RequestParam String phone,
                                         @RequestParam Long packageId,
                                         @RequestParam String date,
                                         @RequestParam("selectedHourRange") String selectedHourRange,
                                         Model model, Principal principal) {

        ClientEntity userGuest = clientService.findOrCreateGuest(username, phone);
        String hairdresserName = principal.getName();
        HairdresserEntity hairdresser = hairdresserRepository.findByEmail(hairdresserName);
        model.addAttribute("hairdresser",hairdresser);
        boolean success = appointmentService.createAppointment(selectedHourRange, packageId, date, userGuest);

        if (!success) {
            model.addAttribute("errorMessage", "No se encontró una franja horaria que coincida con la hora seleccionada.");
            return "error";
        }
        return "redirect:/moderator"; // Redirigir a la lista de citas del moderador
    }
    @GetMapping("/moderator/check-client")
    @ResponseBody
    public ResponseEntity<Map<String, String>> checkClient(@RequestParam String phone) {
        ClientEntity user = clientService.findByPhone(phone);
        if (user != null) {
            Map<String, String> data = new HashMap<>();
            data.put("username", user.getUsername());
            return ResponseEntity.ok(data);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}
