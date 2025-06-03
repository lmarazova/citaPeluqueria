package com.example.citaPeluqueria.domain.dtos;

import com.example.citaPeluqueria.domain.entities.AppointmentEntity;

import java.util.Set;

public class ClientShowTableDTO {
    private String username;
    private String email;
    private String phone;
    private String comments;
    private Set<AppointmentEntity> appointments;
}
