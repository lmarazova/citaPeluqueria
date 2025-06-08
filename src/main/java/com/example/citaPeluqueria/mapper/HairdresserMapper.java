package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.HairdresserRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Componente responsable de mapear entre {@link HairdresserEntity} y {@link HairdresserRestDTO}.
 * También gestiona la conversión de los {@link SlotRestDTO} asociados mediante {@link SlotMapper}.
 */
@Component
public class HairdresserMapper {
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    SlotMapper slotMapper;


    /**
     * Convierte una entidad {@link HairdresserEntity} a un DTO {@link HairdresserRestDTO},
     * incluyendo la lista de turnos (slots) ordenados por ID.
     *
     * @param entity la entidad de peluquero a convertir.
     * @return el DTO correspondiente con los datos del peluquero y sus turnos.
     */
    public HairdresserRestDTO toDTO(HairdresserEntity entity) {
        HairdresserRestDTO dto = new HairdresserRestDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());

        if (entity.getSlots() != null) {
            List<SlotRestDTO> slots = entity.getSlots().stream()
                    .map(slotMapper::toDTO)
                    .sorted(Comparator.comparing(SlotRestDTO::getId))
                    .collect(Collectors.toList());
            dto.setSlots(slots);
        } else {
            dto.setSlots(Collections.emptyList());
        }

        return dto;
    }

    /**
     * Convierte una lista de entidades {@link HairdresserEntity} en una lista de DTOs {@link HairdresserRestDTO}.
     *
     * @param entities lista de entidades de peluqueros.
     * @return lista de DTOs correspondientes.
     */
    public List<HairdresserRestDTO> toDTOList(List<HairdresserEntity> entities) {
        return entities.stream()
                .map(this::toDTO)
                .toList();
    }

    /**
     * Convierte un DTO {@link HairdresserRestDTO} en una entidad {@link HairdresserEntity}.
     *
     * @param hairdresserRestDTO el DTO del peluquero.
     * @return la entidad correspondiente.
     */
    public HairdresserEntity toEntity(HairdresserRestDTO hairdresserRestDTO){
        return modelMapper.map(hairdresserRestDTO, HairdresserEntity.class);
    }

}
