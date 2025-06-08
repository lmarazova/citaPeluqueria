package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.SlotRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.SlotEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de mapear entre {@link SlotEntity} y {@link SlotRestDTO}.
 * Utiliza {@link ModelMapper} para mapear automáticamente los campos básicos
 * y realiza ajustes manuales para representar correctamente relaciones y datos derivados.
 */
@Component
public class SlotMapper {
    @Autowired
    public ModelMapper modelMapper;

    /**
     * Convierte una entidad {@link SlotEntity} a un DTO {@link SlotRestDTO}.
     * Se extraen manualmente algunos campos como nombres de peluquero, cliente y servicio asociado.
     *
     * @param slot la entidad de turno (slot) a convertir.
     * @return el DTO con los datos formateados y enriquecidos, incluyendo nombres y horas como texto.
     */
    public SlotRestDTO toDTO(SlotEntity slot) {
        SlotRestDTO dto = new SlotRestDTO();

        dto.setId(slot.getId());
        dto.setSlotStatus(slot.getSlotStatus().toString());

        dto.setStartHour(slot.getStartHour().toString());
        dto.setFinalHour(slot.getFinalHour().toString());

        if (slot.getHairdresser() != null) {
            dto.setHairdresserName(slot.getHairdresser().getUsername());
        }

        if (slot.getClient() != null) {
            dto.setClientName(slot.getClient().getUsername());
        }


        AppointmentEntity appointment = slot.getAppointment();
        if (appointment != null && appointment.getService() != null) {
            dto.setServicePackName((appointment.getService().getDescription()));
        } else {
            dto.setServicePackName(null); // O puedes omitir esta línea
        }

        return dto;
    }

    /**
     * Convierte un DTO {@link SlotRestDTO} a una entidad {@link SlotEntity}.
     * Usa {@link ModelMapper} para realizar el mapeo automático.
     *
     * @param slotRestDTO el DTO del turno.
     * @return la entidad lista para su uso en persistencia o lógica de negocio.
     */
    public SlotEntity toEntity(SlotRestDTO slotRestDTO){
        return modelMapper.map(slotRestDTO, SlotEntity.class);
    }
}