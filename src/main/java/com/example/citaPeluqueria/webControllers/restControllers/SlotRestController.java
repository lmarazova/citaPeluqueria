package com.example.citaPeluqueria.webControllers.restControllers;

import com.example.citaPeluqueria.domain.dtos.restDTOS.HairdresserRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import com.example.citaPeluqueria.mapper.SlotMapper;
import com.example.citaPeluqueria.repositories.SlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * Controlador REST que maneja los turnos disponibles.
 */
@RestController
@RequestMapping("/api/slots")
public class SlotRestController {
    @Autowired
    private SlotMapper slotMapper;
    private final SlotRepository slotRepository;

    /**
     * Constructor que inyecta el repositorio de turnos.
     *
     * @param slotRepository repositorio de turnos.
     */
    public SlotRestController(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    /**
     * Obtiene un turno específico por ID.
     *
     * @param id ID del turno.
     * @return DTO del turno si existe, o 404 si no.
     */
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SlotRestDTO> getSlotById(@PathVariable Long id){
        return slotRepository.findById(id)
                .map(slotMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los turnos registrados.
     *
     * @return lista de DTOs de turnos.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<SlotRestDTO> getAllSlots(){ List<SlotEntity> slots = slotRepository.findAll();
        return slots.stream()
                .map(slotMapper::toDTO)
                .toList();}
}
