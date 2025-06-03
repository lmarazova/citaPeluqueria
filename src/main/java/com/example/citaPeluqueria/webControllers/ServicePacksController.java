package com.example.citaPeluqueria.webControllers;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.example.citaPeluqueria.services.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ServicePacksController {

    private final ServiceRepository serviceRepository;
    @Autowired
    private ServiceService serviceService;

    public ServicePacksController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @GetMapping("/service-packs")
    public String showAllService(Model model){
        List<ServiceEntity> packages = serviceRepository.findAll();
        model.addAttribute("packages", packages);

        return "service-packs";
    }
    @PostMapping("/services/update")
    public String updateService(@RequestParam Long id,
                                @RequestParam double price,
                                @RequestParam(required = false) Boolean active) {
        boolean isActive = active != null; // checkbox no enviado si está desmarcado

        serviceService.updateService(id, price, isActive);
        return "redirect:/service-packs";
    }


}
