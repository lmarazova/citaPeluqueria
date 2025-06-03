package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ClientMapper {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AppointmentMapper appointmentMapper;
    public UserRestDTO toDTO(ClientEntity user){
        UserRestDTO userRestDTO = new UserRestDTO();
        userRestDTO = modelMapper.map(user, UserRestDTO.class);
        userRestDTO.setAppointmentDTOS(
                appointmentMapper.toDTOList(new ArrayList<>(user.getAppointments()))
        );
        return userRestDTO;
    }
    public UserEntity toEntity(UserRestDTO userRestDTO){
        return modelMapper.map(userRestDTO, UserEntity.class);
    }
}
