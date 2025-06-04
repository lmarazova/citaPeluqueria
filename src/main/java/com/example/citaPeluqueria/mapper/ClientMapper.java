package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
/**
 * Componente que maneja la conversión entre {@link ClientEntity} y {@link UserRestDTO}.
 * Utiliza {@link ModelMapper} para mapear automáticamente los campos comunes.
 * También convierte las citas asociadas mediante {@link AppointmentMapper}.
 */
@Component
public class ClientMapper {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AppointmentMapper appointmentMapper;

    /**
     * Convierte una entidad {@link ClientEntity} a un DTO {@link UserRestDTO}, incluyendo sus citas.
     *
     * @param user la entidad del cliente a convertir.
     * @return el DTO correspondiente con los datos del cliente y sus citas.
     */
    public UserRestDTO toDTO(ClientEntity user){
        UserRestDTO userRestDTO = new UserRestDTO();
        userRestDTO = modelMapper.map(user, UserRestDTO.class);
        userRestDTO.setAppointmentDTOS(
                appointmentMapper.toDTOList(new ArrayList<>(user.getAppointments()))
        );
        return userRestDTO;
    }

    /**
     * Convierte un DTO {@link UserRestDTO} en una entidad {@link UserEntity}.
     * Nota: no incluye citas en la conversión inversa.
     *
     * @param userRestDTO el DTO que representa los datos del usuario.
     * @return la entidad correspondiente para persistencia.
     */
    public UserEntity toEntity(UserRestDTO userRestDTO){
        return modelMapper.map(userRestDTO, UserEntity.class);
    }
}
