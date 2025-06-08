package com.example.citaPeluqueria.mapper;

import com.example.citaPeluqueria.domain.dtos.ServiceDTO;
import com.example.citaPeluqueria.domain.dtos.restDTOS.ServiceBasicRestDTO;
import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * Componente encargado de mapear entre {@link HairServiceEntity} y {@link ServiceBasicRestDTO}.
 * También personaliza el nombre del servicio si se trata de un servicio personalizado (CUSTOM).
 */
@Component
public class ServiceBasicRestMapper {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * Convierte una entidad {@link HairServiceEntity} a un DTO {@link ServiceBasicRestDTO}.
     * Si el servicio es personalizado (CUSTOM), se utiliza la etiqueta personalizada.
     *
     * @param hairServiceEntity la entidad de servicio a convertir.
     * @return el DTO con el nombre apropiado del servicio.
     */
    public ServiceBasicRestDTO toDTO(HairServiceEntity hairServiceEntity){
        ServiceBasicRestDTO serviceBasicRestDTO = new ServiceBasicRestDTO();
        serviceBasicRestDTO = modelMapper.map(hairServiceEntity, ServiceBasicRestDTO.class);
        if(hairServiceEntity.getHairService()==HairService.CUSTOM){
            serviceBasicRestDTO.setServiceName(hairServiceEntity.getCustomLabel());
        }else{
            serviceBasicRestDTO.setServiceName(String.valueOf(hairServiceEntity.getHairService()));
        }
        return serviceBasicRestDTO;
    }

    /**
     * Convierte un DTO {@link ServiceBasicRestDTO} en una entidad {@link HairServiceEntity}.
     *
     * @param serviceBasicRestDTO el DTO del servicio.
     * @return la entidad correspondiente.
     */
    public HairServiceEntity toEntity(ServiceBasicRestDTO serviceBasicRestDTO){
        return modelMapper.map(serviceBasicRestDTO, HairServiceEntity.class);
    }

}
