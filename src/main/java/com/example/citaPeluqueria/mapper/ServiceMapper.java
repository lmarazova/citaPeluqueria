package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.dtos.UserRestDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.entities.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper{

    @Autowired
    ModelMapper modelMapper;
    public ServiceDTO toDTO(ServiceEntity service){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO = modelMapper.map(service, ServiceDTO.class);
        return serviceDTO;
    }
    public ServiceEntity toEntity(ServiceDTO serviceDTO){
        return modelMapper.map(serviceDTO, ServiceEntity.class);
    }
}
