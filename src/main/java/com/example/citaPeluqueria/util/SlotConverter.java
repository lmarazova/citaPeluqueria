package com.example.citaPeluqueria.util;

import com.example.citaPeluqueria.domain.dtos.SlotOutputDTO;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de convertir {@link SlotEntity} a su representación {@link SlotOutputDTO}.
 */
@Component
public class SlotConverter {

    /**
     * Convierte una entidad Slot en un DTO para la salida.
     *
     * @param entity entidad Slot a convertir.
     * @return DTO con los datos del slot.
     */
    public SlotOutputDTO convertToDto(SlotEntity entity) {
        SlotOutputDTO slotOutputDTO = new SlotOutputDTO();
        slotOutputDTO.setId(entity.getId());
        slotOutputDTO.setSlotStartHour(String.valueOf(entity.getStartHour()));
        slotOutputDTO.setSlotFinalHour(String.valueOf(entity.getFinalHour()));

        System.out.println("Convirtiendo slot con ID: " + entity.getId());
        System.out.println("Peluquero asignado: " + entity.getHairdresser());

        if (entity.getHairdresser() != null) {
            slotOutputDTO.setHairdresser(entity.getHairdresser().getUsername());
        } else {
            slotOutputDTO.setHairdresser(null);
        }
        slotOutputDTO.setHairdresser(entity.getHairdresser().getUsername());
        slotOutputDTO.setSlotStatus(String.valueOf(entity.getSlotStatus()));
        return slotOutputDTO;
    }
}
