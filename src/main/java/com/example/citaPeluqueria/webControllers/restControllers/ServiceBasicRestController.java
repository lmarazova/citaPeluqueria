package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.ServiceBasicRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.mapper.ServiceBasicRestMapper;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.OAEPParameterSpec;
import java.util.List;
import java.util.Optional;
/**
 * Controlador REST que gestiona los servicios básicos de peluquería.
 */
@RestController
@RequestMapping("/api/services-basic")
public class ServiceBasicRestController {
    @Autowired
    public ServiceBasicRestMapper serviceBasicRestMapper;

    private final HairServiceRepository hairServiceRepository;

    /**
     * Constructor que inyecta el repositorio de servicios básicos.
     *
     * @param hairServiceRepository repositorio de servicios de peluquería.
     */
    public ServiceBasicRestController(HairServiceRepository hairServiceRepository) {
        this.hairServiceRepository = hairServiceRepository;
    }

    /**
     * Obtiene un servicio básico por su ID.
     *
     * @param id ID del servicio.
     * @return DTO del servicio si existe o 404 si no se encuentra.
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceBasicRestDTO> getBasicServiceById(@PathVariable Long id){
        Optional<HairServiceEntity> optionalService= hairServiceRepository.findById(id);
        if(optionalService.isPresent()){
            ServiceBasicRestDTO serviceBasicRestDTO = serviceBasicRestMapper.toDTO(optionalService.get());
            return ResponseEntity.ok(serviceBasicRestDTO);
        }else{
            return ResponseEntity.notFound().build();
        }
        }

    /**
     * Obtiene todos los servicios básicos disponibles.
     *
     * @return lista de DTOs de servicios.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceBasicRestDTO> getAllBasicServices(){
        List<HairServiceEntity>hairServices = hairServiceRepository.findAll();
        return hairServices.stream()
                .map(serviceBasicRestMapper::toDTO)
                .toList();

    }
}
