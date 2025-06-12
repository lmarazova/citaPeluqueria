package com.example.citaPeluqueria.integration;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ServiceRepositoryIT {

    @Autowired
    private ServiceRepository serviceRepository;

   @Test
    void testGuardarYBuscarServicio() {
        ServiceEntity service = new ServiceEntity();
        service.setDescription("Lavado + Corte");
        service.setPrice(30.0);
        service.setTotalDuration(60);
        service.setPackageEnum(ServicePackageEnum.CUT_WASH_BLOWDRY);
        service.isActive();
        service.setServices(List.of(HairService.WASH, HairService.CUT));

        service = serviceRepository.save(service);

        Optional<ServiceEntity> encontrado = serviceRepository.findById(service.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getDescription()).contains("Corte");
    }
}