package com.example.citaPeluqueria.services;

import com.example.citaPeluqueria.domain.entities.ServiceEntity;
import com.example.citaPeluqueria.repositories.ServiceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService{
    private final ObjectMapper mapper;
    private final ServiceRepository serviceRepository;

    public ServiceServiceImpl(ObjectMapper mapper, ServiceRepository serviceRepository) {
        this.mapper = mapper;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceEntity getById(Long packageId) {

        return serviceRepository.findById(packageId).orElse(null);
    }
    @Override
    public String buildLabelFromSelectedServices(List<Map<String, String>> selectedServices) {
        return selectedServices.stream()
                .map(service -> service.get("label"))
                .collect(Collectors.joining(" + "));
    }

    @Override
    public void updateService(Long id, double price, boolean isActive) {
        serviceRepository.findById(id).ifPresent(service -> {
            service.setPrice(price);
            service.setActive(isActive);
            serviceRepository.save(service);
        });
    }


}
