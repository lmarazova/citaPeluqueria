package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.AppointmentDTO;
import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.entities.AppointmentEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ServiceMapper serviceMapper;

    @InjectMocks
    private AppointmentMapper appointmentMapper;

    @Test
    void toDTO_shouldMapEntityToDTO_andSetServiceDTO() {
        AppointmentEntity entity = new AppointmentEntity();
        ServiceEntity serviceEntity = new ServiceEntity();
        entity.setService(serviceEntity);

        AppointmentDTO mappedDTO = new AppointmentDTO();
        ServiceDTO serviceDTO = new ServiceDTO();

        when(modelMapper.map(entity, AppointmentDTO.class)).thenReturn(mappedDTO);
        when(serviceMapper.toDTO(serviceEntity)).thenReturn(serviceDTO);

        AppointmentDTO result = appointmentMapper.toDTO(entity);

        verify(modelMapper).map(entity, AppointmentDTO.class);
        verify(serviceMapper).toDTO(serviceEntity);

        assertSame(mappedDTO, result);

        assertEquals(serviceDTO, result.getService());
    }

    @Test
    void toEntity_shouldMapDTOToEntity() {
        AppointmentDTO dto = new AppointmentDTO();

        AppointmentEntity mappedEntity = new AppointmentEntity();

        // Mock modelMapper.map for dto to entity
        when(modelMapper.map(dto, AppointmentEntity.class)).thenReturn(mappedEntity);

        AppointmentEntity result = appointmentMapper.toEntity(dto);

        // Verify modelMapper.map called correctly
        verify(modelMapper).map(dto, AppointmentEntity.class);

        // Assert result equals mocked mapped entity
        assertSame(mappedEntity, result);
    }

  @Test
    void toDTOList_shouldConvertListOfEntitiesToListOfDTOs() {
        AppointmentEntity entity1 = new AppointmentEntity();
        AppointmentEntity entity2 = new AppointmentEntity();
        List<AppointmentEntity> entities = List.of(entity1, entity2);

        AppointmentDTO dto1 = new AppointmentDTO();
        AppointmentDTO dto2 = new AppointmentDTO();

        // Spy the mapper to mock toDTO method behavior
        AppointmentMapper spyMapper = spy(appointmentMapper);

        doReturn(dto1).when(spyMapper).toDTO(entity1);
        doReturn(dto2).when(spyMapper).toDTO(entity2);

        List<AppointmentDTO> result = spyMapper.toDTOList(entities);

        // Assert that the list size is correct and contains the expected DTOs
        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
    }

    @Test
    void toDTO_shouldHandleNullServiceGracefully() {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setService(null);  // Servicio null

        AppointmentDTO mappedDTO = new AppointmentDTO();
        when(modelMapper.map(appointment, AppointmentDTO.class)).thenReturn(mappedDTO);

        AppointmentDTO result = appointmentMapper.toDTO(appointment);

        verify(modelMapper).map(appointment, AppointmentDTO.class);
        verify(serviceMapper, never()).toDTO(any());

        assertSame(mappedDTO, result);
    }



    @Test
    void toDTOList_shouldReturnEmptyList_whenInputIsEmpty() {
        List<AppointmentEntity> emptyList = Collections.emptyList();

        List<AppointmentDTO> result = appointmentMapper.toDTOList(emptyList);

        assertTrue(result.isEmpty());
    }
}