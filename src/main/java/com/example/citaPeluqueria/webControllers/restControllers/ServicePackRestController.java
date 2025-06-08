package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServicePackRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.mapper.ServicePackRestMapper;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
/**
 * Controlador REST que maneja los servicios en paquete.
 */
@RestController
@RequestMapping("/api/service-pack")
public class ServicePackRestController {

    private final ServiceRepository serviceRepository;
    @Autowired
    private ServicePackRestMapper servicePackRestMapper;

    /**
     * Constructor que inyecta el repositorio de paquetes de servicios.
     *
     * @param serviceRepository repositorio de servicios.
     */
    public ServicePackRestController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Obtiene un paquete de servicio por ID.
     *
     * @param id ID del servicio.
     * @return DTO del paquete si se encuentra, o 404 si no.
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServicePackRestDTO>getServicePackById(@PathVariable Long id){
        Optional<ServiceEntity>optionalService = serviceRepository.findById(id);
        if(optionalService.isPresent()){
            ServicePackRestDTO servicePackRestDTO = servicePackRestMapper.toDTO(optionalService.get());
            return ResponseEntity.ok(servicePackRestDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
        }

    /**
     * Obtiene todos los paquetes de servicios registrados.
     *
     * @return lista de DTOs de paquetes de servicios.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServicePackRestDTO> getAllServicePacks(){
        List<ServiceEntity>services = serviceRepository.findAll();
        return services.stream()
                .map(servicePackRestMapper::toDTO)
                .toList();
        }
}

