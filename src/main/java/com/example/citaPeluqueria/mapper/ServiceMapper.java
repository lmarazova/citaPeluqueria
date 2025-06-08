package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de mapear entre {@link ServiceEntity} y {@link ServiceDTO}.
 * Utiliza {@link ModelMapper} para realizar conversiones automáticas de campos.
 */
@Component
public class ServiceMapper{

    @Autowired
    ModelMapper modelMapper;

    /**
     * Convierte una entidad {@link ServiceEntity} a un DTO {@link ServiceDTO}.
     *
     * @param service la entidad de servicio que se va a convertir.
     * @return el objeto {@link ServiceDTO} correspondiente.
     */
    public ServiceDTO toDTO(ServiceEntity service){
        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO = modelMapper.map(service, ServiceDTO.class);
        return serviceDTO;
    }

    /**
     * Convierte un DTO {@link ServiceDTO} en una entidad {@link ServiceEntity}.
     *
     * @param serviceDTO el DTO que contiene los datos del servicio.
     * @return la entidad {@link ServiceEntity} correspondiente lista para persistencia.
     */
    public ServiceEntity toEntity(ServiceDTO serviceDTO){
        return modelMapper.map(serviceDTO, ServiceEntity.class);
    }
}
