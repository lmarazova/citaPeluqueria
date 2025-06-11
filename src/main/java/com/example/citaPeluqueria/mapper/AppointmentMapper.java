package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
/**
 * Componente responsable de mapear entre entidades de cita ({@link AppointmentEntity}) y sus correspondientes DTOs ({@link AppointmentDTO}).
 * Utiliza {@link ModelMapper} para la conversión automática de campos y delega el mapeo de servicios a {@link ServiceMapper}.
 */
@Component
public class AppointmentMapper {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ServiceMapper serviceMapper;

    /**
     * Convierte una entidad de cita en un DTO, incluyendo el mapeo del servicio asociado.
     *
     * @param appointment La entidad de cita a convertir.
     * @return Un objeto {@link AppointmentDTO} con los datos correspondientes.
     */
    public AppointmentDTO toDTO(AppointmentEntity appointment) {
        AppointmentDTO appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);
        if (appointment.getService() != null) {
            appointmentDTO.setService(serviceMapper.toDTO(appointment.getService()));
        }
        return appointmentDTO;
    }

    /**
     * Convierte un DTO de cita en una entidad.
     * No incluye la conversión del objeto {@code service}, ya que puede necesitar tratamiento adicional.
     *
     * @param appointmentDTO El DTO de la cita a convertir.
     * @return Una nueva instancia de {@link AppointmentEntity} con los datos del DTO.
     */
    public AppointmentEntity toEntity(AppointmentDTO appointmentDTO){
        AppointmentEntity appointment = new AppointmentEntity();
        appointment = modelMapper.map(appointmentDTO, AppointmentEntity.class);
        return appointment;
    }

    /**
     * Convierte una lista de entidades de cita en una lista de DTOs.
     *
     * @param appointments Lista de entidades {@link AppointmentEntity}.
     * @return Lista de objetos {@link AppointmentDTO} equivalentes.
     */
    public List<AppointmentDTO> toDTOList(List<AppointmentEntity> appointments) {
        return appointments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
