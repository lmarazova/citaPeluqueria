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
/**
 * Controlador para gestionar los paquetes de servicios disponibles.
 * Permite visualizar todos los servicios y actualizar su información.
 */
@Controller
public class ServicePacksController {

    private final ServiceRepository serviceRepository;
    @Autowired
    private ServiceService serviceService;


    /**
     * Constructor del controlador, inyectando el repositorio de servicios.
     *
     * @param serviceRepository Repositorio que gestiona los paquetes de servicio.
     */
    public ServicePacksController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }


    /**
     * Muestra todos los paquetes de servicio disponibles.
     *
     * @param model Modelo para pasar información a la vista.
     * @return Nombre de la vista "service-packs".
     */
    @GetMapping("/service-packs")
    public String showAllService(Model model){
        List<ServiceEntity> packages = serviceRepository.findAll();
        model.addAttribute("packages", packages);

        return "service-packs";
    }

    /**
     * Actualiza la información de un servicio.
     *
     * @param id Identificador del servicio a actualizar.
     * @param price Nuevo precio del servicio.
     * @param active Estado de activación del servicio (true si está activo, false si no).
     * @return Redirección a la página de paquetes de servicio.
     */
    @PostMapping("/services/update")
    public String updateService(@RequestParam Long id,
                                @RequestParam double price,
                                @RequestParam(required = false) Boolean active) {
        boolean isActive = active != null; // checkbox no enviado si está desmarcado

        serviceService.updateService(id, price, isActive);
        return "redirect:/service-packs";
    }


}
