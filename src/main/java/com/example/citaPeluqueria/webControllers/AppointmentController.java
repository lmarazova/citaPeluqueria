package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import com.example.citaPeluqueria.services.ClientService;
import com.example.citaPeluqueria.services.ServiceService;
import com.example.citaPeluqueria.services.SlotService;
import com.example.citaPeluqueria.util.SlotConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
public class AppointmentController {

    @Autowired
    private SlotService slotService;
    @Autowired
    private ClientService userService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private AppointmentService appointmentService; // Para obtener las franjas horarias
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SlotConverter slotConverter;
    @Autowired
    private AppointmentRepository appointmentRepository;

    @GetMapping("/selectAnHour")
    public String selectAnHour(@RequestParam("packageId") Long packageId,
                               @RequestParam("date") String date,
                               Principal principal,
                               Model model) {

        ClientEntity user = clientRepository.findByEmail(principal.getName());
        appointmentService.prepareHourSelectionView(model, packageId, date, user);
        return "hours-available";
    }

    @PostMapping("/selectSlot")
    public String selectSlot(@RequestParam("selectedHourRange") String selectedHourRange,
                             @RequestParam("packageId") Long packageId,
                             @RequestParam("date") String date,
                             Model model,
                             Principal principal) {

        ClientEntity user = clientRepository.findByEmail(principal.getName());
        boolean success = appointmentService.createAppointment(selectedHourRange, packageId, date, user);

        if (!success) {
            model.addAttribute("errorMessage", "No se encontró una franja horaria que coincida con la hora seleccionada.");
            return "error";
        }

        return "redirect:/home";
    }





    }






