package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.restDTOS.ServiceBasicRestDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.ServicePackRestDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de mapear entre {@link ServiceEntity} y {@link ServicePackRestDTO}.
 * Utiliza {@link ModelMapper} para realizar el mapeo automático de los campos.
 */
@Component
public class ServicePackRestMapper {
    @Autowired
    ModelMapper modelMapper;

    /**
     * Convierte una entidad {@link ServiceEntity} a un DTO {@link ServicePackRestDTO}.
     *
     * @param service la entidad de servicio a convertir.
     * @return el DTO correspondiente con los datos del servicio.
     */
    public ServicePackRestDTO toDTO(ServiceEntity service){
        ServicePackRestDTO servicePackRestDTO = new ServicePackRestDTO();
        servicePackRestDTO = modelMapper.map(service, ServicePackRestDTO.class);
        return servicePackRestDTO;
    }

    /**
     * Convierte un DTO {@link ServicePackRestDTO} en una entidad {@link ServiceEntity}.
     *
     * @param servicePackRestDTO el DTO del servicio.
     * @return la entidad correspondiente lista para persistencia.
     */
    public ServiceEntity toEntity(ServicePackRestDTO servicePackRestDTO){
        return modelMapper.map(servicePackRestDTO, ServiceEntity.class);
    }


}
