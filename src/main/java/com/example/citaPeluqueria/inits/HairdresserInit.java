package com.example.citaPeluqueria.inits;

import com.example.citaPeluqueria.domain.entities.HairdresserEntity;
import com.example.citaPeluqueria.repositories.HairdresserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HairdresserInit /*implements CommandLineRunner */{

   /* private final HairdresserRepository hairdresserRepository;

    public HairdresserInit(HairdresserRepository hairdresserRepository) {
        this.hairdresserRepository = hairdresserRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(hairdresserRepository.count() > 0){
            return;
        }

        HairdresserEntity dani = new HairdresserEntity();
        dani.setName("DANI");
        HairdresserEntity ana = new HairdresserEntity();
        ana.setName("ANA");
        HairdresserEntity carla = new HairdresserEntity();
        carla.setName("CARLA");
        hairdresserRepository.saveAll(List.of(dani, ana, carla));
    }*/
}
