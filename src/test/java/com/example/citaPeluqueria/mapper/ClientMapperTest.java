package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.AppointmentRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ClientEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTest {

    @InjectMocks
    private ClientMapper clientMapper;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Test
    void toDTO_shouldMapEntityToDto_withAppointments() {
        ClientEntity clientEntity = new ClientEntity();
        AppointmentEntity appointmentEntity = new AppointmentEntity();
        clientEntity.setAppointments(Set.of(appointmentEntity));

        UserRestDTO userRestDTO = new UserRestDTO();
        List<AppointmentDTO> appointmentDTOList = List.of(new AppointmentDTO());

        when(modelMapper.map(clientEntity, UserRestDTO.class)).thenReturn(userRestDTO);
        when(appointmentMapper.toDTOList(Mockito.<List<AppointmentEntity>>any())).thenReturn(appointmentDTOList);

        UserRestDTO result = clientMapper.toDTO(clientEntity);

        assertNotNull(result);
        assertEquals(appointmentDTOList, result.getAppointmentDTOS());
    }

    @Test
    void toEntity_shouldMapUserRestDTOToUserEntity() {

        UserRestDTO userRestDTO = new UserRestDTO();
        UserEntity userEntity = new UserEntity();

        when(modelMapper.map(userRestDTO, UserEntity.class)).thenReturn(userEntity);

        UserEntity result = clientMapper.toEntity(userRestDTO);

        assertNotNull(result);
        assertEquals(userEntity, result);
        verify(modelMapper).map(userRestDTO, UserEntity.class);
    }
}
