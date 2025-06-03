package com.example.citaPeluqueria.inits;

import com.example.citaPeluqueria.domain.entities.HairServiceEntity;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.domain.enums.HairService;
import com.example.citaPeluqueria.domain.enums.ServicePackageEnum;
import com.example.citaPeluqueria.repositories.HairServiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class HairServiceInit implements CommandLineRunner {

    private final HairServiceRepository hairServiceRepository;

    public HairServiceInit(HairServiceRepository hairServiceRepository) {
        this.hairServiceRepository = hairServiceRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (hairServiceRepository.count() > 0) {
            return;
        }
        List<HairServiceEntity> hairServices = Arrays.stream(HairService.values())
                .map(service -> {
                    HairServiceEntity hairService = new HairServiceEntity();
                    hairService.setHairService(service);
                    return hairService;
                })
                .toList();

        hairServiceRepository.saveAll(hairServices);
    }
}
