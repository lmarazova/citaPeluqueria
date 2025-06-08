package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.HairdresserRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.mapper.AppointmentMapper;
import com.example.citaPeluqueria.mapper.HairdresserMapper;
import com.example.citaPeluqueria.mapper.SlotMapper;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Controlador REST que gestiona las operaciones relacionadas con los peluqueros.
 * Permite consultar todos los peluqueros o uno específico por su ID.
 */
@RestController
@RequestMapping("/api/hairdressers")
public class HairdresserRestController {

    private final HairdresserRepository hairdresserRepository;
    @Autowired
    private SlotMapper slotMapper;
    @Autowired
    private HairdresserMapper hairdresserMapper;

    /**
     * Constructor que inyecta el repositorio de peluqueros.
     *
     * @param hairdresserRepository repositorio de peluqueros.
     */
    public HairdresserRestController(HairdresserRepository hairdresserRepository) {
        this.hairdresserRepository = hairdresserRepository;
    }

    /**
     * Obtiene un peluquero por su ID.
     *
     * @param id ID del peluquero a buscar.
     * @return {@link ResponseEntity} con el DTO del peluquero si existe, o 404 si no se encuentra.
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HairdresserRestDTO> getHairdresserById(@PathVariable Long id){
        Optional<HairdresserEntity>optionalHairdresser = hairdresserRepository.findById(id);
        if(optionalHairdresser.isPresent()){
            HairdresserRestDTO hairdresserRestDTO = hairdresserMapper.toDTO(optionalHairdresser.get());
            return ResponseEntity.ok(hairdresserRestDTO);
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    /**
     * Obtiene todos los peluqueros registrados junto con sus turnos ordenados por ID.
     *
     * @return lista de DTOs {@link HairdresserRestDTO} representando a todos los peluqueros.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HairdresserRestDTO> getAllHairdressers() {
        List<HairdresserEntity> hairdressers = hairdresserRepository.findAll();
        return hairdressers.stream()
                .map(hairdresser -> new HairdresserRestDTO(
                        hairdresser.getId(),
                        hairdresser.getUsername(),
                        hairdresser.getEmail(),
                        hairdresser.getSlots().stream()
                                .sorted(Comparator.comparing(SlotEntity::getId)) // ordena por ID
                                .map(slotMapper::toDTO)
                                .collect(Collectors.toList()) // importante: List, no Set
                ))
                .toList();
    }

}
