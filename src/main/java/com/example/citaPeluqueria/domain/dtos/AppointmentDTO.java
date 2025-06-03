package com.example.citaPeluqueria.domain.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
public class AppointmentDTO {
    private String Date;
    private String selectedHourRange;
    private ServiceDTO service;
}
