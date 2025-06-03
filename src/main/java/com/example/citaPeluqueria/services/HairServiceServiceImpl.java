package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.enums.HairService;
import org.springframework.stereotype.Service;

@Service
public class HairServiceServiceImpl implements HairServiceService{


    @Override
    public HairService fromLabel(String label) {
        for(HairService service : HairService.values()){
            if(service.getLabel().equalsIgnoreCase(label.trim())){
                return service;
            }
        }
        return null;
    }
}
