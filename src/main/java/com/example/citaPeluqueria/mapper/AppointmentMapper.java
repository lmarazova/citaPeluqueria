package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
@Component
public class AppointmentMapper {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ServiceMapper serviceMapper;
    public AppointmentDTO toDTO(AppointmentEntity appointment){
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);
        appointmentDTO.setService(serviceMapper.toDTO(appointment.getService()));
        return appointmentDTO;
    }
    public AppointmentEntity toEntity(AppointmentDTO appointmentDTO){
        AppointmentEntity appointment = new AppointmentEntity();
        appointment = modelMapper.map(appointmentDTO, AppointmentEntity.class);
        return appointment;
    }
    public List<AppointmentDTO> toDTOList(List<AppointmentEntity> appointments) {
        return appointments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
