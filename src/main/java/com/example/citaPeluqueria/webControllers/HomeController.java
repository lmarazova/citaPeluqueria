package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import com.example.citaPeluqueria.repositories.AppointmentRepository;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.repositories.SlotRepository;
import com.example.citaPeluqueria.repositories.ClientRepository;
import com.example.citaPeluqueria.services.AppointmentService;
import com.example.citaPeluqueria.services.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private final ServiceRepository serviceRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private SlotService slotService;
    @Autowired
    AppointmentService appointmentService;
    private final AppointmentRepository appointmentRepository;
    private final SlotRepository slotRepository;

    public HomeController(ServiceRepository serviceRepository,
                          AppointmentRepository appointmentRepository,
                          SlotRepository slotRepository) {
        this.serviceRepository = serviceRepository;
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    @GetMapping("/home")
    public String home(Model model, Principal principal) {
        UserEntity user = clientRepository.findByEmail(principal.getName());
        List<ServiceEntity> packages = serviceRepository.findAll();
        List<AppointmentEntity> appointments = appointmentRepository.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("packages", packages);
        model.addAttribute("appointments", appointments);
       // model.addAttribute("localDate", localDate);

        return "home";
    }
    @PostMapping("/home/delete/{id}")
    public String deleteAppointment(@PathVariable("id") Long appointmentId, Principal principal) {
        appointmentService.deleteClientAppointment(appointmentId, principal);
        // Verifica si el usuario está logueado

        // Redirigir a la página de inicio después de eliminar la cita
        return "redirect:/home";
    }

}
