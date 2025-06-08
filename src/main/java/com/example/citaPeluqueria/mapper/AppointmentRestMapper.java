package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de mapear entre {@link AppointmentEntity} y {@link AppointmentRestDTO}.
 * Utiliza {@link ModelMapper} para la conversión automática de campos y {@link ServiceMapper}
 * para mapear información adicional del servicio asociado.
 */
@Component
public class AppointmentRestMapper {
    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public ServiceMapper serviceMapper;

    /**
     * Convierte una entidad {@link AppointmentEntity} a un DTO {@link AppointmentRestDTO}.
     * Incluye la descripción del servicio si está presente.
     *
     * @param appointment la entidad de cita a convertir.
     * @return el DTO correspondiente con los datos mapeados, o {@code null} si la entrada es {@code null}.
     */
    public AppointmentRestDTO toDTO(AppointmentEntity appointment){
        if(appointment == null){
            return null;
        }
        AppointmentRestDTO appointmentRestDTO = new AppointmentRestDTO();
        appointmentRestDTO = modelMapper.map(appointment, AppointmentRestDTO.class);
        if(appointment.getService() != null){
            appointmentRestDTO.setDescription(serviceMapper.toDTO(appointment.getService()).getDescription());
        } else {
            appointmentRestDTO.setDescription(null);
        }
        return appointmentRestDTO;
    }

    /**
     * Convierte un DTO {@link AppointmentRestDTO} en una entidad {@link AppointmentEntity}.
     *
     * @param appointmentRestDTO el DTO que representa una cita.
     * @return la entidad correspondiente, o {@code null} si la entrada es {@code null}.
     */
    public AppointmentEntity toEntity(AppointmentRestDTO appointmentRestDTO){
        if(appointmentRestDTO == null){
            return null;
        }
        return modelMapper.map(appointmentRestDTO, AppointmentEntity.class);
    }
}
