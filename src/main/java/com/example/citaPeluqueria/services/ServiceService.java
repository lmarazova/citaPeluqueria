package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.dtos.CustomServicePackageDTO;
import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface ServiceService {
    ServiceEntity getById(Long packageId);


    String buildLabelFromSelectedServices(List<Map<String, String>> selectedServices);


    void updateService(Long id, double price, boolean isActive);
}
